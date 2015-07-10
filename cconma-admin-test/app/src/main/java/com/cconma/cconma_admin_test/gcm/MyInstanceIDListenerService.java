package com.cconma.cconma_admin_test.gcm;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Young Bin Kim on 2015-07-09.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "debugging";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Instance ID Listener Service start");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
