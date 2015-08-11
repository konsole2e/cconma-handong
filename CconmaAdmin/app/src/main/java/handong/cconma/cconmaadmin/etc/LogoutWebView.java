package handong.cconma.cconmaadmin.etc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.mainpage.StartPage;

/**
 * Created by Young Bin Kim on 2015-08-06.
 */
public class LogoutWebView extends AppCompatActivity {
    private android.webkit.WebView webview;
    private CircularProgressBar circularProgressBar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        circularProgressBar= (CircularProgressBar) findViewById(R.id.progressbar_circular);

        webview = (WebView) findViewById(R.id.webView);
        webview.setVisibility(View.GONE);

        url = "http://www.cconma.com/Cconma/logout.fmv?path=http://www.cconma.com%2Fmobile%2Findex.pmv";

        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        String userAgent = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(userAgent + ";com.cconma.app");
        webview.loadUrl(url);
        webview.setWebViewClient(new WebClient());
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            if (url.contains("index.pmv")) {
                new IntegratedSharedPreferences(LogoutWebView.this).
                        remove("AUTO_LOGIN_AUTH_ENABLED");
                new IntegratedSharedPreferences(LogoutWebView.this).
                        remove("AUTO_LOGIN_AUTH_TOKEN");
                Intent intent = new Intent(LogoutWebView.this, StartPage.class);
                startActivity(intent);
                finish();

                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        public void onPageFinished(WebView view, String url){
            ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).progressiveStop();
            Cookies.getInstance(LogoutWebView.this).updateCookies(url);
        }
    }
}