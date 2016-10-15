package com.stylingandroid.nougat.messenger;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public class MessengerService extends GcmTaskService {
    public static final String ACTION_CLEAR_MESSAGES = "com.stylingandroid.nougat.ACTION_CLEAR_MESSAGES";
    public static final String ACTION_REPLY = "com.stylingandroid.nougat.ACTION_REPLY";

    private static final String TAG = "MessengerService";

    private Messenger messenger;
    private ServiceScheduler serviceScheduler;
    private NotificationBuilder notificationBuilder;

    public MessengerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = Messenger.newInstance(this);
        serviceScheduler = ServiceScheduler.newInstance(this);
        notificationBuilder = NotificationBuilder.newInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_CLEAR_MESSAGES.equals(action)) {
            notificationBuilder.clearMessages();
            return START_NOT_STICKY;
        }
        if (ACTION_REPLY.equals(action)) {
            notificationBuilder.reply(intent);
            return START_NOT_STICKY;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Message message = messenger.generateNewMessage();
        Log.d(TAG, message.toString());
        //notificationBuilder.sendBundledNotification(message);
        notificationBuilder.sendMessagingStyleNotification(message);
        serviceScheduler.scheduleService();
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    @Override
    public void onDestroy() {
        messenger = null;
        serviceScheduler = null;
        super.onDestroy();
    }
}
