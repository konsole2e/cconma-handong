package handong.cconma.cconmaadmin.board;

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


/**
 * Created by Young Bin Kim on 2015-07-09.
 */
public class BoardMarkedActivity extends AppCompatActivity {
    private Toolbar toolbar;

    ListView list_board_marked;
    BoardAdapter adapter_marked;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_marked);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        list_board_marked = (ListView)findViewById(R.id.list_board_marked);
        adapter_marked = new BoardAdapter(this);
        list_board_marked.setAdapter(adapter_marked);

        /* 데이터 넣기 */


        list_board_marked.setFocusable(false);

        list_board_marked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BoardMarkedActivity.this, BoardViewActivity.class);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
