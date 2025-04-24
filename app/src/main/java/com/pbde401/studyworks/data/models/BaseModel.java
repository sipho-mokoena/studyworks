package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import java.util.Date;

public class BaseModel {
    @NonNull
    private String id;
    @NonNull
    private Date createdAt;
    @NonNull
    private Date updatedAt;

    public BaseModel(@NonNull String id, @NonNull Date createdAt, @NonNull Date updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@NonNull Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
