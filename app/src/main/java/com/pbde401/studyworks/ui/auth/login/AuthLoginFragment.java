package com.pbde401.studyworks.ui.auth.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.User;
import com.pbde401.studyworks.data.models.enums.UserRole;
import androidx.core.content.ContextCompat;

public class AuthLoginFragment extends Fragment {

    private AuthLoginViewModel viewModel;
    private EditText emailEditText;
    private EditText passwordEditText;
    private RadioGroup roleRadioGroup;
    private Button loginButton;
    private TextView registerLinkText;
    
    public AuthLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthLoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth_login, container, false);
        
        // Initialize UI elements
        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        roleRadioGroup = view.findViewById(R.id.radioGroupRole);
        loginButton = view.findViewById(R.id.buttonLogin);
        registerLinkText = view.findViewById(R.id.textViewRegisterLink);
        
        // Setup observers
        viewModel.getAuthState().observe(getViewLifecycleOwner(), isAuthenticated -> {
            if (isAuthenticated) {
                navigateToUserDashboard();
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                loginButton.setEnabled(false);
            } else {
                loginButton.setEnabled(true);
            }
        });
        
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
                        .show();
            }
        });
        
        // Set click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        
        registerLinkText.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.navigation_register);
        });
        
        return view;
    }
    
    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Basic validation
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        // Attempt login
        viewModel.login(email, password);
    }
    
    private void navigateToUserDashboard() {
        UserRole userRole = viewModel.getUserRole();

        try {
            if (userRole == UserRole.CANDIDATE) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_login_to_candidate_dashboard);
            } else if (userRole == UserRole.EMPLOYER) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_login_to_employer_dashboard);
            }
        } catch (Exception e) {
            Snackbar.make(requireView(), "Navigation error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}