package com.pbde401.studyworks.ui.employer.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.enums.ApplicationStatus;

public class EmployerSingleJobViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<Application> application = new MutableLiveData<>();
    private final MutableLiveData<Candidate> candidate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<Application> getApplication() { return application; }
    public LiveData<Candidate> getCandidate() { return candidate; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }

    public void loadApplication(String applicationId) {
        isLoading.setValue(true);
        error.setValue(null);

        db.collection("applications")
            .document(applicationId)
            .get()
            .addOnSuccessListener(doc -> {
                Application app = doc.toObject(Application.class);
                if (app != null) {
                    application.setValue(app);
                    loadCandidateDetails(app.getCandidateId());
                } else {
                    error.setValue("Application not found");
                    isLoading.setValue(false);
                }
            })
            .addOnFailureListener(e -> {
                error.setValue("Failed to load application: " + e.getMessage());
                isLoading.setValue(false);
            });
    }

    private void loadCandidateDetails(String candidateId) {
        db.collection("candidates")
            .document(candidateId)
            .get()
            .addOnSuccessListener(doc -> {
                Candidate cand = doc.toObject(Candidate.class);
                if (cand != null) {
                    candidate.setValue(cand);
                }
                isLoading.setValue(false);
            })
            .addOnFailureListener(e -> {
                error.setValue("Failed to load candidate details: " + e.getMessage());
                isLoading.setValue(false);
            });
    }

    public void updateApplicationStatus(String applicationId, String newStatus) {
        db.collection("applications")
            .document(applicationId)
            .update("status", newStatus)
            .addOnSuccessListener(v -> {
                Application currentApp = application.getValue();
                if (currentApp != null) {
                    currentApp.setStatus(ApplicationStatus.fromString(newStatus));
                    application.setValue(currentApp);
                }
            })
            .addOnFailureListener(e -> 
                error.setValue("Failed to update status: " + e.getMessage()));
    }

    public void createChat(String employerId, String candidateId, OnChatCreatedListener listener) {
        // ...
    }

    public interface OnChatCreatedListener {
        void onChatCreated(String chatId);
    }
}
