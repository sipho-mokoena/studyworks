package com.pbde401.studyworks.ui.employer.jobs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbde401.studyworks.R;


public class EmployerSingleJobFragment extends Fragment {

    public EmployerSingleJobFragment() {
        // Required empty public constructor
    }

    public static EmployerSingleJobFragment newInstance(String param1, String param2) {
        EmployerSingleJobFragment fragment = new EmployerSingleJobFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employer_single_job, container, false);
    }
}