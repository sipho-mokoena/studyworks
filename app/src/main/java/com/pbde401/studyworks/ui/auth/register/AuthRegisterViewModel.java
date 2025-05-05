package com.pbde401.studyworks.ui.auth.register;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.models.enums.UserRole;
import com.pbde401.studyworks.util.AuthManager;

public class AuthRegisterViewModel extends ViewModel {
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
    
    public void register(String email, String password, UserRole role) {
        isLoading.setValue(true);
        error.setValue(null);
        
        authManager.register(email, password, "", role)
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    error.setValue("Registration failed: " + e.getMessage());
                })
                .addOnCompleteListener(task -> {
                    isLoading.setValue(false);
                });
    }
}
