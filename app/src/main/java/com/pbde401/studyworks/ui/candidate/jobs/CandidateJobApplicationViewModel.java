package com.pbde401.studyworks.ui.candidate.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;
import java.util.Date;

public class CandidateJobApplicationViewModel extends ViewModel {
    private final MutableLiveData<JobDetail> job = new MutableLiveData<>();
    private final MutableLiveData<ApplicationData> application = new MutableLiveData<>();
    private final MutableLiveData<ChatData> chat = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private String jobId;
    private String userId;

    public static class JobDetail {
        public String id;
        public String title;
        public String company;
        public String companyId;
        public String location;
        public String type;
        public String level;
        public String salary;
        public String posted;
        public String description;
        // Add other fields as needed
    }

    public static class ApplicationData {
        public String id;
        public String jobId;
        public String candidateId;
        public String employerId;
        public String status;
        public int progress;
        public String appliedAt;
        public String coverLetter;
        public String portfolioUrl;
        public String linkedinUrl;
    }

    public static class ChatData {
        public String id;
        public String employerId;
        public String candidateId;
    }

    public void init(String jobId, String userId) {
        this.jobId = jobId;
        this.userId = userId;
        loadJob();
        loadApplication();
    }

    public void loadJob() {
        loading.setValue(true);
        // TODO: Replace with actual API call
        // Simulated API call
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                JobDetail jobDetail = new JobDetail();
                // Populate with mock data for now
                jobDetail.id = jobId;
                jobDetail.title = "Software Engineer";
                jobDetail.company = "Tech Corp";
                // ... populate other fields
                
                job.postValue(jobDetail);
                loading.postValue(false);
            } catch (Exception e) {
                error.postValue("Failed to load job: " + e.getMessage());
                loading.postValue(false);
            }
        }).start();
    }

    private void loadApplication() {
        if (jobId == null || userId == null) return;
        
        // TODO: Replace with actual API call
        // Similar implementation as loadJob()
    }

    private void loadChat() {
        ApplicationData currentApplication = application.getValue();
        if (currentApplication == null) return;

        // TODO: Replace with actual API call
        // Similar implementation as loadJob()
    }

    public void submitApplication(String coverLetter, String portfolioUrl, String linkedinUrl) {
        if (job.getValue() == null) {
            error.setValue("Job information not available");
            return;
        }

        loading.setValue(true);
        error.setValue(null);

        ApplicationData newApplication = new ApplicationData();
        newApplication.id = UUID.randomUUID().toString();
        newApplication.jobId = jobId;
        newApplication.candidateId = userId;
        newApplication.employerId = job.getValue().companyId;
        newApplication.status = "Submitted";
        newApplication.progress = 40;
        newApplication.appliedAt = new Date().toString();
        newApplication.coverLetter = coverLetter;
        newApplication.portfolioUrl = portfolioUrl;
        newApplication.linkedinUrl = linkedinUrl;

        // TODO: Replace with actual API call
        // Simulated API call
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                application.postValue(newApplication);
                loading.postValue(false);
            } catch (Exception e) {
                error.postValue("Failed to submit application: " + e.getMessage());
                loading.postValue(false);
            }
        }).start();
    }

    // Getters for LiveData
    public LiveData<JobDetail> getJob() { return job; }
    public LiveData<ApplicationData> getApplication() { return application; }
    public LiveData<ChatData> getChat() { return chat; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }
}