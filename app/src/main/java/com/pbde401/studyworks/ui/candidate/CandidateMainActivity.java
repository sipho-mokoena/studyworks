package com.pbde401.studyworks.ui.candidate;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.pbde401.studyworks.R;
import com.pbde401.studyworks.databinding.ActivityCandidateMainBinding;

public class CandidateMainActivity extends AppCompatActivity {
    private ActivityCandidateMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCandidateMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar if needed
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Set up navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_candidate_main);
        
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            
            // Set up bottom navigation
            BottomNavigationView bottomNav = findViewById(R.id.menu_candidate_bottom_navigation);
            appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_candidate_dashboard,
                R.id.navigation_candidate_jobs,
                R.id.navigation_candidate_applications,
                R.id.navigation_candidate_chats,
                R.id.navigation_candidate_profile
            ).build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}