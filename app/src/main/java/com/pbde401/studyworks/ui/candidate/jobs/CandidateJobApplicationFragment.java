package com.pbde401.studyworks.ui.candidate.jobs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.pbde401.studyworks.R;

public class CandidateJobApplicationFragment extends Fragment {
    private CandidateJobApplicationViewModel viewModel;
    private String jobId;
    
    // UI components
    private TextView errorText;
    private TextView jobTitle;
    private TextView companyName;
    private TextView location;
    private TextView jobType;
    private TextView salary;
    private Button messageRecruiterButton;
    private TextInputEditText coverLetterInput;
    private TextInputEditText portfolioUrlInput;
    private TextInputEditText linkedinUrlInput;
    private Button cancelButton;
    private Button submitButton;
    private ProgressBar loadingProgress;

    public static CandidateJobApplicationFragment newInstance() {
        return new CandidateJobApplicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidate_job_application, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        jobId = getArguments() != null ? getArguments().getString("jobId") : null;
        
        // Initialize UI components
        errorText = view.findViewById(R.id.errorText);
        jobTitle = view.findViewById(R.id.jobTitle);
        companyName = view.findViewById(R.id.companyName);
        location = view.findViewById(R.id.location);
        jobType = view.findViewById(R.id.jobType);
        salary = view.findViewById(R.id.salary);
        messageRecruiterButton = view.findViewById(R.id.messageRecruiterButton);
        coverLetterInput = view.findViewById(R.id.coverLetterInput);
        portfolioUrlInput = view.findViewById(R.id.portfolioUrlInput);
        linkedinUrlInput = view.findViewById(R.id.linkedinUrlInput);
        cancelButton = view.findViewById(R.id.cancelButton);
        submitButton = view.findViewById(R.id.submitButton);
        loadingProgress = view.findViewById(R.id.loadingProgress);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CandidateJobApplicationViewModel.class);
        viewModel.init(jobId);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        viewModel.getJob().observe(getViewLifecycleOwner(), job -> {
            if (job != null) {
                jobTitle.setText(job.title);
                companyName.setText(job.company);
                location.setText(job.location);
                jobType.setText(job.type);
                salary.setText(job.salary);
            }
        });

        viewModel.getApplication().observe(getViewLifecycleOwner(), application -> {
            if (application != null) {
                // Application exists, disable form
                coverLetterInput.setText(application.coverLetter);
                portfolioUrlInput.setText(application.portfolioUrl);
                linkedinUrlInput.setText(application.linkedinUrl);
                
                coverLetterInput.setEnabled(false);
                portfolioUrlInput.setEnabled(false);
                linkedinUrlInput.setEnabled(false);
                submitButton.setEnabled(false);
            }
        });

        viewModel.getChat().observe(getViewLifecycleOwner(), chat -> {
            messageRecruiterButton.setVisibility(chat != null ? View.VISIBLE : View.GONE);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            submitButton.setEnabled(!isLoading);
            cancelButton.setEnabled(!isLoading);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            errorText.setVisibility(error != null ? View.VISIBLE : View.GONE);
            errorText.setText(error);
        });
    }

    private void setupListeners() {
        messageRecruiterButton.setOnClickListener(v -> {
            // TODO: Navigate to chat
        });

        cancelButton.setOnClickListener(v -> {
            // TODO: Navigate back to job details
            requireActivity().onBackPressed();
        });

        submitButton.setOnClickListener(v -> {
            String coverLetter = coverLetterInput.getText().toString();
            String portfolioUrl = portfolioUrlInput.getText().toString();
            String linkedinUrl = linkedinUrlInput.getText().toString();

            if (TextUtils.isEmpty(coverLetter)) {
                coverLetterInput.setError("Cover letter is required");
                return;
            }

            viewModel.submitApplication(coverLetter, portfolioUrl, linkedinUrl);
        });
    }
}