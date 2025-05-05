package com.pbde401.studyworks.ui.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.models.enums.UserRole;
import com.pbde401.studyworks.util.AuthManager;

public class AuthLoginViewModel extends ViewModel {
    
    private final AuthManager authManager = AuthManager.getInstance();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    
    public LiveData<Boolean> getAuthState() {
        return authManager.getAuthState();
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getError() {
        return error;
    }
    
    public UserRole getUserRole() {
        return authManager.getCurrentUser().getValue() != null ? 
            authManager.getCurrentUser().getValue().getRole() : 
            UserRole.GUEST;
    }
    
    public void login(String email, String password) {
        isLoading.setValue(true);
        error.setValue(null);
        
        authManager.login(email, password)
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    error.setValue("Login failed: " + e.getMessage());
                })
                .addOnCompleteListener(task -> {
                    isLoading.setValue(false);
                });
    }
}
