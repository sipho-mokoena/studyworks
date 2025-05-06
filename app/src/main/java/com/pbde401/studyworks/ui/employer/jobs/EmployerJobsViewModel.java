package com.pbde401.studyworks.ui.employer.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.repository.JobsRepository;
import com.pbde401.studyworks.data.models.Job;
import java.util.List;

public class EmployerJobsViewModel extends ViewModel {
    private final JobsRepository jobsRepository;
    private LiveData<List<Job>> employerJobs;

    public EmployerJobsViewModel() {
        jobsRepository = new JobsRepository();
    }

    public LiveData<List<Job>> getEmployerJobs(String employerId) {
        return jobsRepository.getEmployerJobs(employerId);
    }
}
