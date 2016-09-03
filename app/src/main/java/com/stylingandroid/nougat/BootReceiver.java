package com.stylingandroid.nougat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stylingandroid.nougat.messenger.ServiceScheduler;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
            Log.d("BootReceiver", String.format("Device boot detected: %1$s", action));
            ServiceScheduler serviceScheduler = ServiceScheduler.newInstance(context);
            if (serviceScheduler.isEnabled()) {
                serviceScheduler.scheduleService();
            }
        }
    }
}
