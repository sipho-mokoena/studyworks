package com.pbde401.studyworks.ui.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pbde401.studyworks.data.repository.UserRepository;
import com.pbde401.studyworks.data.models.enums.UserRole;

public class AuthLoginViewModel extends ViewModel {
    
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final UserRepository userRepository = new UserRepository();
    
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
    
    public void login(String email, String password, UserRole selectedRole) {
        isLoading.setValue(true);
        error.setValue(null);
        
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        fetchUserData(firebaseUser, selectedRole);
                    } else {
                        isLoading.setValue(false);
                        error.setValue("Login failed. Please check your credentials and try again.");
                    }
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    error.setValue("Login failed: " + e.getMessage());
                });
    }
    
    private void fetchUserData(FirebaseUser firebaseUser, UserRole selectedRole) {
        userRepository.getUserByEmail(firebaseUser.getEmail())
            .addOnSuccessListener(user -> {
                if (user != null) {
                    try {
                        userRole = selectedRole;
                        isLoading.setValue(false);
                        authState.setValue(true);
                    } catch (IllegalArgumentException e) {
                        isLoading.setValue(false);
                        error.setValue("Invalid user role retrieved from the database.");
                    }
                } else {
                    isLoading.setValue(false);
                    error.setValue("User account not found.");
                }
            })
            .addOnFailureListener(e -> {
                isLoading.setValue(false);
                error.setValue("Error fetching user data: " + e.getMessage());
            });
    }
}
