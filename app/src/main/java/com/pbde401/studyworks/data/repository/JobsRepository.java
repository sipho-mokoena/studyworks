package com.pbde401.studyworks.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pbde401.studyworks.data.models.Job;
import com.pbde401.studyworks.data.models.enums.JobType;
import com.pbde401.studyworks.data.models.enums.WorkMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class JobsRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String JOBS_COLLECTION = "jobs";

    public LiveData<List<Job>> getActiveJobs() {
        MutableLiveData<List<Job>> jobsLiveData = new MutableLiveData<>();

        db.collection(JOBS_COLLECTION)
            .whereEqualTo("active", true)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Job> jobs = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Job job = this.documentToJob(document);
                    jobs.add(job);
                }
                jobsLiveData.setValue(jobs);
            })
            .addOnFailureListener(e -> {
                // Handle any errors
                jobsLiveData.setValue(new ArrayList<>());
            });

        return jobsLiveData;
    }

    public LiveData<List<Job>> getCandidateJobs(String candidateId) {
        MutableLiveData<List<Job>> jobsLiveData = new MutableLiveData<>();

        db.collection(JOBS_COLLECTION)
            .whereArrayContains("candidateIds", candidateId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Job> jobs = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Job job = this.documentToJob(document);
                    jobs.add(job);
                }
                jobsLiveData.setValue(jobs);
            })
            .addOnFailureListener(e -> {
                // Handle any errors
                jobsLiveData.setValue(new ArrayList<>());
            });

        return jobsLiveData;
    }

    @NonNull
    private Job documentToJob(DocumentSnapshot document) {
        String id = document.getId();
        String title = document.getString("title");
        String description = document.getString("description");
        String location = document.getString("location");
        String companyId = document.getString("companyId");
        String companyName = document.getString("companyName");
        JobType type = JobType.fromString(document.getString("type"));
        WorkMode workMode = WorkMode.fromString(document.getString("workMode"));
        String level = document.getString("level");
        List<String> requirements = (List<String>) document.get("requirements");
        List<String> responsibilities = (List<String>) document.get("responsibilities");
        List<String> benefits = (List<String>) document.get("benefits");
        List<String> candidateIds = (List<String>) document.get("candidateIds");
        String salary = document.getString("salary");
        Boolean active = document.getBoolean("active");

        String createdAtString = document.getString("createdAt");
        String updatedAtString = document.getString("updatedAt");
        Date createdAt = parseDate(createdAtString);
        Date updatedAt = parseDate(updatedAtString);

        return new Job(id,
            createdAt,
            updatedAt,
            title,
            companyId,
            companyName,
            location,
            type,
            workMode,
            level,
            description,
            requirements,
            responsibilities,
            benefits,
            candidateIds,
            salary,
            active
        );
    }

    private Date parseDate(String dateString) {
        if (dateString == null) return null;
        try {
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601Format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
