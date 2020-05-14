package com.example.communityoutbreakmanagement;

import android.app.IntentService;
import android.content.Intent;


public class TemperatureReportReminderIntentService extends IntentService {

    public TemperatureReportReminderIntentService() {
        super("TemperatureReportReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
