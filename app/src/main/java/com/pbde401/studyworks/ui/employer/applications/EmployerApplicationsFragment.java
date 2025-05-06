package com.pbde401.studyworks.ui.employer.applications;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.ui.employer.applications.JobApplicationAdapter;

public class EmployerApplicationsFragment extends Fragment {
    private EmployerApplicationsViewModel viewModel;
    private JobApplicationAdapter adapter;
    private RecyclerView recyclerView;

    public EmployerApplicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EmployerApplicationsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer_applications, container, false);
        
        recyclerView = view.findViewById(R.id.rvEmployerApplications);
        adapter = new JobApplicationAdapter(this);
        recyclerView.setAdapter(adapter);

        // TODO: Get employer ID from authentication or preferences
        String employerId = "current_employer_id";
        
        // Observe employer applications
        viewModel.getEmployerApplications(employerId).observe(getViewLifecycleOwner(), applications -> {
            adapter.setApplications(applications);
        });

        return view;
    }
}