package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.widget.Toast;

public class ReminderTasks {

    public static final String ACTION_REMIND_REPORT_TEMPERATURE = "remind-report-temperature";

    public static void executeTask(Context context, String action) {
        if (ACTION_REMIND_REPORT_TEMPERATURE.equals(action)) {
            Toast.makeText( context,
                    "Now start performing the operation" + ACTION_REMIND_REPORT_TEMPERATURE,
                    Toast.LENGTH_SHORT).show();
            System.out.println("Now start performing the operation" + ACTION_REMIND_REPORT_TEMPERATURE);
        }
    }
}
