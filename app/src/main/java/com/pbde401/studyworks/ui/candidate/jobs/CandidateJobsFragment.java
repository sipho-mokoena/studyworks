package com.pbde401.studyworks.ui.candidate.jobs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Job;

public class CandidateJobsFragment extends Fragment implements JobListingAdapter.OnJobClickListener {
    private CandidateJobsViewModel viewModel;
    private JobListingAdapter adapter;
    private RecyclerView recyclerView;

    public CandidateJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CandidateJobsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_jobs, container, false);
        
        recyclerView = view.findViewById(R.id.rvJobs);
        adapter = new JobListingAdapter(this);
        recyclerView.setAdapter(adapter);

        // Observe active jobs
        viewModel.getActiveJobs().observe(getViewLifecycleOwner(), jobs -> {
            adapter.setJobs(jobs);
        });

        return view;
    }

    @Override
    public void onJobClick(Job job) {
        // Navigate to job details
        Bundle bundle = new Bundle();
        bundle.putString("jobId", job.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_candidate_jobs_to_navigation_candidate_single_job, bundle);
    }
}