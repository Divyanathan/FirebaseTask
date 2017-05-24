package com.example.user.firebaseexample.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by user on 23/05/17.
 */

public class FireBaseMessagingInstanceService extends FirebaseInstanceIdService {

    private static final String TAG = "FireBaseMessagingInstan";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.d(TAG, "onTokenRefresh: "+FirebaseInstanceId.getInstance().getToken());

    }
}
