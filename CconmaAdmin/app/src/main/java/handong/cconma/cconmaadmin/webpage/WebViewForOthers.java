package handong.cconma.cconmaadmin.webpage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.Cookies;

/**
 * Created by Young Bin Kim on 2015-08-12.
 */
public class WebViewForOthers extends AppCompatActivity{
    private WebView pageWebView;
    private CircularProgressBar progressBar;
    private SwipeRefreshLayout sr;
    private String current_page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_webview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String url = getIntent().getStringExtra("url");
        pageWebView = (WebView)findViewById(R.id.other_webView);
        progressBar = (CircularProgressBar)findViewById(R.id.progressbar_circular);
        progressBar.setVisibility(View.VISIBLE);

        sr = (SwipeRefreshLayout)findViewById(R.id.refresh);
        sr.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr.setRefreshing(true);
                ((CircularProgressDrawable) progressBar.getIndeterminateDrawable()).start();
                pageWebView.loadUrl(current_page);
            }
        });

        pageWebView.getSettings().setJavaScriptEnabled(true);
        pageWebView.getSettings().setBuiltInZoomControls(true);
        pageWebView.getSettings().setSupportZoom(true);
        pageWebView.getSettings().setLoadWithOverviewMode(true);
        pageWebView.getSettings().setUseWideViewPort(true);
        pageWebView.setWebViewClient(new WebClient());
        pageWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.e("alert triggered", message);
                return false;
            }
        });
        pageWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(pageWebView.canGoBack())
            pageWebView.goBack();
        else
            super.onBackPressed();
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            Cookies.getInstance(WebViewForOthers.this).updateCookies(url);
            return true;
        }

        public void onPageFinished(WebView view, String url){
            current_page = url;
            progressBar.setVisibility(View.GONE);
            if( sr.isRefreshing() ){
                sr.setRefreshing(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
