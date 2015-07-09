package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by eundi on 15. 7. 9..
 */
public class PushView extends Activity{

    ListView list_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        list_push = (ListView)findViewById(R.id.list_push);



    }
}
