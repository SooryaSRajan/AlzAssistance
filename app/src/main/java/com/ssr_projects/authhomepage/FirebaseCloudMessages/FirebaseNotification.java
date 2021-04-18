package com.ssr_projects.authhomepage.FirebaseCloudMessages;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotification extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        FirebaseDatabase.getInstance().getReference().child("FCM").setValue(remoteMessage.getData());
    }
}
