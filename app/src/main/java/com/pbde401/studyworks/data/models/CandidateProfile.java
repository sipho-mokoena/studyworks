package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import java.util.List;

public class CandidateProfile {
    @NonNull
    private String phone;
    @NonNull
    private String location;
    @NonNull
    private List<CandidateEducation> candidateEducation;
    @NonNull
    private List<CandidateExperience> candidateExperience;
    @NonNull
    private List<String> skills;

    public CandidateProfile(@NonNull String phone, @NonNull String location,
                            @NonNull List<CandidateEducation> candidateEducation, @NonNull List<CandidateExperience> candidateExperience,
                            @NonNull List<String> skills) {
        this.phone = phone;
        this.location = location;
        this.candidateEducation = candidateEducation;
        this.candidateExperience = candidateExperience;
        this.skills = skills;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    public void setLocation(@NonNull String location) {
        this.location = location;
    }

    @NonNull
    public List<CandidateEducation> getEducation() {
        return candidateEducation;
    }

    public void setEducation(@NonNull List<CandidateEducation> candidateEducation) {
        this.candidateEducation = candidateEducation;
    }

    @NonNull
    public List<CandidateExperience> getExperience() {
        return candidateExperience;
    }

    public void setExperience(@NonNull List<CandidateExperience> candidateExperience) {
        this.candidateExperience = candidateExperience;
    }

    @NonNull
    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(@NonNull List<String> skills) {
        this.skills = skills;
    }
}
