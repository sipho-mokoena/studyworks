package com.pbde401.studyworks.data.models;

import androidx.annotation.NonNull;
import com.pbde401.studyworks.data.models.enums.UserRole;
import java.util.Date;

public class Candidate extends User {
    @NonNull
    private CandidateProfile candidateProfile;

    public Candidate(@NonNull String id, @NonNull Date createdAt, @NonNull Date updatedAt,
                    @NonNull String uid, @NonNull String fullName, @NonNull String email,
                    @NonNull CandidateProfile candidateProfile) {
        super(id, createdAt, updatedAt, uid, fullName, email, UserRole.CANDIDATE);
        this.candidateProfile = candidateProfile;
    }

    @NonNull
    public CandidateProfile getProfile() {
        return candidateProfile;
    }

    public void setProfile(@NonNull CandidateProfile candidateProfile) {
        this.candidateProfile = candidateProfile;
    }
}
