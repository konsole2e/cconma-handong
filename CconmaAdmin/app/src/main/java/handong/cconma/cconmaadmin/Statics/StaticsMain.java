package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import handong.cconma.cconmaadmin.R;

public class StaticsMain extends Activity implements View.OnClickListener {
    private Button orderH;
    private Button orderRcnt;
    private Button trade;
    private Button like;
    private Button member;
    private Button memberRcnt;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_main);

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

 //       new ConnectToUrl().execute();
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

    class ConnectToUrl extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... str) {
            if (getData()) {
                return "success";
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("네트워크 오류");
                builder.setMessage("데이터를 읽어 올 수 없습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
    }

    public boolean getData() {
        String url = "http://api.androidhive.info/contacts/";
        String line = null;
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            //con.setDoOutput(true);
            con.setDoInput(true);
            BufferedInputStream is;
            //BufferedOutputStream os;

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //os = new BufferedInputStream(con.getOutputStream());
                is = new BufferedInputStream(con.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                }
                //  os.close();
                is.close();
                reader.close();
            } else {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
