package com.pbde401.studyworks.ui.employer.jobs;

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
import com.pbde401.studyworks.ui.candidate.jobs.JobListingAdapter;

public class EmployerJobsFragment extends Fragment implements JobListingAdapter.OnJobClickListener {
    private EmployerJobsViewModel viewModel;
    private JobListingAdapter adapter;
    private RecyclerView recyclerView;

    public EmployerJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EmployerJobsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer_jobs, container, false);
        
        recyclerView = view.findViewById(R.id.rvEmployerJobs);
        adapter = new JobListingAdapter(this);
        recyclerView.setAdapter(adapter);

        // TODO: Get employer ID from authentication or preferences
        String employerId = "current_employer_id";
        
        // Observe employer's jobs
        viewModel.getEmployerJobs(employerId).observe(getViewLifecycleOwner(), jobs -> {
            adapter.setJobs(jobs);
        });

        return view;
    }

    @Override
    public void onJobClick(Job job) {
        // Navigate to job details
        Bundle bundle = new Bundle();
        bundle.putString("jobId", job.getId());
        // TODO: Implement navigation to job details
        // Navigation.findNavController(requireView())
        //         .navigate(R.id.action_employerJobsFragment_to_jobDetailsFragment, bundle);
    }
}