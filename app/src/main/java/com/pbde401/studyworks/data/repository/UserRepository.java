package com.pbde401.studyworks.data.repository;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pbde401.studyworks.data.models.User;
import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.data.models.Employer;
import com.pbde401.studyworks.data.models.enums.UserRole;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.QuerySnapshot;
import android.util.Log;

public class UserRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String USERS_COLLECTION = "users";
    
    public Task<User> getUserByEmail(String email) {
        return db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        return documentToUser(document);
                    }
                    return null;
                });
    }
    
    public Task<User> getUserById(String id) {
        return db.collection(USERS_COLLECTION)
                .document(id)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        return documentToUser(task.getResult());
                    }
                    return null;
                });
    }
    
    public Task<Void> createUser(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("uid", user.getUid());
        userData.put("fullName", user.getFullName());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole().toString());
        userData.put("createdAt", user.getCreatedAt());
        userData.put("updatedAt", user.getUpdatedAt());
        
        return db.collection(USERS_COLLECTION)
                .document(user.getId())
                .set(userData);
    }
    
    @NonNull
    private User documentToUser(DocumentSnapshot document) {
        String id = document.getString("id");
        String uid = document.getString("uid");
        String fullName = document.getString("fullName");
        String email = document.getString("email");
        String roleString = document.getString("role");
        UserRole role = roleString != null ? UserRole.fromString(roleString) : UserRole.GUEST;

        String createdAtString = document.getString("createdAt");
        String updatedAtString = document.getString("updatedAt");
        Date createdAt = parseDate(createdAtString);
        Date updatedAt = parseDate(updatedAtString);

        if (role == UserRole.CANDIDATE) {
            return new Candidate(id, createdAt, updatedAt, uid, fullName, email, null);
        } else {
            return new Employer(id, createdAt, updatedAt, uid, fullName, email, null);
        }
    }

    private Date parseDate(String dateString) {
        if (dateString == null) return null;
        try {
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601Format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Task<Void> updateUserProfile(String userId, Map<String, Object> updates) {
        return db.collection(USERS_COLLECTION)
                .document(userId)
                .update(updates);
    }

    public Task<List<Candidate>> getCandidates() {
        return db.collection(USERS_COLLECTION)
                .whereEqualTo("role", UserRole.CANDIDATE.getRoleString())
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<Candidate> candidates = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            User user = documentToUser(document);
                            if (user instanceof Candidate) {
                                candidates.add((Candidate) user);
                            }
                        }
                        return candidates;
                    }
                    return new ArrayList<>();
                });
    }

    public Task<List<Employer>> getEmployers() {
        return db.collection(USERS_COLLECTION)
                .whereEqualTo("role", UserRole.EMPLOYER.getRoleString())
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<Employer> employers = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            User user = documentToUser(document);
                            if (user instanceof Employer) {
                                employers.add((Employer) user);
                            }
                        }
                        return employers;
                    }
                    return new ArrayList<>();
                });
    }

    public Task<Candidate> getCandidateById(String candidateId) {
        return db.collection(USERS_COLLECTION)
                .document(candidateId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        User user = documentToUser(task.getResult());
                        if (user instanceof Candidate) {
                            return (Candidate) user;
                        }
                    }
                    return null;
                });
    }

    public Task<List<Candidate>> getManyCandidatesByIds(List<String> candidateIds) {
        return db.collection(USERS_COLLECTION)
                .whereEqualTo("role", UserRole.CANDIDATE.getRoleString())
                .whereIn("id", candidateIds)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<Candidate> candidates = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            User user = documentToUser(document);
                            if (user instanceof Candidate) {
                                candidates.add((Candidate) user);
                            }
                        }
                        return candidates;
                    }
                    return new ArrayList<>();
                });
    }

    public Task<List<Employer>> getManyEmployersByIds(List<String> employerIds) {
        return db.collection(USERS_COLLECTION)
                .whereEqualTo("role", UserRole.EMPLOYER.getRoleString())
                .whereIn("id", employerIds)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<Employer> employers = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            User user = documentToUser(document);
                            if (user instanceof Employer) {
                                employers.add((Employer) user);
                            }
                        }
                        return employers;
                    }
                    return new ArrayList<>();
                });
    }

    public Task<List<Candidate>> searchCandidatesByFullname(String fullName) {
        return db.collection(USERS_COLLECTION)
                .whereEqualTo("role", UserRole.CANDIDATE.getRoleString())
                .whereEqualTo("fullName", fullName)
                .limit(10)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<Candidate> candidates = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            User user = documentToUser(document);
                            if (user instanceof Candidate) {
                                candidates.add((Candidate) user);
                            }
                        }
                        return candidates;
                    }
                    return new ArrayList<>();
                });
    }

    public Task<Candidate> setCandidateProfile(Candidate candidate) {
        if (candidate.getRole() != UserRole.CANDIDATE) {
            throw new IllegalArgumentException("User is not a candidate");
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("profile", candidate.getProfile());
        updates.put("updatedAt", new Date());

        return db.collection(USERS_COLLECTION)
                .document(candidate.getId())
                .update(updates)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        candidate.setUpdatedAt(new Date());
                        return candidate;
                    }
                    throw task.getException();
                });
    }

    public Task<Employer> setEmployerProfile(Employer employer) {
        if (employer.getRole() != UserRole.EMPLOYER) {
            throw new IllegalArgumentException("User is not an employer");
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("profile", employer.getProfile());
        updates.put("updatedAt", new Date());

        return db.collection(USERS_COLLECTION)
                .document(employer.getId())
                .update(updates)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        employer.setUpdatedAt(new Date());
                        return employer;
                    }
                    throw task.getException();
                });
    }
}
