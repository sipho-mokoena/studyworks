package com.pbde401.studyworks.ui.employer.jobs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Application;
import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.data.models.CandidateEducation;
import com.pbde401.studyworks.data.models.CandidateExperience;
import com.pbde401.studyworks.data.models.CandidateProfile;
import com.pbde401.studyworks.data.models.Chat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import androidx.lifecycle.ViewModelProvider;

public class EmployerSingleJobFragment extends Fragment {
    private String applicationId;
    private EmployerSingleJobViewModel viewModel;
    
    private ProgressBar loadingProgress;
    private TextView errorText;
    private View contentContainer;
    
    private TextView jobTitle, companyName, appliedDate, jobDescription, candidateName;
    private Chip applicationStatus;
    private ChipGroup requirementsGroup, skillsGroup;
    private LinearLayout educationContainer, experienceContainer;
    private MaterialButton messageButton, interviewButton, rejectButton;
    private FirebaseFirestore db;

    public static EmployerSingleJobFragment newInstance(String applicationId) {
        EmployerSingleJobFragment fragment = new EmployerSingleJobFragment();
        Bundle args = new Bundle();
        args.putString("applicationId", applicationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EmployerSingleJobViewModel.class);
        if (getArguments() != null) {
            applicationId = getArguments().getString("applicationId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employer_single_job, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        loadingProgress = view.findViewById(R.id.loading_progress);
        errorText = view.findViewById(R.id.error_text);
        contentContainer = view.findViewById(R.id.content_container);
        
        jobTitle = view.findViewById(R.id.job_title);
        companyName = view.findViewById(R.id.company_name);
        appliedDate = view.findViewById(R.id.applied_date);
        jobDescription = view.findViewById(R.id.job_description);
        candidateName = view.findViewById(R.id.candidate_name);
        applicationStatus = view.findViewById(R.id.application_status);
        requirementsGroup = view.findViewById(R.id.requirements_group);
        skillsGroup = view.findViewById(R.id.skills_group);
        educationContainer = view.findViewById(R.id.education_container);
        experienceContainer = view.findViewById(R.id.experience_container);
        
        messageButton = view.findViewById(R.id.message_button);
        interviewButton = view.findViewById(R.id.interview_button);
        rejectButton = view.findViewById(R.id.reject_button);
        
        setupObservers();
        setupClickListeners();
        viewModel.loadApplication(applicationId);
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), this::showLoading);
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) showError(error);
        });
        viewModel.getApplication().observe(getViewLifecycleOwner(), this::updateUI);
        viewModel.getCandidate().observe(getViewLifecycleOwner(), this::updateCandidateUI);
    }

    private void setupClickListeners() {
        messageButton.setOnClickListener(v -> {
            Candidate candidate = viewModel.getCandidate().getValue();
            if (candidate != null) {
                viewModel.createChat(
                    viewModel.getApplication().getValue().getEmployerId(),
                    candidate.getId(),
                    chatId -> {
                        // TODO: Navigate to chat screen with chatId
                    }
                );
            }
        });
        
        interviewButton.setOnClickListener(v -> showStatusConfirmationDialog("INTERVIEW"));
        rejectButton.setOnClickListener(v -> showStatusConfirmationDialog("REJECTED"));
    }

    private void showStatusConfirmationDialog(String newStatus) {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm")
            .setMessage("Are you sure you want to change application status to " + newStatus + "?")
            .setPositiveButton("Yes", (dialog, which) -> 
                viewModel.updateApplicationStatus(applicationId, newStatus))
            .setNegativeButton("No", null)
            .show();
    }

    private void updateUI(Application application) {
        jobTitle.setText(application.getJobTitle());
        companyName.setText(application.getCandidateId());
        appliedDate.setText("Applied: " + new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(application.getAppliedAt()));
        jobDescription.setText(application.getJobTitle());
        applicationStatus.setText(application.getStatus().getValue());
    }

    private void updateCandidateUI(Candidate candidate) {
        candidateName.setText(candidate.getFullName());
        CandidateProfile profile = candidate.getProfile();
        // Update education
        for (CandidateEducation edu : profile.getEducation()) {
            TextView view = new TextView(requireContext());
            view.setText(String.format("%s - %s\n%s", 
                edu.getInstitution(), edu.getDegree(), edu.getEndDate()));
            educationContainer.addView(view);
        }
        
        // Update experience
        for (CandidateExperience exp : profile.getExperience()) {
            TextView view = new TextView(requireContext());
            view.setText(String.format("%s at %s (%s - %s)", 
                exp.getTitle(), exp.getCompany(), exp.getStartDate(), exp.getEndDate()));
            experienceContainer.addView(view);
        }
        
        // Update skills
        for (String skill : profile.getSkills()) {
            Chip chip = new Chip(requireContext());
            chip.setText(skill);
            skillsGroup.addView(chip);
        }
    }
    
    private void showLoading(boolean show) {
        loadingProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        contentContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        errorText.setVisibility(View.GONE);
    }
    
    private void showError(String message) {
        loadingProgress.setVisibility(View.GONE);
        contentContainer.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(message);
    }
}