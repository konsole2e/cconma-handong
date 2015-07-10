package handong.cconma.cconmaadmin.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import handong.cconma.cconmaadmin.R;

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
        Log.d(TAG, "GG");
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;

        //String default_senderId =  "277137853646";
        String default_senderId = getString(R.string.gcm_defaultSenderId);

        try{
            Log.d(TAG, "GG");
            token = instanceID.getToken(default_senderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        }catch(IOException e){
            Log.d(TAG, e.getMessage());
        }

        Log.d(TAG, "GCM Registration Token: " + token);

    }
}

