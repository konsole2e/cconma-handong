package handong.cconma.cconmaadmin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import handong.cconma.cconmaadmin.R;

/**
 * Created by Young Bin Kim on 2015-07-06.
 */
public class MyWebView extends AppCompatActivity {
    Toolbar toolbar;
    android.webkit.WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_webview);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        webview = (WebView)findViewById(R.id.webView);

        Intent data = getIntent();
        String url = data.getStringExtra("URL");
        final Activity activity = this;

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(android.webkit.WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });

        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.canGoBackOrForward(5);
        webview.loadUrl(url);
        webview.setWebViewClient(new WebClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if(webview.canGoBack()){
                        webview.goBack();
                    }else{
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}

class WebClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
