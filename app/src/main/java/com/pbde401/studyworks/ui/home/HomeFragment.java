package com.pbde401.studyworks.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.pbde401.studyworks.R;

public class HomeFragment extends Fragment {

    private FeatureData[] features = {
        new FeatureData("For Students", 
            "Access exclusive opportunities tailored for students and recent graduates"),
        new FeatureData("For Employers",
            "Connect with talented students and graduates for your organization"),
        new FeatureData("Direct Communication",
            "Seamless communication between candidates and employers")
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        setupFeatures(view);
        setupButtons(view);
        
        return view;
    }

    private void setupFeatures(View view) {
        LinearLayout featuresContainer = view.findViewById(R.id.featuresContainer);
        
        for (FeatureData feature : features) {
            MaterialCardView card = (MaterialCardView) getLayoutInflater()
                .inflate(R.layout.component_home_feature_card, featuresContainer, false);
            
            TextView titleView = card.findViewById(R.id.feature_title);
            TextView descriptionView = card.findViewById(R.id.feature_description);
            
            titleView.setText(feature.getTitle());
            descriptionView.setText(feature.getDescription());
            
            featuresContainer.addView(card);
        }
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.getStartedButton).setOnClickListener(v -> {
            navigateToRegister();
        });

        view.findViewById(R.id.signInButton).setOnClickListener(v -> {
            navigateToLogin();
        });

        view.findViewById(R.id.ctaButton).setOnClickListener(v -> {
            navigateToRegister();
        });
    }

    private void navigateToRegister() {
        // Navigation will be implemented when auth fragments are ready
    }

    private void navigateToLogin() {
        // Navigation will be implemented when auth fragments are ready
    }

    private static class FeatureData {
        private final String title;
        private final String description;

        FeatureData(String title, String description) {
            this.title = title;
            this.description = description;
        }

        String getTitle() { return title; }
        String getDescription() { return description; }
    }
}