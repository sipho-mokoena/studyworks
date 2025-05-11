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
import com.google.android.material.chip.Chip;
import com.pbde401.studyworks.R;

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
        jobDescriptionText = view.findViewById(R.id.jobDescriptionText);
        requirementsContainer = view.findViewById(R.id.requirementsContainer);
        messageCandidateButton = view.findViewById(R.id.messageCandidateButton);
        interviewButton = view.findViewById(R.id.interviewButton);
        rejectButton = view.findViewById(R.id.rejectButton);

        messageCandidateButton.setOnClickListener(v -> {
            // TODO: Navigate to chat
        });
        
        interviewButton.setOnClickListener(v -> {
            // TODO: Handle interview action
        });
        
        rejectButton.setOnClickListener(v -> {
            // TODO: Handle reject action
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EmployerSingleApplicationViewModel.class);
        
        // Observe application data
        viewModel.getApplication(applicationId).observe(getViewLifecycleOwner(), application -> {
            // TODO: Update UI with application data
        });
    }
}