package com.pbde401.studyworks.models;

import androidx.annotation.NonNull;
import com.pbde401.studyworks.models.enums.JobType;
import com.pbde401.studyworks.models.enums.WorkMode;
import java.util.List;

public class JobFilters {
    @NonNull
    private List<JobType> type;
    @NonNull
    private List<WorkMode> workMode;

    public JobFilters(@NonNull List<JobType> type, @NonNull List<WorkMode> workMode) {
        this.type = type;
        this.workMode = workMode;
    }

    @NonNull
    public List<JobType> getType() {
        return type;
    }

    public void setType(@NonNull List<JobType> type) {
        this.type = type;
    }

    @NonNull
    public List<WorkMode> getWorkMode() {
        return workMode;
    }

    public void setWorkMode(@NonNull List<WorkMode> workMode) {
        this.workMode = workMode;
    }
}
