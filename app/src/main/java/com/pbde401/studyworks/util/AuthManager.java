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
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>(false);

    private AuthManager() {
        // Initialize with current Firebase user if logged in
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userRepository.getUserByEmail(firebaseUser.getEmail())
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
        return firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() != null) {
                        userRepository.getUserByEmail(email)
                                .addOnSuccessListener(user -> {
                                    currentUser.setValue(user);
                                    isAuthenticated.setValue(true);
                                });
                    }
                });
    }

    public Task<AuthResult> register(String email, String password, String fullName, UserRole role) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        // Create user document in Firestore
                        String userId = UUID.randomUUID().toString();
                        User user = new User(
                                userId,
                                new Date(),
                                new Date(),
                                firebaseUser.getUid(),
                                fullName,
                                email,
                                role
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
        currentUser.setValue(null);
        isAuthenticated.setValue(false);
    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
