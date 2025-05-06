package com.pbde401.studyworks.ui.candidate.applications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;
import com.pbde401.studyworks.data.models.Application;
import java.util.List;

public class CandidateApplicationsViewModel extends ViewModel {
    private final ApplicationsRepository applicationsRepository;
    private LiveData<List<Application>> applications;

    public CandidateApplicationsViewModel() {
        applicationsRepository = new ApplicationsRepository();
    }

    public LiveData<List<Application>> getApplications() {
        if (applications == null) {
            applications = applicationsRepository.getCandidateApplications();
        }
        return applications;
    }
}
