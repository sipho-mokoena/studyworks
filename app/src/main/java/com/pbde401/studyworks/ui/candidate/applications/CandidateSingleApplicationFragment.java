package com.pbde401.studyworks.ui.candidate.applications;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Application;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import com.pbde401.studyworks.ui.candidate.chats.CandidateChatActivity;

public class CandidateSingleApplicationFragment extends Fragment {

    private CandidateSingleApplicationViewModel mViewModel;

    public static CandidateSingleApplicationFragment newInstance() {
        return new CandidateSingleApplicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_single_application, container, false);
        // Initialize ViewModel before using
        mViewModel = new ViewModelProvider(this).get(CandidateSingleApplicationViewModel.class);
        TextView tvJobTitle = view.findViewById(R.id.tvJobTitleDetail);
        TextView tvStatus = view.findViewById(R.id.tvStatusDetail);
        TextView tvAppliedDate = view.findViewById(R.id.tvAppliedDateDetail);
        
        String applicationId = getArguments() != null ? getArguments().getString("applicationId") : null;
        if (applicationId != null) {
            mViewModel.getApplication(applicationId).observe(getViewLifecycleOwner(), new Observer<Application>() {
                @Override
                public void onChanged(Application application) {
                    if (application != null) {
                        tvJobTitle.setText(application.getJobTitle());
                        tvStatus.setText(application.getStatus().getValue());
                        tvAppliedDate.setText(application.getAppliedAt().toString());
                    }
                }
            });
        }

        // Set up Chat button navigation
        Button btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CandidateChatActivity.class);
            intent.putExtra("chatId", applicationId);
            startActivity(intent);
        });

        return view;
    }
}