package com.pbde401.studyworks.ui.candidate.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.repository.JobsRepository;
import com.pbde401.studyworks.data.models.Job;
import java.util.List;

public class CandidateJobsViewModel extends ViewModel {
    private final JobsRepository jobsRepository;
    private LiveData<List<Job>> activeJobs;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public CandidateJobsViewModel() {
        jobsRepository = new JobsRepository();
        loadActiveJobs();
    }

    private void loadActiveJobs() {
        isLoading.setValue(true);
        activeJobs = jobsRepository.getActiveJobs();
        isLoading.setValue(false);
    }

    public LiveData<List<Job>> getActiveJobs() {
        return activeJobs;
    }

    public LiveData<List<Job>> getCandidateJobs(String candidateId) {
        isLoading.setValue(true);
        LiveData<List<Job>> jobs = jobsRepository.getCandidateJobs(candidateId);
        isLoading.setValue(false);
        return jobs;
    }

    public LiveData<List<Job>> getFilteredJobs(String workMode, String jobType) {
        isLoading.setValue(true);
        LiveData<List<Job>> jobs = jobsRepository.getFilteredJobs(workMode, jobType);
        isLoading.setValue(false);
        return jobs;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
