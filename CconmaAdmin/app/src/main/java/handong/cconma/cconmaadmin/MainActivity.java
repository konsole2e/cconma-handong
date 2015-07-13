package handong.cconma.cconmaadmin;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import handong.cconma.cconmaadmin.inquiry.InquryMember;
import handong.cconma.cconmaadmin.push.PushView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent("iconservice"));

        Button boardMainButton = (Button)findViewById(R.id.board_main_button);
        Button writeButton = (Button)findViewById(R.id.write_button);
        Button modifyButton = (Button)findViewById(R.id.modify_button);

        boardMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BoardCconma.class);

                startActivity(intent);
            }
        });
        writeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PushView.class);

                startActivity(intent);

            }

        });

        modifyButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BoardMarked.class);
                startActivity(intent);

            }

        });

    }
}