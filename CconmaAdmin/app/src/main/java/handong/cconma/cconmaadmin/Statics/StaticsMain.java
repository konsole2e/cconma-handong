package handong.cconma.cconmaadmin.Statics;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import handong.cconma.cconmaadmin.R;

public class StaticsMain extends Activity implements View.OnClickListener{
    private Button orderH;
    private Button orderRcnt;
    private Button trade;
    private Button like;
    private Button member;
    private Button memberRcnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics_main);

        orderH = (Button)findViewById(R.id.order_hourly_btn);
        orderH.setOnClickListener(this);
        orderRcnt = (Button)findViewById(R.id.order_Recent_btn);
        orderRcnt.setOnClickListener(this);
        trade = (Button)findViewById(R.id.trade_btn);
        trade.setOnClickListener(this);
        like = (Button)findViewById(R.id.like_btn);
        like.setOnClickListener(this);
        member = (Button)findViewById(R.id.member_btn);
        member.setOnClickListener(this);
        memberRcnt = (Button)findViewById(R.id.member_Recent_btn);
        memberRcnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_hourly_btn :
                startActivity(new Intent(this, StaticsOrderH.class));
                break;
            case R.id.order_Recent_btn :
                startActivity(new Intent(this, StaticsOrderRecent.class));
                break;
            case R.id.trade_btn :
                startActivity(new Intent(this, StaticsTrade.class));
                break;
            case R.id.like_btn:
                startActivity(new Intent(this, StaticsLike.class));
                break;
            case R.id.member_btn :
                startActivity(new Intent(this, StaticsMember.class));
                break;
            case R.id.member_Recent_btn :
                startActivity(new Intent(this, StaticsMemberRecent.class));
                break;
        }
    }

}
