package com.pbde401.studyworks.ui.candidate.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.repository.JobsRepository;
import com.pbde401.studyworks.data.models.Job;
import java.util.List;

public class CandidateJobsViewModel extends ViewModel {
    private final JobsRepository jobsRepository;
    private LiveData<List<Job>> activeJobs;

    public CandidateJobsViewModel() {
        jobsRepository = new JobsRepository();
        activeJobs = jobsRepository.getActiveJobs();
    }

    public LiveData<List<Job>> getActiveJobs() {
        return activeJobs;
    }

    public LiveData<List<Job>> getCandidateJobs(String candidateId) {
        return jobsRepository.getCandidateJobs(candidateId);
    }
}
