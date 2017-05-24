package com.example.user.firebaseexample.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by user on 23/05/17.
 */

public class FireBaseMessagingService  extends FirebaseMessagingService{

    private static final String TAG = "FireBaseMessagingServic";
    
    @Override
    public void onMessageReceived(RemoteMessage pRemoteMessage) {
        super.onMessageReceived(pRemoteMessage);

        Log.d(TAG, "onMessageReceived: ");
    }
}
