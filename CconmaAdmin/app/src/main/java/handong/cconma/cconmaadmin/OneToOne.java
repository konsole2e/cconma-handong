package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by eundi on 15. 7. 9..
 */
public class OneToOne extends Activity {
    WebView webview_qna;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        webview_qna = (WebView)findViewById(R.id.webview_inquiry);
        WebSettings webset = webview_qna.getSettings();
        webset.setJavaScriptEnabled(true);
        webset.setBuiltInZoomControls(true);


        webview_qna.loadUrl("http://www.cconma.com/admin/help_board/help_board_list.pmv");
        webview_qna.setWebViewClient(new WebViewClientClass());

    }

    public class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }
}
