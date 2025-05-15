package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.Timestamp;
import java.util.Date;

public class Chat extends BaseModel {
    @Nullable
    private String jobId;
    @NonNull
    private String candidateId;
    @NonNull
    private String employerId;
    @Nullable
    private String lastMessage;
    @Nullable
    private Timestamp lastMessageAt;

    // Add default constructor
    public Chat() {
        super();
    }

    public Chat(@NonNull String id, @NonNull Timestamp createdAt, @NonNull Timestamp updatedAt,
               @NonNull String candidateId, @NonNull String employerId) {
        super(id, createdAt != null ? createdAt.toDate() : null, 
                  updatedAt != null ? updatedAt.toDate() : null);
        this.candidateId = candidateId;
        this.employerId = employerId;
    }

    public Chat(@NonNull String id, @NonNull Timestamp createdAt, @NonNull Timestamp updatedAt,
               @Nullable String jobId, @NonNull String candidateId, @NonNull String employerId,
               @Nullable String lastMessage, @Nullable Timestamp lastMessageAt) {
        super(id, createdAt != null ? createdAt.toDate() : null, 
                  updatedAt != null ? updatedAt.toDate() : null);
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.employerId = employerId;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
    }

    @Nullable
    public String getJobId() {
        return jobId;
    }

    public void setJobId(@Nullable String jobId) {
        this.jobId = jobId;
    }

    @NonNull
    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(@NonNull String candidateId) {
        this.candidateId = candidateId;
    }

    @NonNull
    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(@NonNull String employerId) {
        this.employerId = employerId;
    }

    @Nullable
    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(@Nullable String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Nullable
    public Timestamp getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(@Nullable Timestamp lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}
