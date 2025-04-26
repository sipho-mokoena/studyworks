package com.pbde401.studyworks.ui.auth.register;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pbde401.studyworks.data.repository.UserRepository;
import com.pbde401.studyworks.data.models.enums.UserRole;
import com.pbde401.studyworks.data.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthRegisterViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final UserRepository userRepository = new UserRepository();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<Boolean> authState = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private UserRole userRole = UserRole.GUEST;

    public LiveData<Boolean> getAuthState() {
        return authState;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void register(String email, String password, UserRole role) {
        isLoading.setValue(true);
        error.setValue(null);
        setUserRole(role);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        createUserInFirestore(firebaseUser, role);
                    } else {
                        isLoading.setValue(false);
                        error.setValue("Registration failed. Please try again.");
                    }
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    error.setValue("Registration failed: " + e.getMessage());
                });
    }

    private void createUserInFirestore(FirebaseUser firebaseUser, UserRole role) {
        String userId = UUID.randomUUID().toString(); // Generate UUID v4
        String firebaseUid = firebaseUser.getUid();
        String email = firebaseUser.getEmail();

        if (email == null) {
            isLoading.setValue(false);
            error.setValue("Email is null. Cannot create user profile.");
            return;
        }

        Date now = new Date();
        User newUser = new User(userId, now, now, firebaseUid, "", email, role);

        // Store using UUID as document ID instead of Firebase UID
        db.collection("users").document(userId)
                .set(newUser)
                .addOnSuccessListener(aVoid -> {
                    isLoading.setValue(false);
                    authState.setValue(true);
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    error.setValue("Failed to create user profile: " + e.getMessage());
                    firebaseUser.delete();
                });
    }

    private void fetchUserData(FirebaseUser firebaseUser) {
        userRepository.getUserByEmail(firebaseUser.getEmail())
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        try {
                            setUserRole(user.getRole());
                            authState.setValue(true);
                        } catch (IllegalArgumentException e) {
                            error.setValue("Invalid user role retrieved from the database.");
                        }
                    } else {
                        error.setValue("User account not found.");
                    }
                })
                .addOnFailureListener(e -> {
                    error.setValue("Error fetching user data: " + e.getMessage());
                });
    }
}
