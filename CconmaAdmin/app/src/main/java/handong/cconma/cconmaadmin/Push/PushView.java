package handong.cconma.cconmaadmin.Push;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import handong.cconma.cconmaadmin.R;

/**
 * Created by eundi on 15. 7. 9..
 */
public class PushView extends Activity{

    ListView list_push;
    PushAdapter adapter_push;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

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

            }
        });
    }
}
