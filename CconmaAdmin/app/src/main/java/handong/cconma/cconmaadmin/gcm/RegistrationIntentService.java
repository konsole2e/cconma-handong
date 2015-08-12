package handong.cconma.cconmaadmin.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;

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
        IntegratedSharedPreferences pref = new IntegratedSharedPreferences(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;

        String default_senderId = getString(R.string.gcm_defaultSenderId);

        try {
            token = instanceID.getToken(default_senderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            pref.put("PUSH_ID", token);

            Log.d(TAG, "TOKEN saved. TOKEN key: " + pref.getValue("TOKEN", ""));

            //gcmAsyncTask task = new gcmAsyncTask(device_id);
            //task.execute(json);

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        Log.d(TAG, "GCM Registration Token: " + token);

    }

    class gcmAsyncTask extends AsyncTask<JSONObject, String, String> {

        private final static String TAG = "debugging";
        private String device_name;

        public gcmAsyncTask(String device_name) {
            this.device_name = device_name;
        }

        @Override
        protected String doInBackground(JSONObject... params) {
            String sResult = "Error";

            try {
                Log.d(TAG, "START!!!");
                URL url = new URL("http://wonderwallserverfromyb.hol.es/register.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                String body = params[0].toString();

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(body);
                osw.flush();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;

                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }
                sResult = builder.toString();
            } catch (MalformedURLException e) {
                Log.d(TAG, "exception " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "IOException " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "message " + sResult);
            return sResult;
        }
    }
}

