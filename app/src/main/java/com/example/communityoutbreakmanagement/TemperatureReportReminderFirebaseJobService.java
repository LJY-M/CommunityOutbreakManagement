package com.example.communityoutbreakmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class TemperatureReportReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    private String[] identityInformation;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters job) {

        Resident resident = new Resident("","","", "");
        Bundle bundle = job.getExtras();
        identityInformation = new String[]{
                bundle.getString(resident.identityAuthentication + "0"),
                bundle.getString(resident.identityAuthentication + "1")};


        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {

                Context context =TemperatureReportReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_REMIND_REPORT_TIME_TASK_REMINDER, identityInformation);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) {
            mBackgroundTask.cancel(true);
        }
        return true;
    }
}
