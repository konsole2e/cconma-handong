package handong.cconma.cconmaadmin.inquiry;
import handong.cconma.cconmaadmin.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by eundi on 15. 7. 9..
 */
public class InquryMember extends Activity{

    WebView webview_member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        webview_member = (WebView)findViewById(R.id.webview_inquiry);
        WebSettings webset = webview_member.getSettings();
        webset.setJavaScriptEnabled(true);
        webset.setBuiltInZoomControls(true);

        webview_member.loadUrl("http://www.cconma.com/CconmaAdmin/member.fmv?cmd=list");
        webview_member.setWebViewClient(new WebViewClientClass());

    }

    public class WebViewClientClass extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }
}
