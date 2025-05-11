package com.pbde401.studyworks.ui.candidate.jobs;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.Job;
import com.pbde401.studyworks.data.models.enums.ApplicationStatus;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;
import com.pbde401.studyworks.data.repository.ChatsRepository;
import com.pbde401.studyworks.data.repository.JobsRepository;
import com.pbde401.studyworks.data.repository.UserRepository;

import java.util.UUID;
import java.util.Date;

public class CandidateJobApplicationViewModel extends ViewModel {
    private final JobsRepository jobsRepository;
    private final ApplicationsRepository applicationsRepository;
    private final UserRepository userRepository;
    private final ChatsRepository chatsRepository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();private final MutableLiveData<Job> job = new MutableLiveData<>();
    private final MutableLiveData<Application> application = new MutableLiveData<>();
    private final MutableLiveData<Chat> chat = new MutableLiveData<>();

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

    public CandidateJobApplicationViewModel(
            JobsRepository jobsRepository,
            ApplicationsRepository applicationsRepository,
            UserRepository userRepository,
            ChatsRepository chatsRepository) {
        this.jobsRepository = jobsRepository;
        this.applicationsRepository = applicationsRepository;
        this.userRepository = userRepository;
        this.chatsRepository = chatsRepository;
    }

    public void init(String jobId, String userId) {
        this.jobId = jobId;
        this.userId = userId;
        loadJob();
        loadApplication();
    }

    public void loadJob() {
        loading.setValue(true);
        if (jobId == null) {
            error.setValue("Job ID is missing");
            loading.setValue(false);
            return;
        }

        jobsRepository.getJob(jobId).observeForever(jobData -> {
            if (jobData != null) {
                job.setValue(jobData);
            } else {
                error.setValue("Job not found");
            }
            loading.setValue(false);
        });
    }

    private void loadApplication() {
        if (jobId == null || userId == null) {
            error.setValue("Missing job or user information");
            return;
        }

        applicationsRepository.getApplicationByJobAndCandidateId(jobId, userId)
                .observeForever(applicationData -> {
                    application.setValue(applicationData);
                    if (applicationData != null) {
                        loadChat();
                    }
                });
    }

    private void loadChat() {
        Application currentApplication = application.getValue();
        if (currentApplication == null) return;

        // Assuming you have a method to get chat by employer and candidate
        chatsRepository.getChatByEmployerIdAndCandidateId(
                currentApplication.getEmployerId(),
                currentApplication.getCandidateId()
        ).observeForever(chat::setValue);
    }

    public LiveData<Job> getJob() {
        return jobsRepository.getJob(jobId);
    }

    public LiveData<Application> getApplication() {
        return applicationsRepository.getApplicationByJobAndCandidateId(jobId, userId);
    }

    public LiveData<Chat> getChat() {
        return chat;
    }

    public void submitApplication(String coverLetter, String portfolioUrl, String linkedinUrl) {
        if (jobId == null || userId == null) {
            error.setValue("Missing job or user information");
            return;
        }

        loading.setValue(true);
        error.setValue(null);

        // Get job details first
        jobsRepository.getJob(jobId).observeForever(job -> {
            if (job != null) {
                Application application = new Application(
                        UUID.randomUUID().toString(),
                        new Date(),
                        new Date(),
                        jobId,
                        userId,
                        job.getCompanyId(),
                        ApplicationStatus.SUBMITTED,
                        new Date(),
                        null, // resumeUrl
                        coverLetter,
                        portfolioUrl,
                        linkedinUrl,
                        40,
                        null // interview
                );

                applicationsRepository.createApplication(application);
                loading.setValue(false);
            } else {
                error.setValue("Failed to load job details");
                loading.setValue(false);
            }
        });
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }
}