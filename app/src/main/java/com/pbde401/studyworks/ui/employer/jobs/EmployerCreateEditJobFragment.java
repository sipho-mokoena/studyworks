package com.pbde401.studyworks.ui.employer.jobs;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbde401.studyworks.R;

public class EmployerCreateEditJobFragment extends Fragment {

    private EmployerCreateEditJobViewModel viewModel;

    public static EmployerCreateEditJobFragment newInstance() {
        return new EmployerCreateEditJobFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employer_create_edit_job, container, false);
    }

}