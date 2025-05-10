package com.pbde401.studyworks.ui.employer.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Patterns;

import com.pbde401.studyworks.data.models.Employer;
import com.pbde401.studyworks.data.models.EmployerProfile;
import com.pbde401.studyworks.R;

public class EmployerProfileFragment extends Fragment {
    private EmployerProfileViewModel viewModel;
    private Employer employer;
    private View loadingIndicator;
    private TextView errorAlert;
    private EditText fullNameEditText, emailEditText, companyNameEditText, companyDescriptionEditText, websiteEditText;
    private ImageButton editButton, saveButton, closeButton;
    private boolean isEditing = false;

    public EmployerProfileFragment() {
        // Required empty public constructor
    }
    public static EmployerProfileFragment newInstance() {
        EmployerProfileFragment fragment = new EmployerProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_employer_profile, container, false);

        viewModel = new ViewModelProvider(this).get(EmployerProfileViewModel.class);

        // Bind views from layout
        loadingIndicator = rootView.findViewById(R.id.loading_indicator);
        errorAlert = rootView.findViewById(R.id.error_alert);
        fullNameEditText = rootView.findViewById(R.id.full_name);
        emailEditText = rootView.findViewById(R.id.email);
        companyNameEditText = rootView.findViewById(R.id.company_name);
        companyDescriptionEditText = rootView.findViewById(R.id.company_description);
        websiteEditText = rootView.findViewById(R.id.website);
        editButton = rootView.findViewById(R.id.edit_button);
        saveButton = rootView.findViewById(R.id.save_button);
        closeButton = rootView.findViewById(R.id.close_button);

        // Load current employer (assumed via a helper method)
        employer = viewModel.getCurrentEmployer();
        updateUIFromEmployer();

        // Set editing controls
        editButton.setOnClickListener(v -> setEditing(true));
        saveButton.setOnClickListener(v -> updateProfile());
        closeButton.setOnClickListener(v -> {
            setEditing(false);
            updateUIFromEmployer(); // revert unsaved changes
        });

        return rootView;
    }

    private void setEditing(boolean editing) {
        isEditing = editing;
        fullNameEditText.setEnabled(editing);
        emailEditText.setEnabled(editing);
        companyNameEditText.setEnabled(editing);
        companyDescriptionEditText.setEnabled(editing);
        websiteEditText.setEnabled(editing);
        editButton.setVisibility(editing ? View.GONE : View.VISIBLE);
        saveButton.setVisibility(editing ? View.VISIBLE : View.GONE);
        closeButton.setVisibility(editing ? View.VISIBLE : View.GONE);
    }

    private void updateUIFromEmployer() {
        if (employer != null) {
            fullNameEditText.setText(employer.getFullName());
            emailEditText.setText(employer.getEmail());
            if (employer.getProfile() != null) {
                companyNameEditText.setText(employer.getProfile().getCompanyName());
                companyDescriptionEditText.setText(employer.getProfile().getCompanyDescription());
                websiteEditText.setText(employer.getProfile().getWebsite());
            }
        }
        errorAlert.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        setEditing(false);
    }

    private void updateProfile() {
        // Validate fields
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String companyName = companyNameEditText.getText().toString().trim();
        String companyDescription = companyDescriptionEditText.getText().toString().trim();
        String website = websiteEditText.getText().toString().trim();

        if (fullName.isEmpty()) {
            showError("Full name is required");
            return;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Valid email is required");
            return;
        }
        if (companyName.isEmpty()) {
            showError("Company name is required");
            return;
        }

        loadingIndicator.setVisibility(View.VISIBLE);
        errorAlert.setVisibility(View.GONE);

        // Update employer fields with new values
        employer.setFullName(fullName);
        employer.setEmail(email);
        if (employer.getProfile() == null) {
            // Assuming EmployerProfile has a constructor with (String companyName, String companyDescription, String website)
            employer.setProfile(new EmployerProfile(companyName, companyDescription, website));
        } else {
            employer.getProfile().setCompanyName(companyName);
            employer.getProfile().setCompanyDescription(companyDescription);
            employer.getProfile().setWebsite(website);
        }

        // Simulate asynchronous update (e.g., network/database call)
        new Thread(() -> {
            try {
                Thread.sleep(1500);  // Simulate update delay
            } catch (InterruptedException e) {
                // Handle interruption if needed
            }
            requireActivity().runOnUiThread(() -> {
                loadingIndicator.setVisibility(View.GONE);
                setEditing(false);
                updateUIFromEmployer();
            });
        }).start();
    }

    private void showError(String message) {
        errorAlert.setText(message);
        errorAlert.setVisibility(View.VISIBLE);
    }
}