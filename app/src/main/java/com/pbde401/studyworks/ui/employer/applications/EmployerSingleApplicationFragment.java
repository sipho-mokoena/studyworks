package com.pbde401.studyworks.ui.employer.applications;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbde401.studyworks.R;

public class EmployerSingleApplicationFragment extends Fragment {

    private EmployerSingleApplicationViewModel mViewModel;

    public static EmployerSingleApplicationFragment newInstance() {
        return new EmployerSingleApplicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employer_single_application, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EmployerSingleApplicationViewModel.class);
        // TODO: Use the ViewModel
    }

}