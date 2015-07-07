package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by √÷º≠¿≤ on 2015-07-07.
 */
public class BoardModify extends Activity {

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);

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

                if (!selectedItem.getText().equals("")) {
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
                Toast.makeText(getApplicationContext(), "modified", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoardModify.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // cancel button
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "canceled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoardModify.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
