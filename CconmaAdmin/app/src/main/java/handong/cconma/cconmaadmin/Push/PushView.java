package handong.cconma.cconmaadmin.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardViewActivity;

/**
 * Created by eundi on 15. 7. 9..
 */
public class PushView extends AppCompatActivity{
    private Toolbar toolbar;

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

        adapter_push = new PushAdapter(this);
        list_push = (ListView)findViewById(R.id.list_push);

        list_push.setAdapter(adapter_push);

        adapter_push.addBoardItem(0, "김은지", "[전체알림]", "안녕하세요", "반가워요", "2014/07/02 12:34:23");
        adapter_push.addStockItem(1, "당근", 30);
        adapter_push.addBoardItem(0, "이재성", "[IT개발팀]", "감사합니다", "ㄱㅅㄱㅅㄱㅅ", "2014/07/02 12:34:23");
        adapter_push.addItem(2);
        adapter_push.addStockItem(1, "호박", 20);
        adapter_push.addStockItem(1, "감자", 50);
        adapter_push.addBoardItem(0, "최서율", "[디자인팀]", "안드로이드 스튜디오", "디자인 디자인 디자인 디자인 디자인", "2014/07/02 12:34:23");
        adapter_push.addBoardItem(0, "김영빈", "[전체알림]", "속도가 너무 느리네요", "저도 느리네요", "2014/07/02 12:34:23");
        adapter_push.addItem(2);

        list_push.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = ((PushData)adapter_push.getItem(position)).type;
                if(type == 0){
                    Intent intent = new Intent(PushView.this, BoardViewActivity.class);
                    startActivity(intent);
                }else if(type == 2){
                    //1:1 문의 게시판으로 이동.
                }

            }
        });
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
}
