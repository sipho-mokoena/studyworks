package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import java.util.Date;

public class CandidateEducation {
    @NonNull
    private String degree;
    @NonNull
    private String institution;
    @NonNull
    private Date startDate;
    @NonNull
    private Date endDate;
    @NonNull
    private String description;

    public CandidateEducation(@NonNull String degree, @NonNull String institution,
                              @NonNull Date startDate, @NonNull Date endDate,
                              @NonNull String description) {
        this.degree = degree;
        this.institution = institution;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @NonNull
    public String getDegree() {
        return degree;
    }

    public void setDegree(@NonNull String degree) {
        this.degree = degree;
    }

    @NonNull
    public String getInstitution() {
        return institution;
    }

    public void setInstitution(@NonNull String institution) {
        this.institution = institution;
    }

    @NonNull
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    @NonNull
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(@NonNull Date endDate) {
        this.endDate = endDate;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }
}
