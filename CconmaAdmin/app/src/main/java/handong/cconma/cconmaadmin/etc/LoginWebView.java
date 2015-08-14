package handong.cconma.cconmaadmin.etc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.Cookies;
/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class LoginWebView extends AppCompatActivity {
    private android.webkit.WebView webview;
    private CircularProgressBar circularProgressBar;
    private int autoLogin;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        circularProgressBar= (CircularProgressBar) findViewById(R.id.progressbar_circular);

        webview = (WebView) findViewById(R.id.webView);

        url = "http://www.cconma.com/mobile/auth/index.pmv?path=http://www.cconma.com%2Fmobile%2Findex.pmv";

        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        String userAgent = webview.getSettings().getUserAgentString();
        Cookies.getInstance(null).removeAllCookies();
        webview.getSettings().setUserAgentString(userAgent + ";com.cconma.app");
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl(url);
        webview.setWebViewClient(new WebClient());
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            if(url.contains("index.pmv") && !url.contains("join.pmv")) {
                webview.setVisibility(View.GONE);
                ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).start();

                Log.d("debugging", "loginwebview thread start");
                Cookies.getInstance(LoginWebView.this).updateCookies(url);
                Intent intent = new Intent(LoginWebView.this, StartupWebView.class);
                startActivity(intent);
                finish();

                return true;
            }else if(url.contains("join.pmv")){
                return true;
            }else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        public void onPageFinished(WebView view, String url){
            Log.d("debugging", "LoginWebView onPageFinished");
            ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).progressiveStop();
        }
    }
}
