package handong.cconma.cconmaadmin.mainpage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;

import java.util.concurrent.ExecutionException;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.data.StartUp;
import handong.cconma.cconmaadmin.etc.LoginWebView;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;
import handong.cconma.cconmaadmin.etc.StartupWebView;
import handong.cconma.cconmaadmin.gcm.RegistrationIntentService;
import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-15.
 */
public class StartPage extends AppCompatActivity {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "debugging";
    private CircularProgressBar circularProgressBar;
    private ImageView logoImage;
    private ImageView backgroundImage;

    private IntegratedSharedPreferences pref;
    private JSONObject responseJson;

    private int playServiceFlag = 0;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        getWindow().setBackgroundDrawable(null);

        logoImage = (ImageView) findViewById(R.id.startup_image);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.progressbar_circular);
        circularProgressBar.setVisibility(View.VISIBLE);

        getInstanceIdToken();

        String board_no = getIntent().getStringExtra("board_no");
        String boardArticle_no = getIntent().getStringExtra("boardarticle_no");
        if (board_no != null && boardArticle_no != null) {
            AdminApplication.getInstance().setBoardNo(board_no);
            AdminApplication.getInstance().setArticleNo(boardArticle_no);
        }

        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d(TAG, "device id: " + device_id);
        new IntegratedSharedPreferences(StartPage.this).put("ANDROID_ID", device_id);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            Log.d(TAG, "VERSION: " + version);
            new IntegratedSharedPreferences(StartPage.this).
                    put("APP_VERSION", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pref = new IntegratedSharedPreferences(getApplicationContext());
                if (pref.getValue("AUTO_LOGIN_AUTH_ENABLED", "").equals("1")) {
                    intent = new Intent(StartPage.this, StartupWebView.class);
                } else {
                    intent = new Intent(StartPage.this, LoginWebView.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000);

        /*
        this.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo);
                        logoImage.startAnimation(animation);
                        animation.setFillAfter(true);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                circularProgressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                });
        */
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            IntegratedSharedPreferences pref = new IntegratedSharedPreferences(getApplicationContext());
            String token = pref.getValue("PUSH_ID", "");
            //if(token.equals("")) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            // }
            //else{
            Log.d(TAG, "TOKEN already exists!!! Your token is " + token);
            // }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            /*if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }*/
            return false;
        }
        return true;
    }
}
