package com.cconma.cconma_admin_test.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cconma.cconma_test_actionbar.R;

/**
 * Created by Young Bin Kim on 2015-07-06.
 */
public class LoginWebView extends AppCompatActivity {
    Toolbar toolbar;
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_webview);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar haha = getSupportActionBar();
        haha.setDisplayHomeAsUpEnabled(true);

        webview = (WebView)findViewById(R.id.webView);

        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });

        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        webview.loadUrl("http://www.cconma.com/CconmaAdmin/login.fmv?cmd=loginForm&path=%2FCconmaAdmin%2Fmain.fmv"); //cconma login mobile page
    }
}

class WebClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
