package com.stylingandroid.nougat.messenger;

import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public class MessengerService extends GcmTaskService {
    private static final String TAG = MessengerService.class.getCanonicalName();

    private Messenger messenger;
    private ServiceScheduler serviceScheduler;

    public MessengerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = Messenger.newInstance(this);
        serviceScheduler = ServiceScheduler.newInstance(this);
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Message message = messenger.generateNewMessage();
        Log.d(TAG, message.toString());
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
