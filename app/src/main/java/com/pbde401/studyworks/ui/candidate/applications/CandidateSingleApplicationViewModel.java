package com.pbde401.studyworks.ui.candidate.applications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;

public class CandidateSingleApplicationViewModel extends ViewModel {
    private final ApplicationsRepository repository;

    public CandidateSingleApplicationViewModel() {
        repository = new ApplicationsRepository();
    }

    public LiveData<Application> getApplication(String applicationId) {
        return repository.getApplication(applicationId);
    }
}