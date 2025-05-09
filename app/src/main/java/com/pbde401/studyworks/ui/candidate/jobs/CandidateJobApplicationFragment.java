package com.pbde401.studyworks.ui.candidate.jobs;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbde401.studyworks.R;

public class CandidateJobApplicationFragment extends Fragment {

    private CandidateJobApplicationViewModel mViewModel;

    public static CandidateJobApplicationFragment newInstance() {
        return new CandidateJobApplicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidate_job_application, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CandidateJobApplicationViewModel.class);
        // TODO: Use the ViewModel
    }

}