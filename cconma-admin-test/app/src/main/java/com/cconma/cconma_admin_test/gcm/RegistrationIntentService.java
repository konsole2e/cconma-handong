package com.cconma.cconma_admin_test.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Young Bin Kim on 2015-07-09.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "debugging";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;


        String default_senderId = "277137853646";
        //String default_senderId = getString(R.string.gcm_defaultSenderId);

        String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;

        try{
            token = instanceID.getToken(default_senderId, scope, null);
        }catch(IOException e){
            Log.d(TAG, e.getMessage());
        }

        Log.d(TAG, "GCM Registration Token: " + token);

    }
}

