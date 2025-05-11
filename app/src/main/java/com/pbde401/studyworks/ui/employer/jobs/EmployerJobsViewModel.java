package com.pbde401.studyworks.ui.employer.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.repository.JobsRepository;
import com.pbde401.studyworks.data.models.Job;
import java.util.List;

public class EmployerJobsViewModel extends ViewModel {
    private final JobsRepository jobsRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private String currentEmployerId;

    public EmployerJobsViewModel() {
        jobsRepository = new JobsRepository();
    }

    public LiveData<List<Job>> getEmployerJobs(String employerId) {
        currentEmployerId = employerId;
        return jobsRepository.getEmployerJobs(employerId);
    }

    public LiveData<List<Job>> getFilteredEmployerJobs(String workMode, String jobType) {
        return jobsRepository.getFilteredEmployerJobs(currentEmployerId, workMode, jobType);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }
}
