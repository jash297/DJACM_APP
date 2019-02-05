package com.imbuegen.alumniapp.Service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMInstanceId extends FirebaseMessagingService {

    static String TAG = "Debug";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody() + remoteMessage.getNotification().getTitle());

            MyNotifManager.getInstance(getApplicationContext())
                    .displayNotif(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }
}
