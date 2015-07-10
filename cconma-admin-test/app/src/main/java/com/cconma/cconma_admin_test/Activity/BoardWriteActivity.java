package com.cconma.cconma_admin_test.Activity;

import android.app.Activity;
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

import com.cconma.cconma_test_actionbar.R;

/**
 * Created by on 2015-07-06.
 */
public class BoardWriteActivity extends AppCompatActivity {
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar haha = getSupportActionBar();
        haha.setDisplayHomeAsUpEnabled(true);

        // notice spinner
        String[] conditions = getResources().getStringArray(R.array.write_notice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, conditions);
        Spinner noticeSpinner = (Spinner)findViewById(R.id.notice);
        noticeSpinner.setAdapter(adapter);
        noticeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedItem = (TextView) view;
                if(!selectedItem.getText().equals("")) {
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // confirm button
        Button confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ArrayList<String> noticeList = new ArrayList<String>();
                // for noticeList.size -> noticeList.add(i);
                boolean notice = ((CheckBox) findViewById(R.id.checkNotice)).isChecked();
                String title = ((EditText) findViewById(R.id.title)).getText() + "";
                String content = ((EditText) findViewById(R.id.content)).getText() + "";
                // String fild = ((Button)findViewById(R.id.file)).getText()+""; // need to fix it...

                /*
                * INSERT notice, title, content, file INTO board_database
                * */
                Toast.makeText(getApplicationContext(), "confirmed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoardWriteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // cancel button
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "canceled", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}
