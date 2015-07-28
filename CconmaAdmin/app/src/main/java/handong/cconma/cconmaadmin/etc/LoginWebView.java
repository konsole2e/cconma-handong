package handong.cconma.cconmaadmin.etc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.CircularArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.mainpage.BaseActivity;
import handong.cconma.cconmaadmin.mainpage.MainActivity;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class LoginWebView extends AppCompatActivity {
    private android.webkit.WebView webview;
    private CircularProgressBar circularProgressBar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        circularProgressBar= (CircularProgressBar) findViewById(R.id.progressbar_circular);

        webview = (WebView) findViewById(R.id.webView);

        //Intent data = getIntent();
        //url = data.getStringExtra("URL");
        url = "http://www.cconma.com/mobile/auth/index.pmv?path=http%3A%2F%www.cconma.com%2Fmobile%2Findex.pmv";
        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        webview.getSettings().setBuiltInZoomControls(true);
        webview.loadUrl(url);
        webview.setWebViewClient(new WebClient());
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            if(url.contains("index.pmv")){
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            }else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        public void onPageFinished(WebView view, String url){
            Cookies.getInstance().updateCookies(url);
            ((CircularProgressDrawable)circularProgressBar.getIndeterminateDrawable()).progressiveStop();
        }
    }
}