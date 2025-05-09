package com.pbde401.studyworks.ui.candidate.jobs;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.Navigation;

import com.pbde401.studyworks.R;

import android.content.Intent;
import com.pbde401.studyworks.ui.candidate.chats.CandidateChatActivity;

public class CandidateSingleJobFragment extends Fragment {

    private CandidateSingleJobViewModel mViewModel;

    public static CandidateSingleJobFragment newInstance() {
        return new CandidateSingleJobFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidate_single_job, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CandidateSingleJobViewModel.class);
        // TODO: Use the ViewModel

        // Retrieve passed jobId argument
        String jobId = getArguments() != null ? getArguments().getString("jobId") : null;

        // Set up Apply button navigation
        Button btnApply = requireView().findViewById(R.id.btnApply);
        btnApply.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("jobId", jobId);
            Navigation.findNavController(v)
                .navigate(R.id.action_navigation_candidate_single_job_to_navigation_candidate_job_application, bundle);
        });

        // Set up Chat button
        Button btnChat = requireView().findViewById(R.id.btnChat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CandidateChatActivity.class);
            intent.putExtra("chatId", jobId);
            startActivity(intent);
        });
    }

}