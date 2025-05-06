package com.pbde401.studyworks.ui.candidate.applications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.ui.candidate.applications.CandidateApplicationsViewModel;
import com.pbde401.studyworks.ui.candidate.applications.JobApplicationAdapter;


public class CandidateApplicationsFragment extends Fragment {
    private CandidateApplicationsViewModel viewModel;
    private JobApplicationAdapter adapter;
    private RecyclerView recyclerView;

    public CandidateApplicationsFragment() {
        // Required empty public constructor
    }


    public static CandidateApplicationsFragment newInstance() {
        CandidateApplicationsFragment fragment = new CandidateApplicationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CandidateApplicationsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_applications, container, false);
        
        recyclerView = view.findViewById(R.id.rvJobApplications);
        adapter = new JobApplicationAdapter(this);
        recyclerView.setAdapter(adapter);

        // Observe applications data
        viewModel.getApplications().observe(getViewLifecycleOwner(), applications -> {
            adapter.setApplications(applications);
        });

        return view;
    }
}