package com.stylingandroid.nougat.messenger;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.stylingandroid.nougat.R;

final class NotificationBuilder {

    private static final String GROUP_KEY = "Messenger";
    private static final String NOTIFICATION_ID = "com.stylingandroid.nougat.NOTIFICATION_ID";
    private static final int SUMMARY_ID = 0;

    private final Context context;
    private final NotificationManagerCompat notificationManager;
    private final SharedPreferences sharedPreferences;

    static NotificationBuilder newInstance(Context context) {
        Context appContext = context.getApplicationContext();
        Context safeContext = ContextCompat.createDeviceProtectedStorageContext(appContext);
        if (safeContext == null) {
            safeContext = appContext;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(safeContext);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(safeContext);
        return new NotificationBuilder(safeContext, notificationManager, sharedPreferences);
    }

    private NotificationBuilder(Context context,
                                NotificationManagerCompat notificationManager,
                                SharedPreferences sharedPreferences) {
        this.context = context.getApplicationContext();
        this.notificationManager = notificationManager;
        this.sharedPreferences = sharedPreferences;
    }

    @SuppressWarnings("unused")
    void sendBundledNotification(Message message) {
        String groupKey = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? GROUP_KEY : null;
        Notification notification = buildNotification(message, groupKey);
        notificationManager.notify(getNotificationId(), notification);
        if (groupKey != null) {
            Notification summary = buildSummary(message, groupKey);
            notificationManager.notify(SUMMARY_ID, summary);
        }
    }

    private Notification buildNotification(Message message, String groupKey) {
        return new NotificationCompat.Builder(context)
                    .setContentTitle(message.sender())
                    .setContentText(message.message())
                    .setWhen(message.timestamp())
                    .setSmallIcon(R.drawable.ic_message)
                    .setShowWhen(true)
                    .setGroup(groupKey)
                    .build();
    }

    private Notification buildSummary(Message message, String groupKey) {
        return new NotificationCompat.Builder(context)
                .setContentTitle("Nougat Messenger")
                .setContentText("Summary Body")
                .setWhen(message.timestamp())
                .setSmallIcon(R.drawable.ic_message)
                .setShowWhen(true)
                .setGroup(groupKey)
                .setGroupSummary(true)
                .build();
    }

    private int getNotificationId() {
        int id = sharedPreferences.getInt(NOTIFICATION_ID, SUMMARY_ID) + 1;
        while (id == SUMMARY_ID) {
            id++;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATION_ID, id);
        editor.apply();
        return id;
    }
}
