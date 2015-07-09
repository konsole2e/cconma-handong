package handong.cconma.cconmaadmin.Board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import handong.cconma.cconmaadmin.R;

/**
 * Created by eundi on 15. 7. 8..
 */
public class BoardMarked extends Activity{

    ListView list_board_marked;
    BoardAdapter adapter_marked;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_marked);

        list_board_marked = (ListView)findViewById(R.id.list_board_marked);
        adapter_marked = new BoardAdapter(this);
        list_board_marked.setAdapter(adapter_marked);
        adapter_marked.addItem("제목", "김은지", "전체알림", "2015/07/07", 3, true, true);
        adapter_marked.addItem("제목", "김은지", "전체알림", "2015/07/07", 2, false, true);
        adapter_marked.addItem("제목", "김은지", "전체알림", "2015/07/07", 1, true, false);
        adapter_marked.addItem("제목", "김은지", "전체알림", "2015/07/07", 9, true, false);
        adapter_marked.addItem("제목", "김은지", "전체알림", "2015/07/07", 3, false, true);
        list_board_marked.setFocusable(false);
        list_board_marked.setOnItemClickListener(itemclick);

    }

    AdapterView.OnItemClickListener itemclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent(BoardMarked.this, BoardView.class);
            startActivity(i);
        }
    };
}
