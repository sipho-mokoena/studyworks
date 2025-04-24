package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import com.pbde401.studyworks.data.models.enums.UserRole;
import java.util.Date;

public class User extends BaseModel {
    @NonNull
    private String uid;
    @NonNull
    private String fullName;
    @NonNull
    private String email;
    @NonNull
    private UserRole role;

    public User(@NonNull String id, @NonNull Date createdAt, @NonNull Date updatedAt,
               @NonNull String uid, @NonNull String fullName, @NonNull String email,
               @NonNull UserRole role) {
        super(id, createdAt, updatedAt);
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public UserRole getRole() {
        return role;
    }

    public void setRole(@NonNull UserRole role) {
        this.role = role;
    }
}
