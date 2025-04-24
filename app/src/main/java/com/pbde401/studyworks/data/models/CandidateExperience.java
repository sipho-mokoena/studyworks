package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import java.util.Date;

public class CandidateExperience {
    @NonNull
    private String title;
    @NonNull
    private String company;
    @NonNull
    private Date startDate;
    @NonNull
    private Date endDate;
    @NonNull
    private String description;

    public CandidateExperience(@NonNull String title, @NonNull String company,
                               @NonNull Date startDate, @NonNull Date endDate,
                               @NonNull String description) {
        this.title = title;
        this.company = company;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getCompany() {
        return company;
    }

    public void setCompany(@NonNull String company) {
        this.company = company;
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
