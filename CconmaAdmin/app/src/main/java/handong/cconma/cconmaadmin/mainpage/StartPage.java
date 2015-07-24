package handong.cconma.cconmaadmin.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.etc.LoginWebView;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;
import handong.cconma.cconmaadmin.etc.MyWebView;
import handong.cconma.cconmaadmin.gcm.RegistrationIntentService;

/**
 * Created by Young Bin Kim on 2015-07-15.
 */
public class StartPage extends Activity{
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "debugging";
    private CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        circularProgressBar = (CircularProgressBar)findViewById(R.id.progressbar_circular);
        circularProgressBar.setVisibility(View.VISIBLE);
        //getInstanceIdToken(); //get regId for GCM

        //startup???????
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String d = new IntegratedSharedPreferences(getApplicationContext()).getValue("COOKIE", "");
                //if(d.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), LoginWebView.class);
                    intent.putExtra("URL", "http://www.cconma.com/mobile/auth/index.pmv?path=http%3A%2F%www.cconma.com%2Fmobile%2Findex.pmv");
                    startActivity(intent);
                    finish();
                //}
                //else{
                //    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //    finish();
                //}
            }
        });

        Button button2 = (Button) findViewById(R.id.stopbutton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            IntegratedSharedPreferences pref = new IntegratedSharedPreferences(getApplicationContext());
            String token = pref.getValue("TOKEN", "");
            if(token.equals("")) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
            else{
                Log.d(TAG, "TOKEN already exists!!! Your token is " + token);
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
