package com.pbde401.studyworks.ui.candidate.profile;

import androidx.lifecycle.ViewModel;

import com.pbde401.studyworks.data.repository.CandidateProfileRepository;

public class CandidateProfileViewModel extends ViewModel {
    private final CandidateProfileRepository userRepository = new CandidateProfileRepository();
}
