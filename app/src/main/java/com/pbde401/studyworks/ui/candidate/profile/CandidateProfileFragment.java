package com.pbde401.studyworks.ui.candidate.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.data.models.CandidateEducation;
import com.pbde401.studyworks.data.models.CandidateExperience;
import com.pbde401.studyworks.data.models.CandidateProfile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CandidateProfileFragment extends Fragment {
    private CandidateProfileViewModel viewModel;
    private View loadingIndicator;
    private View errorText;
    private TextInputEditText fullNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneInput;
    private TextInputEditText locationInput;
    private ChipGroup skillsChipGroup;
    private LinearLayout educationContainer;
    private LinearLayout experienceContainer;
    private MaterialButton addSkillButton;
    private MaterialButton addEducationButton;
    private MaterialButton addExperienceButton;
    private MaterialButton saveButton;
    private View profileContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_profile, container, false);
        
        viewModel = new ViewModelProvider(this).get(CandidateProfileViewModel.class);
        
        initializeViews(view);
        setupListeners();
        setupObservers();
        loadProfile();
        
        return view;
    }

    private void initializeViews(View view) {
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        errorText = view.findViewById(R.id.error_text);
        profileContainer = view.findViewById(R.id.profile_container);
        
        fullNameInput = view.findViewById(R.id.full_name);
        emailInput = view.findViewById(R.id.email);
        phoneInput = view.findViewById(R.id.phone_number);
        locationInput = view.findViewById(R.id.location);
        
        skillsChipGroup = view.findViewById(R.id.skills_chip_group);
        educationContainer = view.findViewById(R.id.education_container);
        experienceContainer = view.findViewById(R.id.experience_container);
        
        addSkillButton = view.findViewById(R.id.add_skill_button);
        addEducationButton = view.findViewById(R.id.add_education_button);
        addExperienceButton = view.findViewById(R.id.add_experience_button);
        saveButton = view.findViewById(R.id.save_profile_button);
    }

    private void setupListeners() {
        addSkillButton.setOnClickListener(v -> showAddSkillDialog());
        addEducationButton.setOnClickListener(v -> showAddEducationDialog(null));
        addExperienceButton.setOnClickListener(v -> showAddExperienceDialog(null));
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void setupObservers() {
        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) this.updateUI(profile);
        });
        viewModel.getCandidateData().observe(getViewLifecycleOwner(), candidate -> {
            if (candidate != null) {
                fullNameInput.setText(candidate.getFullName());
                emailInput.setText(candidate.getEmail());
            }
        });
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            profileContainer.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                errorText.setVisibility(View.VISIBLE);
                ((TextView) errorText).setText(error);
            } else {
                errorText.setVisibility(View.GONE);
            }
        });
    }

    private void loadProfile() {
        viewModel.loadCurrentUserProfile();
    }

    private void updateUI(CandidateProfile profile) {
        if (profile == null) return;

        phoneInput.setText(profile.getPhone());
        locationInput.setText(profile.getLocation());
        
        // Update skills
        skillsChipGroup.removeAllViews();
        for (String skill : profile.getSkills()) {
            addSkillChip(skill);
        }
        
        // Update education
        educationContainer.removeAllViews();
        for (CandidateEducation education : profile.getEducation()) {
            addEducationItem(education);
        }
        
        // Update experience
        experienceContainer.removeAllViews();
        for (CandidateExperience experience : profile.getExperience()) {
            addExperienceItem(experience);
        }
    }

    private void addSkillChip(String skill) {
        Chip chip = new Chip(requireContext());
        chip.setText(skill);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> skillsChipGroup.removeView(chip));
        skillsChipGroup.addView(chip);
    }

    private void showAddSkillDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_skill, null);
        EditText skillInput = dialogView.findViewById(R.id.skill_input);

        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Skill")
            .setView(dialogView)
            .setPositiveButton("Add", (dialog, which) -> {
                String skill = skillInput.getText().toString().trim();
                if (!skill.isEmpty()) {
                    addSkillChip(skill);
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void addEducationItem(CandidateEducation education) {
        View educationView = getLayoutInflater().inflate(R.layout.item_education, educationContainer, false);

        TextInputEditText degreeInput = educationView.findViewById(R.id.degree_input);
        TextInputEditText institutionInput = educationView.findViewById(R.id.institution_input);
        TextInputEditText startDateInput = educationView.findViewById(R.id.start_date_input);
        TextInputEditText endDateInput = educationView.findViewById(R.id.end_date_input);
        TextInputEditText descriptionInput = educationView.findViewById(R.id.description_input);
        MaterialButton removeButton = educationView.findViewById(R.id.remove_button);

        if (education != null) {
            degreeInput.setText(education.getDegree());
            institutionInput.setText(education.getInstitution());
            startDateInput.setText(formatDate(education.getStartDate()));
            endDateInput.setText(formatDate(education.getEndDate()));
            descriptionInput.setText(education.getDescription());
        }

        startDateInput.setOnClickListener(v -> showDatePicker(startDateInput));
        endDateInput.setOnClickListener(v -> showDatePicker(endDateInput));
        removeButton.setOnClickListener(v -> educationContainer.removeView(educationView));

        educationContainer.addView(educationView);
    }

    private void showAddEducationDialog(CandidateEducation existing) {
        View dlg = getLayoutInflater().inflate(R.layout.dialog_add_education, null);
        TextInputEditText deg = dlg.findViewById(R.id.degree_input);
        TextInputEditText inst = dlg.findViewById(R.id.institution_input);
        TextInputEditText sd = dlg.findViewById(R.id.start_date_input);
        TextInputEditText ed = dlg.findViewById(R.id.end_date_input);
        TextInputEditText desc = dlg.findViewById(R.id.description_input);
        if (existing != null) {
            deg.setText(existing.getDegree());
            inst.setText(existing.getInstitution());
            sd.setText(formatDate(existing.getStartDate()));
            ed.setText(formatDate(existing.getEndDate()));
            desc.setText(existing.getDescription());
        }
        sd.setOnClickListener(v -> showDatePicker(sd));
        ed.setOnClickListener(v -> showDatePicker(ed));
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add Education")
                .setView(dlg)
                .setPositiveButton("Add", (d, w) -> {
                    CandidateEducation ce = new CandidateEducation(
                            deg.getText().toString(),
                            inst.getText().toString(),
                            parseDate(sd.getText().toString()),
                            parseDate(ed.getText().toString()),
                            desc.getText().toString()
                    );
                    addEducationItem(ce);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addExperienceItem(CandidateExperience experience) {
        View experienceView = getLayoutInflater().inflate(R.layout.item_experience, experienceContainer, false);

        TextInputEditText titleInput = experienceView.findViewById(R.id.title_input);
        TextInputEditText companyInput = experienceView.findViewById(R.id.company_input);
        TextInputEditText startDateInput = experienceView.findViewById(R.id.start_date_input);
        TextInputEditText endDateInput = experienceView.findViewById(R.id.end_date_input);
        TextInputEditText descriptionInput = experienceView.findViewById(R.id.description_input);
        MaterialButton removeButton = experienceView.findViewById(R.id.remove_button);

        if (experience != null) {
            titleInput.setText(experience.getTitle());
            companyInput.setText(experience.getCompany());
            startDateInput.setText(formatDate(experience.getStartDate()));
            endDateInput.setText(formatDate(experience.getEndDate()));
            descriptionInput.setText(experience.getDescription());
        }

        startDateInput.setOnClickListener(v -> showDatePicker(startDateInput));
        endDateInput.setOnClickListener(v -> showDatePicker(endDateInput));
        removeButton.setOnClickListener(v -> experienceContainer.removeView(experienceView));

        experienceContainer.addView(experienceView);
    }

    private void showAddExperienceDialog(CandidateExperience existing) {
        View dlg = getLayoutInflater().inflate(R.layout.dialog_add_experience, null);
        TextInputEditText title = dlg.findViewById(R.id.title_input);
        TextInputEditText comp = dlg.findViewById(R.id.company_input);
        TextInputEditText sd = dlg.findViewById(R.id.start_date_input);
        TextInputEditText ed = dlg.findViewById(R.id.end_date_input);
        TextInputEditText desc = dlg.findViewById(R.id.description_input);
        if (existing != null) {
            title.setText(existing.getTitle());
            comp.setText(existing.getCompany());
            sd.setText(formatDate(existing.getStartDate()));
            ed.setText(formatDate(existing.getEndDate()));
            desc.setText(existing.getDescription());
        }
        sd.setOnClickListener(v -> showDatePicker(sd));
        ed.setOnClickListener(v -> showDatePicker(ed));
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add Experience")
                .setView(dlg)
                .setPositiveButton("Add", (d, w) -> {
                    CandidateExperience ce = new CandidateExperience(
                            title.getText().toString(),
                            comp.getText().toString(),
                            parseDate(sd.getText().toString()),
                            parseDate(ed.getText().toString()),
                            desc.getText().toString()
                    );
                    addExperienceItem(ce);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDatePicker(TextInputEditText dateInput) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(),
            (view, year, month, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                dateInput.setText(formatDate(selectedDate.getTime()));
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(date);
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return new SimpleDateFormat("MMM yyyy", Locale.getDefault()).parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    private void saveProfile() {
        // Collect skills
        List<String> skills = new ArrayList<>();
        for (int i = 0; i < skillsChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) skillsChipGroup.getChildAt(i);
            skills.add(chip.getText().toString());
        }

        // Collect education
        List<CandidateEducation> educationList = new ArrayList<>();
        for (int i = 0; i < educationContainer.getChildCount(); i++) {
            View educationView = educationContainer.getChildAt(i);
            TextInputEditText degreeInput = educationView.findViewById(R.id.degree_input);
            TextInputEditText institutionInput = educationView.findViewById(R.id.institution_input);
            TextInputEditText startDateInput = educationView.findViewById(R.id.start_date_input);
            TextInputEditText endDateInput = educationView.findViewById(R.id.end_date_input);
            TextInputEditText descriptionInput = educationView.findViewById(R.id.description_input);

            CandidateEducation education = new CandidateEducation(
                degreeInput.getText().toString(),
                institutionInput.getText().toString(),
                parseDate(startDateInput.getText().toString()),
                parseDate(endDateInput.getText().toString()),
                descriptionInput.getText().toString()
            );
            educationList.add(education);
        }

        // Collect experience
        List<CandidateExperience> experienceList = new ArrayList<>();
        for (int i = 0; i < experienceContainer.getChildCount(); i++) {
            View experienceView = experienceContainer.getChildAt(i);
            TextInputEditText titleInput = experienceView.findViewById(R.id.title_input);
            TextInputEditText companyInput = experienceView.findViewById(R.id.company_input);
            TextInputEditText startDateInput = experienceView.findViewById(R.id.start_date_input);
            TextInputEditText endDateInput = experienceView.findViewById(R.id.end_date_input);
            TextInputEditText descriptionInput = experienceView.findViewById(R.id.description_input);

            CandidateExperience experience = new CandidateExperience(
                titleInput.getText().toString(),
                companyInput.getText().toString(),
                parseDate(startDateInput.getText().toString()),
                parseDate(endDateInput.getText().toString()),
                descriptionInput.getText().toString()
            );
            experienceList.add(experience);
        }

        // Create updated profile
        CandidateProfile updatedProfile = new CandidateProfile(
            phoneInput.getText().toString(),
            locationInput.getText().toString(),
            educationList,
            experienceList,
            skills
        );

        viewModel.saveProfile(updatedProfile);
    }
}