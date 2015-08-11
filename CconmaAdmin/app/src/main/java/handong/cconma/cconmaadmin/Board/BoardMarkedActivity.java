package handong.cconma.cconmaadmin.board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.BasicData;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-09.
 */
public class BoardMarkedActivity extends AppCompatActivity {
    private Toolbar toolbar;

    int page = 1;
    String mem_no;
    ListView list_board_marked;
    BoardAdapter adapter_marked;

    private CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_marked);

        Log.d("scrap", "scrap_page");

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BasicData basicData = BasicData.getInstance();
        mem_no = basicData.getMem_no();

        list_board_marked = (ListView)findViewById(R.id.list_board_marked);
        adapter_marked = new BoardAdapter(this);
        list_board_marked.setAdapter(adapter_marked);

        circularProgressBar = (CircularProgressBar)findViewById(R.id.progressbar_circular);

        new MarkAsyncTask().execute();

        list_board_marked.setFocusable(false);

        list_board_marked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BoardMarkedActivity.this, BoardViewActivity.class);
                i.putExtra("board_no", ((BoardData)adapter_marked.getItem(position)).board_no);
                i.putExtra("boardarticle_no", ((BoardData)adapter_marked.getItem(position)).boardarticle_no);

                BoardData data = (BoardData)adapter_marked.getItem(position);
                i.putExtra("number", data.boardarticle_no);

                i.putExtra("from", "Mark");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                if(adapter_marked.getCheckCount()!=0) {
                    AlertDialog.Builder alert_build = new AlertDialog.Builder(this);
                    String alertMessage = adapter_marked.getCheckCount() + "개의 게시글을 즐겨찾기 해제 하시겠습니까?";
                    alert_build.setMessage(alertMessage).setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //adapter_marked.deleteMarks();
                                    new DelAsyncTask().execute();
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = alert_build.create();
                    alert.show();
                }else{
                    Toast.makeText(BoardMarkedActivity.this, "선택된 게시글이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mark, menu);

        return true;
    }

    class MarkAsyncTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/member/v1/"
                    + mem_no + "/scraped_article"
                    + "?page=" + page + "&n=50", "GET", "");
            String sResult = connection.init();

            try{
                JSONObject json = new JSONObject(sResult);
                JSONArray jsonArray = json.getJSONArray("articles");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String notice_type = jsonObject.getString("notice_type");
                    String board_no = jsonObject.getString("board_no");
                    String boardarticle_no = jsonObject.getString("boardarticle_no");
                    String name = jsonObject.getString("name");
                    String subject = jsonObject.getString("subject");
                    String mem_no = jsonObject.getString("mem_no");
                    String reg_date = jsonObject.getString("reg_date");
                    String ip = jsonObject.getString("ip");
                    String hit = jsonObject.getString("hit");
                    String board_short_name = jsonObject.getString("board_short_name");
                    String hash_tag = "";
                    JSONArray hashArr = jsonObject.getJSONArray("article_hash_tags");
                    HashMap hashMap = new HashMap();
                    if (hashArr.length() != 0) {
                        for (int j = 0; j < hashArr.length(); j++) {
                            JSONObject hashObj = hashArr.getJSONObject(j);
                            hashMap.put("hash_tag" + j, hashObj.getString("hash_tag"));
                            hashMap.put("hash_tag_type" + j, hashObj.getString("hash_tag_type"));
                        }
                    }
                    String comment_nicknames = jsonObject.getString("comment_nicknames");
                    JSONObject scrap = jsonObject.getJSONObject("scrap_info");
                    boolean board_marked = false;
                    if(scrap!=null)
                        board_marked = true;

                    adapter_marked.addItem(notice_type, board_no, boardarticle_no, name,
                            subject, mem_no, reg_date, ip, hit,
                            board_short_name, hashMap, comment_nicknames, board_marked);
                }
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            circularProgressBar.setVisibility(View.GONE);
            adapter_marked.notifyDataSetChanged();
            page++;
        }
    }

    class DelAsyncTask extends AsyncTask<String, Void, JSONObject>{

        BasicData basicData = BasicData.getInstance();
        HashMap hashMap = new HashMap();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            for(int i=0, index=0; i<adapter_marked.board_list_data.size(); i++){
                if(adapter_marked.board_list_data.get(i).check){
                    hashMap.put("board_no"+index, adapter_marked.board_list_data.get(i).board_no);
                    hashMap.put("boardarticle_no"+index, adapter_marked.board_list_data.get(i).boardarticle_no);
                    index++;
                }
            }
            for(int j=adapter_marked.board_list_data.size()-1; j>=0; j--){
                if(adapter_marked.board_list_data.get(j).check)
                    adapter_marked.board_list_data.remove(j);
            }

            adapter_marked.notifyDataSetChanged();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            for(int pos=0; pos<hashMap.size()/2; pos++){
                HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                        + hashMap.get("board_no"+pos)
                        + "/articles/" + hashMap.get("boardarticle_no"+pos)
                        + "/scraped_members/" + basicData.getMem_no()
                        , "PUT", "");
                connection.init();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }
}
