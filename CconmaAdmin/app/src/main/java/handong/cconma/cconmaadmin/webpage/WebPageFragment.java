package handong.cconma.cconmaadmin.webpage;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.etc.SwipeToRefresh;
import handong.cconma.cconmaadmin.mainpage.AdminApplication;
import handong.cconma.cconmaadmin.mainpage.MainFragment;

/**
 * Created by Young Bin Kim on 2015-08-10.
 */
public class WebPageFragment extends Fragment{
    private static final String ARG_URL = "ARG_URL";
    private String url;
    private View view;
    private WebView pageWebView;
    private static WebPageFragment fragment;
    private CircularProgressBar progressBar;
    private SwipeRefreshLayout sr;
    private String location;
    private String current_page;

    public static WebPageFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment = new WebPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    static Fragment getInstance(){
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Debugging", "onCreate IN!!!");
        url = getArguments().getString(ARG_URL);
        Log.d("debugging", "URL URL: " + url);
        location = getResources().getString(R.string.www);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.navi_webview, container, false);
        pageWebView = (WebView)view.findViewById(R.id.navi_webView);
        pageWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        progressBar = (CircularProgressBar)view.findViewById(R.id.progressbar_circular);
        progressBar.setVisibility(View.VISIBLE);

        sr = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
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
        loadingUrl();

        return view;
    }

    public void loadingUrl(){
        pageWebView.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        String userAgent = pageWebView.getSettings().getUserAgentString();
        pageWebView.getSettings().setUserAgentString(userAgent + ";com.admincconma.app");
        pageWebView.getSettings().setBuiltInZoomControls(true);
        pageWebView.getSettings().setSupportZoom(true);
        pageWebView.getSettings().setBuiltInZoomControls(false);
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
        pageWebView.loadUrl(location + url);
    }

    class WebClient extends WebViewClient {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url){
            Log.d("debugging", "URL: " + url);
            current_page = url;
            progressBar.setVisibility(View.GONE);
            Cookies.getInstance(getActivity().getApplicationContext()).updateCookies(url);
            if( sr.isRefreshing() ){
                sr.setRefreshing(false);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}