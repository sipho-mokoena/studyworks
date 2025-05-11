package com.pbde401.studyworks.ui.employer.applications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;

public class EmployerSingleApplicationViewModel extends ViewModel {
    private final ApplicationsRepository repository;

    public EmployerSingleApplicationViewModel() {
        repository = new ApplicationsRepository();
    }

    public LiveData<Application> getApplication(String applicationId) {
        return repository.getApplication(applicationId);
    }
}