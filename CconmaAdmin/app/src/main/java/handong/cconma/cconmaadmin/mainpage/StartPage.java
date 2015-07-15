package handong.cconma.cconmaadmin.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import handong.cconma.cconmaadmin.R;

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
                startActivity(new Intent(StartPage.this, MainActivity.class));
            }
        });
    }
}
