package com.pbde401.studyworks.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.pbde401.studyworks.models.enums.UserRole;
import java.util.Date;

public class Employer extends User {
    @Nullable
    private EmployerProfile profile;

    public Employer(@NonNull String id, @NonNull Date createdAt, @NonNull Date updatedAt,
                   @NonNull String uid, @NonNull String fullName, @NonNull String email,
                   @Nullable EmployerProfile profile) {
        super(id, createdAt, updatedAt, uid, fullName, email, UserRole.EMPLOYER);
        this.profile = profile;
    }

    @Nullable
    public EmployerProfile getProfile() {
        return profile;
    }

    public void setProfile(@Nullable EmployerProfile profile) {
        this.profile = profile;
    }
}
