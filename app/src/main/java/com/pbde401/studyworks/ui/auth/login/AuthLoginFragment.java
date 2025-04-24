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
import com.pbde401.studyworks.data.models.enums.UserRole;
import androidx.core.content.ContextCompat;

public class AuthLoginFragment extends Fragment {

    private AuthLoginViewModel viewModel;
    private EditText emailEditText;
    private EditText passwordEditText;
    private RadioGroup roleRadioGroup;
    private Button loginButton;
    private TextView registerLinkText;
    private View loadingOverlay;
    
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
        loadingOverlay = view.findViewById(R.id.loadingOverlay);
        
        // Setup observers
        viewModel.getAuthState().observe(getViewLifecycleOwner(), isAuthenticated -> {
            if (isAuthenticated) {
                UserRole role = viewModel.getUserRole();
                navigateToUserDashboard(role);
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                loadingOverlay.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
            } else {
                loadingOverlay.setVisibility(View.GONE);
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
        int checkedId = roleRadioGroup.getCheckedRadioButtonId();
        UserRole role;
        if (checkedId == R.id.radioButtonCandidate) {
            role = UserRole.CANDIDATE;
        } else if (checkedId == R.id.radioButtonEmployer) {
            role = UserRole.EMPLOYER;
        } else {
            role = UserRole.GUEST;
        }

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
        viewModel.login(email, password, role);
    }
    
    private void navigateToUserDashboard(UserRole role) {
        try {
            if (role == UserRole.CANDIDATE) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_login_to_candidate_dashboard);
            } else if (role == UserRole.EMPLOYER) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_login_to_employer_dashboard);
            } else {
                Snackbar.make(requireView(), "Invalid user role", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Snackbar.make(requireView(), "Navigation error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}