package handong.cconma.cconmaadmin.Inquiry;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import handong.cconma.cconmaadmin.R;


/**
 * Created by eundi on 15. 7. 9..
 */
public class InquryOrder extends Activity{

    WebView webview_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        webview_order = (WebView)findViewById(R.id.webview_inquiry);
        WebSettings webset = webview_order.getSettings();
        webset.setJavaScriptEnabled(true);
        webset.setBuiltInZoomControls(true);

        webview_order.loadUrl("http://www.cconma.com/CconmaAdmin/orderList.fmv?cmd=list");
        webview_order.setWebViewClient(new WebViewClientClass());

    }

    public class WebViewClientClass extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }
}
