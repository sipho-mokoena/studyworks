package com.pbde401.studyworks.ui.candidate.applications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.pbde401.studyworks.data.models.Job;
import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;
import com.pbde401.studyworks.data.repository.JobsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateApplicationsViewModel extends ViewModel {
    private final ApplicationsRepository applicationsRepository;
    private final JobsRepository jobsRepository;
    
    private final MutableLiveData<List<Application>> applications = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Job>>> applicationsJobs = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public CandidateApplicationsViewModel() {
        applicationsRepository = new ApplicationsRepository();
        jobsRepository = new JobsRepository();
        loadApplicationsAndJobs();
    }

    private void loadApplicationsAndJobs() {
        loading.setValue(true);
        error.setValue(null);

        applicationsRepository.getCandidateApplications().observeForever(applicationList -> {
            if (applicationList != null) {
                applications.setValue(applicationList);
                loadJobsForApplications(applicationList);
            } else {
                error.setValue("Failed to load applications");
                loading.setValue(false);
            }
        });
    }

    private void loadJobsForApplications(@NonNull List<Application> applicationList) {
        Map<String, List<Job>> jobsMap = new HashMap<>();
        int totalApplications = applicationList.size();
        final int[] loadedJobs = {0};

        for (Application application : applicationList) {
            String jobId = application.getJobId();
            jobsRepository.getJob(jobId).observeForever(job -> {
                if (job != null) {
                    List<Job> jobs = jobsMap.get(application.getId());
                    if (jobs == null) {
                        jobs = new ArrayList<>();
                        jobsMap.put(application.getId(), jobs);
                    }
                    jobs.add(job);
                }

                loadedJobs[0]++;
                if (loadedJobs[0] == totalApplications) {
                    applicationsJobs.setValue(jobsMap);
                    loading.setValue(false);
                }
            });
        }
    }

    public LiveData<List<Application>> getApplications() {
        return applications;
    }

    public MutableLiveData<Map<String, List<Job>>> getApplicationsJobs() {
        return applicationsJobs;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Clean up observers if needed
    }
}