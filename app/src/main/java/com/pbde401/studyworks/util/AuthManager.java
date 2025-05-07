package com.pbde401.studyworks.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pbde401.studyworks.data.repository.UserRepository;
import com.pbde401.studyworks.data.models.User;
import com.pbde401.studyworks.data.models.enums.UserRole;
import java.util.Date;
import java.util.UUID;

public class AuthManager {
    private static AuthManager instance;
    private final FirebaseAuth firebaseAuth;
    private final UserRepository userRepository;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>(false);

    private AuthManager() {
        // Initialize Firebase Auth with persistence enabled
        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.setPersistenceEnabled(true);
        userRepository = new UserRepository();

        // Set up auth state listener
        firebaseAuth.addAuthStateListener(auth -> {
            FirebaseUser firebaseUser = auth.getCurrentUser();
            if (firebaseUser != null) {
                userRepository.getUserByEmail(firebaseUser.getEmail())
                        .addOnSuccessListener(user -> {
                            if (user != null) {
                                currentUser.setValue(user);
                                isAuthenticated.setValue(true);
                            } else {
                                // Handle case where user exists in Firebase Auth but not in Firestore
                                logout();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure to fetch user data
                            logout();
                        });
            } else {
                currentUser.setValue(null);
                isAuthenticated.setValue(false);
            }
        });

        // Check initial auth state
        FirebaseUser currentFirebaseUser = firebaseAuth.getCurrentUser();
        if (currentFirebaseUser != null) {
            userRepository.getUserByEmail(currentFirebaseUser.getEmail())
                    .addOnSuccessListener(user -> {
                        if (user != null) {
                            currentUser.setValue(user);
                            isAuthenticated.setValue(true);
                        }
                    });
        }
    }

    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> getAuthState() {
        return isAuthenticated;
    }

    public Task<AuthResult> login(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(String email, String password, String fullName, UserRole role) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        String userId = UUID.randomUUID().toString();
                        User user = new User(
                                userId,
                                new Date(),
                                new Date(),
                                firebaseUser.getUid(),
                                fullName,
                                email,
                                role,
                                null,
                                null
                        );
                        
                        userRepository.createUser(user)
                                .addOnSuccessListener(aVoid -> {
                                    currentUser.setValue(user);
                                    isAuthenticated.setValue(true);
                                });
                    }
                });
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null && currentUser.getValue() != null;
    }
}
