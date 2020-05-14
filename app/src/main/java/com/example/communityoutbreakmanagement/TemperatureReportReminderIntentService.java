package com.example.communityoutbreakmanagement;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;


public class TemperatureReportReminderIntentService extends IntentService {

    private String[] identityInformation;

    public TemperatureReportReminderIntentService() {
        super("TemperatureReportReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            if (intent.hasExtra("identityAuthentication")) {
                identityInformation = intent.getStringArrayExtra("identityAuthentication");
                Toast.makeText(this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        String action = intent.getAction();
        ReminderTasks.executeTask(this, action, identityInformation);
    }
}
