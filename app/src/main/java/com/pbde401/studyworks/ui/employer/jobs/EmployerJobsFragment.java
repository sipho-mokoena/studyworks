package com.pbde401.studyworks.ui.employer.jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Job;
import com.pbde401.studyworks.ui.candidate.jobs.JobListingAdapter;

public class EmployerJobsFragment extends Fragment implements JobListingAdapter.OnJobClickListener {
    private EmployerJobsViewModel viewModel;
    private JobListingAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private AutoCompleteTextView workModeDropdown;
    private AutoCompleteTextView jobTypeDropdown;
    private Button btnApplyFilters;
    private Button btnClearFilters;
    private Button btnCreateJob;

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
        
        initializeViews(view);
        setupDropdowns();
        setupClickListeners();
        observeViewModel();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rvEmployerJobs);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);
        workModeDropdown = view.findViewById(R.id.workModeDropdown);
        jobTypeDropdown = view.findViewById(R.id.jobTypeDropdown);
        btnApplyFilters = view.findViewById(R.id.btnApplyFilters);
        btnClearFilters = view.findViewById(R.id.btnClearFilters);
        btnCreateJob = view.findViewById(R.id.btnCreateJob);
        
        adapter = new JobListingAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupDropdowns() {
        String[] workModes = {"All", "Remote", "On-site", "Hybrid"};
        String[] jobTypes = {"All", "Full-time", "Part-time", "Contract"};

        ArrayAdapter<String> workModeAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_dropdown_item_1line, workModes);
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_dropdown_item_1line, jobTypes);

        workModeDropdown.setAdapter(workModeAdapter);
        jobTypeDropdown.setAdapter(jobTypeAdapter);
    }

    private void setupClickListeners() {
        btnApplyFilters.setOnClickListener(v -> applyFilters());
        btnClearFilters.setOnClickListener(v -> clearFilters());
        btnCreateJob.setOnClickListener(v -> navigateToCreateJob());
    }

    private void observeViewModel() {
        String employerId = "current_employer_id"; // TODO: Get from auth
        viewModel = new ViewModelProvider(this).get(EmployerJobsViewModel.class);
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> 
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE));
            
        viewModel.getEmployerJobs(employerId).observe(getViewLifecycleOwner(), 
            jobs -> adapter.setJobs(jobs));
    }

    private void applyFilters() {
        String workMode = workModeDropdown.getText().toString();
        String jobType = jobTypeDropdown.getText().toString();
        
        if (workMode.equals("All")) workMode = "";
        if (jobType.equals("All")) jobType = "";
        
        viewModel.getFilteredEmployerJobs(workMode, jobType).observe(getViewLifecycleOwner(),
            jobs -> adapter.setJobs(jobs));
    }

    private void clearFilters() {
        workModeDropdown.setText("All", false);
        jobTypeDropdown.setText("All", false);
        String employerId = "current_employer_id"; // TODO: Get from auth
        viewModel.getEmployerJobs(employerId);
    }

    private void navigateToCreateJob() {
        // TODO: Implement navigation to create job screen
    }

    @Override
    public void onJobClick(Job job) {
        // TODO: Navigate to job details
    }
}