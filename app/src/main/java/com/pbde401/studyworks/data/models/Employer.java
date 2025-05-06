package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.pbde401.studyworks.data.models.enums.UserRole;
import java.util.Date;
import java.util.Map;

public class Employer extends User {
    @Nullable
    private Map<String, Object> profile;

    public Employer(@NonNull String id, @NonNull Date createdAt, @NonNull Date updatedAt,
                   @NonNull String uid, @NonNull String fullName, @NonNull String email,
                   @Nullable Map<String, Object> profile) {
        super(id, createdAt, updatedAt, uid, fullName, email, UserRole.EMPLOYER);
        this.profile = profile;
    }

    @Nullable
    public Map<String, Object> getProfile() {
        return profile;
    }

    public void setProfile(@Nullable Map<String, Object> profile) {
        this.profile = profile;
    }
}
