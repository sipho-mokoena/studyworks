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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.pbde401.studyworks.data.repository.JobsRepository;
import com.pbde401.studyworks.data.repository.ChatsRepository;
import com.pbde401.studyworks.data.models.Job;
import androidx.lifecycle.Observer;
import androidx.lifecycle.LiveData;
import com.pbde401.studyworks.data.repository.ApplicationsRepository;
import com.pbde401.studyworks.data.models.Chat;
import androidx.navigation.Navigation;
import android.app.AlertDialog;
import com.pbde401.studyworks.data.models.enums.ApplicationStatus;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CandidateSingleApplicationFragment extends Fragment {

    private CandidateSingleApplicationViewModel mViewModel;
    private TextView tvJobTitle;
    private TextView tvCompany;
    private TextView tvStatus;
    private TextView tvAppliedDate;
    private TextView tvJobDesc;
    private LinearLayout layoutReq;
    private TextView tvCoverLetter;
    private TextView tvPortfolioLabel;
    private TextView tvPortfolioLink;
    private TextView tvLinkedinLabel;
    private TextView tvLinkedinLink;
    private Button btnEdit;
    private Button btnWithdraw;
    private Button btnResubmit;
    private Button btnMessage;
    private Button btnDelete;

    public static CandidateSingleApplicationFragment newInstance() {
        return new CandidateSingleApplicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_single_application, container, false);
        mViewModel = new ViewModelProvider(this).get(CandidateSingleApplicationViewModel.class);
        tvJobTitle = view.findViewById(R.id.tvJobTitleDetail);
        tvCompany = view.findViewById(R.id.tvCompanyNameDetail);
        tvStatus = view.findViewById(R.id.tvStatusDetail);
        tvAppliedDate = view.findViewById(R.id.tvAppliedDateDetail);
        tvJobDesc = view.findViewById(R.id.tvJobDescription);
        layoutReq = view.findViewById(R.id.layoutRequirements);
        tvCoverLetter = view.findViewById(R.id.tvCoverLetter);
        tvPortfolioLabel = view.findViewById(R.id.tvPortfolioLabel);
        tvPortfolioLink = view.findViewById(R.id.tvPortfolioLink);
        tvLinkedinLabel = view.findViewById(R.id.tvLinkedinLabel);
        tvLinkedinLink = view.findViewById(R.id.tvLinkedinLink);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnWithdraw = view.findViewById(R.id.btnWithdraw);
        btnResubmit = view.findViewById(R.id.btnResubmit);
        btnMessage = view.findViewById(R.id.btnMessage);
        btnDelete = view.findViewById(R.id.btnDelete);

        String applicationId = getArguments() != null ? getArguments().getString("applicationId") : null;
        if (applicationId != null) {
            mViewModel.getApplication(applicationId).observe(getViewLifecycleOwner(), new Observer<Application>() {
                @Override
                public void onChanged(Application app) {
                    if (app == null) return;
                    tvJobTitle.setText(app.getJobTitle());
                    tvStatus.setText(app.getStatus().getValue());
                    tvAppliedDate.setText(String.format("%s", formatDate(app.getCreatedAt())));
                    tvCoverLetter.setText(app.getCoverLetter() != null ? app.getCoverLetter() : "Not provided");
                    if (app.getPortfolioUrl() != null) {
                        tvPortfolioLabel.setVisibility(View.VISIBLE);
                        tvPortfolioLink.setVisibility(View.VISIBLE);
                        tvPortfolioLink.setText(app.getPortfolioUrl());
                    }
                    if (app.getLinkedinUrl() != null) {
                        tvLinkedinLabel.setVisibility(View.VISIBLE);
                        tvLinkedinLink.setVisibility(View.VISIBLE);
                        tvLinkedinLink.setText(app.getLinkedinUrl());
                    }
                    // load job details
                    new JobsRepository().getJob(app.getJobId()).observe(getViewLifecycleOwner(), new Observer<Job>() {
                        @Override
                        public void onChanged(Job job) {
                            if (job == null) return;
                            tvJobTitle.setText(job.getTitle());
                            tvCompany.setText(job.getCompanyName());
                            tvJobDesc.setText(job.getDescription());
                            layoutReq.removeAllViews();
                            for (String req : job.getRequirements()) {
                                TextView tv = new TextView(requireContext());
                                tv.setText("• " + req);
                                layoutReq.addView(tv);
                            }
                        }
                    });
                    // setup edit
                    btnEdit.setOnClickListener(v -> {
                        Bundle b = new Bundle();
                        b.putString("jobId", app.getJobId());
                        Navigation.findNavController(v)
                            .navigate(R.id.action_navigation_candidate_single_application_to_navigation_candidate_job_application, b);
                    });
                    // setup message
                    new ChatsRepository().getChatByEmployerIdAndCandidateId(app.getEmployerId(), app.getCandidateId())
                        .observe(getViewLifecycleOwner(), new Observer<Chat>() {
                            @Override
                            public void onChanged(Chat chat) {
                                if (chat != null) {
                                    btnMessage.setOnClickListener(v -> {
                                        Intent intent = new Intent(requireContext(), CandidateChatActivity.class);
                                        intent.putExtra("chatId", chat.getId());
                                        startActivity(intent);
                                    });
                                } else {
                                    btnMessage.setEnabled(false);
                                }
                            }
                        });
                    // setup delete
                    btnDelete.setOnClickListener(v -> {
                        new AlertDialog.Builder(requireContext())
                            .setTitle("Confirm")
                            .setMessage("Delete application?")
                            .setPositiveButton("Yes", (d, which) -> {
                                new ApplicationsRepository().deleteApplication(applicationId);
                                requireActivity().onBackPressed();
                            })
                            .setNegativeButton("No", null)
                            .show();
                    });
                    // configure withdraw/resubmit visibility and actions
                    if (app.getStatus() == ApplicationStatus.SUBMITTED) {
                        btnWithdraw.setVisibility(View.VISIBLE);
                        btnResubmit.setVisibility(View.GONE);
                        btnWithdraw.setOnClickListener(v -> {
                            new AlertDialog.Builder(requireContext())
                                .setTitle("Confirm")
                                .setMessage("Withdraw application?")
                                .setPositiveButton("Yes", (d, which) -> {
                                    new ApplicationsRepository().updateApplicationStatus(applicationId, ApplicationStatus.WITHDRAWN);
                                    tvStatus.setText(ApplicationStatus.WITHDRAWN.getValue());
                                    btnWithdraw.setVisibility(View.GONE);
                                    btnResubmit.setVisibility(View.VISIBLE);
                                })
                                .setNegativeButton("No", null)
                                .show();
                        });
                    } else if (app.getStatus() == ApplicationStatus.WITHDRAWN) {
                        btnWithdraw.setVisibility(View.GONE);
                        btnResubmit.setVisibility(View.VISIBLE);
                        btnResubmit.setOnClickListener(v -> {
                            new AlertDialog.Builder(requireContext())
                                .setTitle("Confirm")
                                .setMessage("Resubmit application?")
                                .setPositiveButton("Yes", (d, which) -> {
                                    new ApplicationsRepository().updateApplicationStatus(applicationId, ApplicationStatus.SUBMITTED);
                                    tvStatus.setText(ApplicationStatus.SUBMITTED.getValue());
                                    btnResubmit.setVisibility(View.GONE);
                                    btnWithdraw.setVisibility(View.VISIBLE);
                                })
                                .setNegativeButton("No", null)
                                .show();
                        });
                    } else {
                        btnWithdraw.setVisibility(View.GONE);
                        btnResubmit.setVisibility(View.GONE);
                    }
                }
            });
        }
        return view;
    }

    private String formatDate(java.util.Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);
    }
}