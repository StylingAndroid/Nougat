package com.stylingandroid.nougat.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;

import java.util.Random;

public final class ServiceScheduler {
    private static final String TAG = ServiceScheduler.class.getCanonicalName();

    private static final String MESSENGER_ENABLED = "com.stylingandroid.nougat.messenger.MESSENGER_ENABLED";

    private static final long MINUTES_IN_SECONDS = 60;
    private static final long HOURS_IN_MINUTES = 60;
    private static final long HOURS_IN_SECONDS = HOURS_IN_MINUTES * MINUTES_IN_SECONDS;
    private static final int WINDOW_MAX_OFFSET = 8;
    private static final long WINDOW_SIZE = 30 * MINUTES_IN_SECONDS;

    private final GcmNetworkManager networkManager;
    private final SharedPreferences sharedPreferences;
    private boolean isEnabled;

    public static ServiceScheduler newInstance(Context context) {
        Context safeContext = context.getApplicationContext();
        GcmNetworkManager networkManager = GcmNetworkManager.getInstance(safeContext);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(safeContext);
        boolean isEnabled = sharedPreferences.getBoolean(MESSENGER_ENABLED, true);
        return new ServiceScheduler(networkManager, sharedPreferences, isEnabled);
    }

    private ServiceScheduler(GcmNetworkManager networkManager, SharedPreferences sharedPreferences, boolean isEnabled) {
        this.networkManager = networkManager;
        this.sharedPreferences = sharedPreferences;
        this.isEnabled = isEnabled;
    }

    public void startService() {
        isEnabled = true;
        saveEnabledState();
        scheduleService();
    }

    public void stopService() {
        cancelScheduledService();
        isEnabled = false;
        saveEnabledState();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    private void saveEnabledState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MESSENGER_ENABLED, isEnabled);
        editor.apply();
    }

    void scheduleService() {
        Random random = new Random();
        long nextStart = (random.nextInt(WINDOW_MAX_OFFSET) + 1) * HOURS_IN_SECONDS;
        long nextEnd = nextStart + WINDOW_SIZE;

        Task task = new OneoffTask.Builder()
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .setService(MessengerService.class)
                .setExecutionWindow(nextStart, nextEnd)
                .setUpdateCurrent(false)
                .setPersisted(false)
                .setTag(TAG)
                .build();
        Log.d(TAG, String.format("Scheduled between: %d and %d", nextStart, nextEnd));
        networkManager.schedule(task);
    }

    private void cancelScheduledService() {
        Log.d(TAG, "Cancelled Service");
        networkManager.cancelTask(TAG, MessengerService.class);
    }
}
