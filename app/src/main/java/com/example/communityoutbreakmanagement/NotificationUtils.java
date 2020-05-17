package com.example.communityoutbreakmanagement;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class NotificationUtils {


    private static final int TEMPERATURE_REPORT_REMINDER_NOTIFICATION_ID = 1138;
    private static final int TEMPERATURE_REPORT_REMINDER_PENDING_INTENT_ID = 3417;
    private static final String TEMPERATURE_REPORT_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    public static void reminderUserBecauseCharging(Context context, String[] identity) {

        String[] identityInformation = new String[]{identity[0], identity[1]};

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    TEMPERATURE_REPORT_REMINDER_NOTIFICATION_CHANNEL_ID,
                    "main_notification_channel_name",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, TEMPERATURE_REPORT_REMINDER_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(R.drawable.ic_thermometer_icon)
//                        .setLargeIcon(largeIcon(context))
                        .setContentTitle("设备工作中 - 请上报你的体温")
                        .setContentText("您好，\n 今日体温未上报，\n 请尽快上报体温便于社区统计与管理。")
                        .setStyle( new NotificationCompat.BigTextStyle().bigText(
                                "您好，\n 今日体温未上报，\n 请尽快上报体温便于社区统计与管理。"))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context, identityInformation))
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O ) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(TEMPERATURE_REPORT_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context, String[] identityInformation) {

        Resident resident = new Resident("","","", "");
        Intent startTemperatureReportActivityIntent = new Intent(context, TemperatureReportActivity.class);
        startTemperatureReportActivityIntent.putExtra(resident.identityAuthentication, identityInformation);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                TEMPERATURE_REPORT_REMINDER_PENDING_INTENT_ID,
                startTemperatureReportActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        return pendingIntent;
    }
}
