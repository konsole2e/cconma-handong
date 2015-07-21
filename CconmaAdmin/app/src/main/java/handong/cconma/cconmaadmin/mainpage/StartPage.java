package handong.cconma.cconmaadmin.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.LoginWebView;
import handong.cconma.cconmaadmin.etc.MyWebView;

/**
 * Created by Young Bin Kim on 2015-07-15.
 */
public class StartPage extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra("URL", "http://www.cconma.com/mobile/auth/index.pmv?path=http%3A%2F%2Fwww.cconma.com%2Fmobile%2Findex.pmv");
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.stopbutton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
