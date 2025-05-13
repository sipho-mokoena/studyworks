package com.pbde401.studyworks.ui.employer.applications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.Job;
import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.data.models.User;
import com.pbde401.studyworks.data.models.enums.ApplicationStatus;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;
import com.pbde401.studyworks.data.repository.JobsRepository;
import com.pbde401.studyworks.data.repository.UserRepository;
import com.pbde401.studyworks.data.repository.ChatsRepository;

public class EmployerSingleApplicationViewModel extends ViewModel {
    private final ApplicationsRepository repository;
    private final JobsRepository jobsRepository;
    private final UserRepository userRepository;
    private final ChatsRepository chatsRepository;

    public Job job;
    public Application application;
    public Candidate candidate;

    public EmployerSingleApplicationViewModel() {
        repository = new ApplicationsRepository();
        jobsRepository = new JobsRepository();
        userRepository = new UserRepository();
        chatsRepository = new ChatsRepository();
    }

    public LiveData<Application> getApplication(String applicationId) {
        if (applicationId == null) {
            return new MutableLiveData<>(null);
        }
        return repository.getApplication(applicationId);
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public LiveData<Job> getJob(String jobId) {
        if (jobId == null) {
            return new MutableLiveData<>(null);
        }
        return jobsRepository.getJob(jobId);
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public LiveData<User> getCandidateUser(String candidateId) {
        MutableLiveData<User> candidateUser = new MutableLiveData<>();
        
        if (candidateId == null) {
            candidateUser.setValue(null);
            return candidateUser;
        }

        userRepository.getUserById(candidateId).addOnSuccessListener(user -> {
            candidateUser.setValue(user);
        }).addOnFailureListener(e -> {
            candidateUser.setValue(null);
        });

        return candidateUser;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }


    public void setApplicationStatus(String applicationId, ApplicationStatus status) {
        repository.updateApplicationStatus(applicationId, status);
    }

    public void scheduleInterview(String applicationId) {
        setApplicationStatus(applicationId, ApplicationStatus.INTERVIEW);
    }

    public void rejectApplication(String applicationId) {
        setApplicationStatus(applicationId, ApplicationStatus.REJECTED);
    }

    public LiveData<Chat> findOrCreateChat(String employerId, String candidateId) {
        return chatsRepository.findOrCreateChat(employerId, candidateId);
    }
}