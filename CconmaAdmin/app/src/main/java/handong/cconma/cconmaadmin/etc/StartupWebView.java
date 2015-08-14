package handong.cconma.cconmaadmin.etc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.data.StartUp;
import handong.cconma.cconmaadmin.mainpage.MainActivity;

/**
 * Created by Young Bin Kim on 2015-08-03.
 */
public class StartupWebView extends AppCompatActivity {
    private android.webkit.WebView webview;
    private CircularProgressBar circularProgressBar;
    private Boolean isPageLoaded;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.progressbar_circular);
        webview = (WebView) findViewById(R.id.webView);

        isPageLoaded = false;
        /*IntegratedSharedPreferences pref = new IntegratedSharedPreferences(StartupWebView.this);

        url = "http://www.cconma.com/mobile/admin-app/startup.pmv";
        String requestBody = "autoLoginAuthToken=" +
                pref.getValue("AUTO_LOGIN_AUTH_TOKEN", "");
        requestBody += "&push_id=" + pref.getValue("PUSH_ID", "");
        requestBody += "&app_ver=" + pref.getValue("APP_VERSION", "1.0.0");
        requestBody += "&android_id=" + pref.getValue("ANDROID_ID", "1");

        Log.d("debugging", "REQEUST: " + requestBody);
        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        webview.getSettings().setBuiltInZoomControls(true);
        String userAgent = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(userAgent + ";com.cconma.app");
        webview.postUrl(url, requestBody.getBytes());
        webview.setWebViewClient(new WebClient());*/
        Log.d("debugging", "StartupWebView opened!!!");
        IntegratedSharedPreferences pref = new IntegratedSharedPreferences(StartupWebView.this);

        String requestBody = "autoLoginAuthToken=" + pref.getValue("AUTO_LOGIN_AUTH_TOKEN", "")
                + "&push_id=" + pref.getValue("PUSH_ID", "") + "&app_ver=" +
                pref.getValue("APP_VERSION", "1.0.0") + "&android_id=" +
                pref.getValue("ANDROID_ID", "1");

        Log.d("debugging", "AUTO LOGIN AUTH TOKEN: " + requestBody);
        url = "http://www.cconma.com/mobile/admin-app/startup.pmv";
        Cookies.getInstance(StartupWebView.this).updateCookies(url);
        new StartUp(StartupWebView.this).post(requestBody);

        Intent intent = new Intent(StartupWebView.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    class WebClient extends WebViewClient {

        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            webview.setVisibility(View.GONE);
            ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).start();
            IntegratedSharedPreferences pref = new IntegratedSharedPreferences(StartupWebView.this);

            String requestBody = "autoLoginAuthToken=" + pref.getValue("AUTO_LOGIN_AUTH_TOKEN", "")
                    + "&push_id=" + pref.getValue("PUSH_ID", "") + "&app_ver=" +
                    pref.getValue("APP_VERSION", "1.0.0") + "&android_id=" +
                    pref.getValue("ANDROID_ID", "1");

            Log.d("debugging", "AUTO LOGIN AUTH TOKEN: " + requestBody);
            new StartUp(StartupWebView.this).post(requestBody);
        }

        public void onPageFinished(WebView view, String url) {
            ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).progressiveStop();
            Cookies.getInstance(StartupWebView.this).updateCookies(url);
            isPageLoaded = true;
            Intent intent = new Intent(StartupWebView.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
