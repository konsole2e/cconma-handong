package handong.cconma.cconmaadmin.push;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardViewActivity;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.http.HttpConnection;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by eundi on 15. 7. 9..
 */
public class PushView extends AppCompatActivity{
    private Toolbar toolbar;

    private CircularProgressBar circularProgressBar;

    ListView list_push;
    PushAdapter adapter_push;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        circularProgressBar = (CircularProgressBar)findViewById(R.id.progressbar_circular);

        adapter_push = new PushAdapter(this);
        list_push = (ListView)findViewById(R.id.list_push);
        list_push.setAdapter(adapter_push);

        list_push.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PushView.this, BoardViewActivity.class);
                intent.putExtra("board_no", adapter_push.push_list_data.get(position).board_no);
                intent.putExtra("boardarticle_no", adapter_push.push_list_data.get(position).boardarticle_no);
                intent.putExtra("from", "push");
                startActivity(intent);
            }
        });

        new PushAsync().execute();

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

    class PushAsync extends AsyncTask<String, Void, String>{

        BasicData basicData = BasicData.getInstance();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/push/v1/member/"
                    + basicData.getMem_no() + "/app_type/admincconma"
                    , "GET", "");
            String sResult = connection.init();

            try{
                JSONArray jsonArray = new JSONArray(sResult);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String event_name = jsonObject.getString("event_name");
                    String receiver_mem_no = jsonObject.getString("receiver_mem_no");
                    String title = jsonObject.getString("title");
                    String message = jsonObject.getString("message");
                    String url = jsonObject.getString("url");
                    String pushed_datetime = jsonObject.getString("pushed_datetime");

                    adapter_push.addItem(id, event_name, receiver_mem_no, title,
                            message, url, pushed_datetime);
                }
            }catch(Exception e){

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            circularProgressBar.setVisibility(View.GONE);
            adapter_push.notifyDataSetChanged();
        }
    }
}
