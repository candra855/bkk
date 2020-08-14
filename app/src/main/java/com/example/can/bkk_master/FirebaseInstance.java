package com.example.can.bkk_master;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstance extends FirebaseInstanceIdService {


    private static final String TAG = "Belajar";

    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "refresh token:"+token);

    }

    public void caritoken()
    {
        onTokenRefresh();
    }
    // New code
    // New code
//    @Override
//    public void onNewToken(String token) {
//        Log.d(TAG, "Refreshed token: " + token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
//    }

}
