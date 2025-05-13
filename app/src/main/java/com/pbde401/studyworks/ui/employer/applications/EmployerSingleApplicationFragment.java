package com.pbde401.studyworks.ui.employer.applications;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.data.models.enums.ApplicationStatus;
import android.content.Intent;
import com.pbde401.studyworks.ui.employer.chats.EmployerChatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmployerSingleApplicationFragment extends Fragment {

    private EmployerSingleApplicationViewModel viewModel;

    private String applicationId;
    private TextView jobTitleText;
    private Chip statusChip;
    private TextView companyNameText;
    private TextView applicationDateText;
    private TextView jobDescriptionText;
    private ViewGroup requirementsContainer;
    private Button messageCandidateButton;
    private Button interviewButton;
    private Button rejectButton;
    private TextView candidateNameText, candidateEmailText, coverLetter;
    private Button resumeButton;
    private Button portfolioButton;
    private Button linkedinButton;

    public static EmployerSingleApplicationFragment newInstance() {
        return new EmployerSingleApplicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer_single_application, container, false);
        
        // Initialize views
        jobTitleText = view.findViewById(R.id.jobTitleText);
        statusChip = view.findViewById(R.id.statusChip);
        companyNameText = view.findViewById(R.id.companyNameText);
        applicationDateText = view.findViewById(R.id.applicationDateText);
        coverLetter = view.findViewById(R.id.coverLetter);
        jobDescriptionText = view.findViewById(R.id.jobDescriptionText);
        requirementsContainer = view.findViewById(R.id.requirementsContainer);
        messageCandidateButton = view.findViewById(R.id.messageCandidateButton);
        interviewButton = view.findViewById(R.id.interviewButton);
        rejectButton = view.findViewById(R.id.rejectButton);

        // Initialize candidate info views
        candidateNameText = view.findViewById(R.id.candidateNameText);
        candidateEmailText = view.findViewById(R.id.candidateEmailText);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EmployerSingleApplicationViewModel.class);
        
        // Get applicationId from arguments
        applicationId = getArguments() != null ? getArguments().getString("applicationId") : null;
        if (applicationId != null) {
            viewModel.getApplication(applicationId).observe(getViewLifecycleOwner(), application -> {
                if (application != null) {
                    viewModel.setApplication(application);

                    viewModel.getCandidateUser(application.getCandidateId()).observe(
                        getViewLifecycleOwner(), user -> {
                            Candidate candidate = (Candidate) user;
                            if (candidate != null) {
                                viewModel.setCandidate(candidate);
                                candidateNameText.setText(candidate.getFullName());
                                candidateEmailText.setText(candidate.getEmail());
                            }
                        }
                    );

                    viewModel.getJob(application.getJobId()).observe(getViewLifecycleOwner(), job -> {
                        if (job != null) {
                            viewModel.setJob(job);
                            jobTitleText.setText(job.getTitle());
                            companyNameText.setText(job.getCompanyName());
                            jobDescriptionText.setText(job.getDescription());

                            // Populate requirements
                            for (String requirement : job.getRequirements()) {
                                TextView requirementText = new TextView(getContext());
                                requirementText.setText(String.format("• %s", requirement));
                                requirementsContainer.addView(requirementText);
                            }
                        }
                    });
                    
                    statusChip.setText(application.getStatus().toString());
                    coverLetter.setText(application.getCoverLetter());
                    applicationDateText.setText(formatDate(application.getAppliedAt()));
                    
                    // Disable buttons based on status
                    boolean isActionable = application.getStatus() == ApplicationStatus.SUBMITTED;
                    interviewButton.setEnabled(isActionable);
                    rejectButton.setEnabled(isActionable);
                }
            });
        }

        messageCandidateButton.setOnClickListener(v -> {
            if (viewModel.application != null) {
                String employerId = viewModel.application.getEmployerId();
                String candidateId = viewModel.application.getCandidateId();
                
                viewModel.findOrCreateChat(employerId, candidateId).observe(getViewLifecycleOwner(), chat -> {
                    if (chat != null) {
                        Intent intent = new Intent(requireContext(), EmployerChatActivity.class);
                        intent.putExtra("chatId", chat.getId());
                        startActivity(intent);
                    }
                });
            }
        });

        interviewButton.setOnClickListener(v -> {
            viewModel.scheduleInterview(applicationId);
            showConfirmationMessage("Interview request sent");
        });
        
        rejectButton.setOnClickListener(v -> {
            viewModel.rejectApplication(applicationId);
            showConfirmationMessage("Application rejected");
        });
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private void showConfirmationMessage(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}