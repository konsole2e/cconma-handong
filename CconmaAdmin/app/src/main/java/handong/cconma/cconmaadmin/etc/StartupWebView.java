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

        circularProgressBar= (CircularProgressBar) findViewById(R.id.progressbar_circular);
        webview = (WebView) findViewById(R.id.webView);

        isPageLoaded = false;
        url = "http://www.cconma.com/mobile/admin-app/startup.pmv";
        String requestBody = "autoLoginAuthToken=" +
                new IntegratedSharedPreferences(StartupWebView.this).
                        getValue("AUTO_LOGIN_AUTH_TOKEN", "");

        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        webview.getSettings().setBuiltInZoomControls(true);
        String userAgent = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(userAgent + ";com.cconma.app");
        webview.postUrl(url, requestBody.getBytes());
        webview.setWebViewClient(new WebClient());
    }

    class WebClient extends WebViewClient {

        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            webview.setVisibility(View.GONE);
            ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).start();
            String requestBody = "autoLoginAuthToken=" +
                    new IntegratedSharedPreferences(StartupWebView.this).
                    getValue("AUTO_LOGIN_AUTH_TOKEN", "");
            Log.d("debugging", "AUTO LOGIN AUTH TOKEN: " + requestBody);
            new StartUp(StartupWebView.this).post(requestBody);
        }

        public void onPageFinished(WebView view, String url){
                ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).progressiveStop();
                Cookies.getInstance(StartupWebView.this).updateCookies(url);
                isPageLoaded = true;
                startActivity(new Intent(StartupWebView.this, MainActivity.class));
                finish();
        }
    }
}
