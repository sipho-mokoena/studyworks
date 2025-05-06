package com.pbde401.studyworks.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.models.ApplicationInterview;
import com.pbde401.studyworks.data.models.enums.ApplicationStatus;
import com.pbde401.studyworks.util.AuthManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ApplicationsRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String APPLICATIONS_COLLECTION = "applications";
    private final AuthManager authManager = AuthManager.getInstance();

    public LiveData<List<Application>> getCandidateApplications() {
        MutableLiveData<List<Application>> applicationsLiveData = new MutableLiveData<>();

        db.collection(APPLICATIONS_COLLECTION)
            .whereEqualTo("candidateId", getCurrentUserId())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Application> applications = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Application application = documentToApplication(document);
                    applications.add(application);
                }
                applicationsLiveData.setValue(applications);
            })
            .addOnFailureListener(e -> {
                // Handle any errors
                applicationsLiveData.setValue(new ArrayList<>());
            });

        return applicationsLiveData;
    }

    @NonNull
    private Application documentToApplication(DocumentSnapshot document) {
        String id = document.getId();
        String jobId = document.getString("jobId");
        String candidateId = document.getString("candidateId");
        String employerId = document.getString("employerId");
        ApplicationStatus status = ApplicationStatus.fromString(document.getString("status"));
        String coverLetter = document.getString("coverLetter");
        String resumeUrl = document.getString("resumeUrl");
        String portfolioUrl = document.getString("portfolioUrl");
        String linkedinUrl = document.getString("linkedinUrl");
        ApplicationInterview applicationInterview = null;
        Integer progress = document.getLong("progress").intValue();
        
        String createdAtString = document.getString("createdAt");
        String updatedAtString = document.getString("updatedAt");
        String appliedAtString = document.getString("appliedAt");
        Date createdAt = parseDate(createdAtString);
        Date updatedAt = parseDate(updatedAtString);
        Date appliedAt = parseDate(appliedAtString);

        return new Application(id, createdAt, updatedAt,
                jobId, candidateId, employerId,
                status, appliedAt,
                resumeUrl, coverLetter,
                portfolioUrl, linkedinUrl,
                progress, applicationInterview
        );
    }

    private String getCurrentUserId() {
        return authManager.getCurrentUser().getValue() != null ? 
            authManager.getCurrentUser().getValue().getId() : 
            null;
    }

    private Date parseDate(String dateString) {
        if (dateString == null) return null;
        try {
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601Format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
