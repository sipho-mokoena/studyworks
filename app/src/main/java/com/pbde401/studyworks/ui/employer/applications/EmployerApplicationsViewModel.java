package com.pbde401.studyworks.ui.employer.applications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;
import com.pbde401.studyworks.data.models.Application;
import java.util.List;

public class EmployerApplicationsViewModel extends ViewModel {
    private final ApplicationsRepository applicationsRepository;
    private LiveData<List<Application>> applications;

    public EmployerApplicationsViewModel() {
        applicationsRepository = new ApplicationsRepository();
    }

    public LiveData<List<Application>> getEmployerApplications(String employerId) {
        return applicationsRepository.getEmployerApplications(employerId);
    }
}
