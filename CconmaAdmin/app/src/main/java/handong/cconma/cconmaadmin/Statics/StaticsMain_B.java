package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import net.htmlparser.jericho.Source;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.HTTPConnector;
import handong.cconma.cconmaadmin.etc.JSONResponse;
import handong.cconma.cconmaadmin.mainpage.BaseActivity;

public class StaticsMain_B extends Activity implements View.OnClickListener {
    private Button orderH;
    private Button orderRcnt;
    private Button trade;
    private Button like;
    private Button member;
    private Button memberRcnt;
    private String result;
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_main_b);

        orderH = (Button) findViewById(R.id.order_hourly_btn);
        orderH.setOnClickListener(this);
        orderRcnt = (Button) findViewById(R.id.order_recent_btn);
        orderRcnt.setOnClickListener(this);
        trade = (Button) findViewById(R.id.trade_btn);
        trade.setOnClickListener(this);
        like = (Button) findViewById(R.id.like_btn);
        like.setOnClickListener(this);
        member = (Button) findViewById(R.id.member_btn);
        member.setOnClickListener(this);
        memberRcnt = (Button) findViewById(R.id.member_recent_btn);
        memberRcnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_hourly_btn:
                startActivity(new Intent(this, StaticsOrderH.class));
                break;
            case R.id.order_recent_btn:
                startActivity(new Intent(this, StaticsOrderRecent.class));
                break;
            case R.id.trade_btn:
                startActivity(new Intent(this, StaticsTrade.class));
                break;
            case R.id.like_btn:
                startActivity(new Intent(this, StaticsLike.class));
                break;
            case R.id.member_btn:
                startActivity(new Intent(this, StaticsMember.class));
                break;
            case R.id.member_recent_btn:
                startActivity(new Intent(this, StaticsMemberRecent.class));
                break;
        }
    }
}
