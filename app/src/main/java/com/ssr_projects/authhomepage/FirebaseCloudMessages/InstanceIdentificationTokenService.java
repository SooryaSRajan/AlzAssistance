package com.ssr_projects.authhomepage.FirebaseCloudMessages;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

public class InstanceIdentificationTokenService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseDatabase.getInstance().getReference().child("TOKENS").setValue(s);

    }
}
