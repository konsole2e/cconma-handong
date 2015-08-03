package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import handong.cconma.cconmaadmin.mainpage.MainActivity;
import handong.cconma.cconmaadmin.R;

/**
 * Created by ????? on 2015-07-07.
 */
public class BoardModifyActivity extends AppCompatActivity {
    private Toolbar toolbar;

    Context context;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();

        String number = this.getIntent().getStringExtra("number");

        /*
        * SELECT notice, title, content, file FROM board_database WHERE number=number
        * */
        boolean noticeCheck = true;
        ((CheckBox)findViewById(R.id.checkNotice)).setChecked(noticeCheck);
        EditText title = (EditText)findViewById(R.id.title);
        title.setText("load the title of "+number+"th text");
        EditText content = (EditText)findViewById(R.id.content);
        content.setText("load the content of "+number+"th text\nthis activity is reusing the layout of write\nthe number of the text is passed by intent\nand load all data of the text at this activity");

        String[] conditions = getResources().getStringArray(R.array.write_notice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, conditions);
        Spinner noticeSpinner = (Spinner)findViewById(R.id.notice);
        noticeSpinner.setAdapter(adapter);

        noticeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedItem = (TextView) view;

                if (!selectedItem.getText().equals("알림 조건")) {
                    Button testButton = new Button(getApplicationContext());
                    testButton.setText(selectedItem.getText());
                    ((LinearLayout) findViewById(R.id.selectedList)).addView(testButton);
                    testButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((LinearLayout) findViewById(R.id.selectedList)).removeView(v);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }
}