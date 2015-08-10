package handong.cconma.cconmaadmin.board;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;
import handong.cconma.cconmaadmin.mainpage.AdminApplication;


/**
 * Created by Young Bin Kim on 2015-07-09.
 */
public class BoardMarkedActivity extends AppCompatActivity {
    private Toolbar toolbar;

    int page = 1;
    String mem_no;
    ListView list_board_marked;
    BoardAdapter adapter_marked;

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

        /* 데이터 넣기 */

        jsonParser();

        list_board_marked.setFocusable(false);

        list_board_marked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BoardMarkedActivity.this, BoardViewActivity.class);
                i.putExtra("board_no", ((BoardData)adapter_marked.getItem(position)).board_no);
                i.putExtra("boardarticle_no", ((BoardData)adapter_marked.getItem(position)).boardarticle_no);

                BoardData data = (BoardData)adapter_marked.getItem(position);
                i.putExtra("number", data.boardarticle_no);

                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        jsonParser();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:

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

    public void jsonParser(){

        try{
            JSONObject jason = new MainAsyncTask("http://www.cconma.com/admin/api/member/v1/"
                    + mem_no + "/scraped_article"
                    + "?page=" + page + "&n=50", "GET", "").execute().get();

            JSONArray jsonArray = jason.getJSONArray("articles");

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
        }catch(Exception e){

        }

        adapter_marked.notifyDataSetChanged();
        page++;
    }


}
