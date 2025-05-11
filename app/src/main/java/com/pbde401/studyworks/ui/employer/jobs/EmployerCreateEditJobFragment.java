package com.pbde401.studyworks.ui.employer.jobs;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.pbde401.studyworks.R;

public class EmployerCreateEditJobFragment extends Fragment {

    private EmployerCreateEditJobViewModel viewModel;

    public static EmployerCreateEditJobFragment newInstance() {
        return new EmployerCreateEditJobFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employer_create_edit_job, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup job type dropdown
        AutoCompleteTextView jobTypeInput = view.findViewById(R.id.jobTypeInput);
        String[] jobTypes = new String[]{"Full-time", "Part-time", "Contract"};
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(
                requireContext(), 
                android.R.layout.simple_dropdown_item_1line, 
                jobTypes
        );
        jobTypeInput.setAdapter(jobTypeAdapter);

        // Setup work mode dropdown
        AutoCompleteTextView workModeInput = view.findViewById(R.id.workModeInput);
        String[] workModes = new String[]{"Remote", "On-site", "Hybrid"};
        ArrayAdapter<String> workModeAdapter = new ArrayAdapter<>(
                requireContext(), 
                android.R.layout.simple_dropdown_item_1line, 
                workModes
        );
        workModeInput.setAdapter(workModeAdapter);

        // Setup buttons
        MaterialButton cancelButton = view.findViewById(R.id.cancelButton);
        MaterialButton saveButton = view.findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(v -> {
            // TODO: Navigate back
        });

        saveButton.setOnClickListener(v -> {
            // TODO: Handle form submission
        });
    }

}