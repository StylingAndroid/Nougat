package com.stylingandroid.nougat.messenger;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.stylingandroid.nougat.R;

import java.util.List;
final class NotificationBuilder {

    private static final String GROUP_KEY = "Messenger";
    private static final String MESSAGES_KEY = "Messages";
    private static final String NOTIFICATION_ID = "com.stylingandroid.nougat.NOTIFICATION_ID";
    private static final int SUMMARY_ID = 0;
    private static final String EMPTY_MESSAGE_STRING = "[]";
    private static final String MY_DISPLAY_NAME = "Me";

    private final Context context;
    private final NotificationManagerCompat notificationManager;
    private final SharedPreferences sharedPreferences;
    private final MessageListMarshaller marshaller;

    static NotificationBuilder newInstance(Context context) {
        Context appContext = context.getApplicationContext();
        Context safeContext = ContextCompat.createDeviceProtectedStorageContext(appContext);
        if (safeContext == null) {
            safeContext = appContext;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(safeContext);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(safeContext);
        MessageListMarshaller marshaller = MessageListMarshaller.newInstance();
        return new NotificationBuilder(safeContext, notificationManager, sharedPreferences, marshaller);
    }

    private NotificationBuilder(Context context,
                                NotificationManagerCompat notificationManager,
                                SharedPreferences sharedPreferences,
                                MessageListMarshaller marshaller) {
        this.context = context.getApplicationContext();
        this.notificationManager = notificationManager;
        this.sharedPreferences = sharedPreferences;
        this.marshaller = marshaller;
    }

    @SuppressWarnings("unused")
    void sendBundledNotification(Message message) {
        Notification notification = buildNotification(message, GROUP_KEY);
        notificationManager.notify(getNotificationId(), notification);
        Notification summary = buildSummary(message, GROUP_KEY);
        notificationManager.notify(SUMMARY_ID, summary);
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
                .setContentText("You have unread messages")
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

    @SuppressWarnings("unused")
    void sendMessagingStyleNotification(Message newMessage) {
        List<Message> messages = addMessage(newMessage);
        updateMessagingStyleNotification(messages);
    }

    @NonNull
    private List<Message> addMessage(Message newMessage) {
        List<Message> messages = getMessages();
        messages.add(newMessage);
        saveMessages(messages);
        return messages;
    }

    private void saveMessages(List<Message> messages) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MESSAGES_KEY, marshaller.encode(messages));
        editor.apply();
    }

    private List<Message> getMessages() {
        String messagesString = sharedPreferences.getString(MESSAGES_KEY, EMPTY_MESSAGE_STRING);
        return marshaller.decode(messagesString);
    }

    private void updateMessagingStyleNotification(List<Message> messages) {
        NotificationCompat.MessagingStyle messagingStyle = buildMessageList(messages);
        Notification notification = new NotificationCompat.Builder(context)
                .setStyle(messagingStyle)
                .setSmallIcon(R.drawable.ic_message)
                .build();
        notificationManager.notify(SUMMARY_ID, notification);

    }

    private NotificationCompat.MessagingStyle buildMessageList(List<Message> messages) {
        NotificationCompat.MessagingStyle messagingStyle =
                new NotificationCompat.MessagingStyle(MY_DISPLAY_NAME)
                        .setConversationTitle("Messenger");
        for (Message message : messages) {
            String sender = message.sender().equals(MY_DISPLAY_NAME) ? null : message.sender();
            messagingStyle.addMessage(message.message(), message.timestamp(), sender);
        }
        return messagingStyle;
    }
}
