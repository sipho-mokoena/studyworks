package com.pbde401.studyworks.ui.candidate.applications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Application;
import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<Application> applications = new ArrayList<>();
    private final CandidateApplicationsFragment fragment;

    public ApplicationAdapter(CandidateApplicationsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_application_item, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.bindApplication(application);
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
        notifyDataSetChanged();
    }

    class ApplicationViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitleView;
        private final TextView statusView;
        private final TextView dateView;

        ApplicationViewHolder(View itemView) {
            super(itemView);
            jobTitleView = itemView.findViewById(R.id.tvJobTitle);
            statusView = itemView.findViewById(R.id.tvStatus);
            dateView = itemView.findViewById(R.id.tvAppliedDate);
        }

        void bindApplication(Application application) {
            jobTitleView.setText(application.getJobTitle());
            statusView.setText(application.getStatus().getValue());
            dateView.setText(application.getAppliedAt().toString());
            
            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("applicationId", application.getId());
                Navigation.findNavController(v)
                    .navigate(R.id.action_navigation_candidate_applications_to_navigation_candidate_single_application, bundle);
            });
        }
    }
}