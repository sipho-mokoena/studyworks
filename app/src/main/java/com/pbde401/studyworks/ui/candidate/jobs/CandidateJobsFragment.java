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
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class CandidateJobsFragment extends Fragment implements JobListingAdapter.OnJobClickListener {
    private CandidateJobsViewModel viewModel;
    private JobListingAdapter adapter;
    private RecyclerView recyclerView;
    private AutoCompleteTextView workModeDropdown;
    private AutoCompleteTextView jobTypeDropdown;
    private MaterialButton btnApplyFilters;
    private MaterialButton btnClearFilters;
    private ProgressBar loadingIndicator;
    private TextView emptyStateText;

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
        
        // Initialize views
        recyclerView = view.findViewById(R.id.rvJobs);
        workModeDropdown = view.findViewById(R.id.workModeDropdown);
        jobTypeDropdown = view.findViewById(R.id.jobTypeDropdown);
        btnApplyFilters = view.findViewById(R.id.btnApplyFilters);
        btnClearFilters = view.findViewById(R.id.btnClearFilters);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);
        emptyStateText = view.findViewById(R.id.emptyStateText);

        // Setup RecyclerView
        adapter = new JobListingAdapter(this);
        recyclerView.setAdapter(adapter);

        // Setup dropdowns
        ArrayAdapter<CharSequence> workModeAdapter = ArrayAdapter.createFromResource(
            getContext(), R.array.work_mode_options, R.layout.dropdown_menu_popup_item);
        workModeDropdown.setAdapter(workModeAdapter);

        ArrayAdapter<CharSequence> jobTypeAdapter = ArrayAdapter.createFromResource(
            getContext(), R.array.job_type_options, R.layout.dropdown_menu_popup_item);
        jobTypeDropdown.setAdapter(jobTypeAdapter);

        // Setup button listeners
        btnApplyFilters.setOnClickListener(v -> applyFilters());
        btnClearFilters.setOnClickListener(v -> clearFilters());

        // Observe jobs data
        viewModel.getActiveJobs().observe(getViewLifecycleOwner(), this::updateJobsUI);

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
        });

        return view;
    }

    private void updateJobsUI(List<Job> jobs) {
        adapter.setJobs(jobs);
        recyclerView.setVisibility(jobs.isEmpty() ? View.GONE : View.VISIBLE);
        emptyStateText.setVisibility(jobs.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onJobClick(Job job) {
        // Navigate to job details
        Bundle bundle = new Bundle();
        bundle.putString("jobId", job.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_candidate_jobs_to_navigation_candidate_single_job, bundle);
    }

    private void applyFilters() {
        String selectedWorkMode = workModeDropdown.getText().toString();
        String selectedJobType = jobTypeDropdown.getText().toString();

        // Convert "All" to empty string to match React implementation
        selectedWorkMode = "All".equals(selectedWorkMode) ? "" : selectedWorkMode;
        selectedJobType = "All".equals(selectedJobType) ? "" : selectedJobType;

        viewModel.getFilteredJobs(selectedWorkMode, selectedJobType).observe(getViewLifecycleOwner(), this::updateJobsUI);
    }

    private void clearFilters() {
        workModeDropdown.setText("All", false);
        jobTypeDropdown.setText("All", false);
        viewModel.getActiveJobs().observe(getViewLifecycleOwner(), this::updateJobsUI);
    }
}