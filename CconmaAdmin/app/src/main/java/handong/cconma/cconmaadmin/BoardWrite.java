package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2015-07-06.
 */
public class BoardWrite extends Activity {

    String mFileName;
    ListView list_file;
    Context context = this;

    List<String> item = null;
    List<String> path = null;
    String root = Environment.getExternalStorageDirectory().getAbsolutePath();

    FrameLayout frame;

    Button btn_board_write_file;
    TextView text_pwd;
    TextView selected_file;

    String selectedFile="";
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);

        frame = (FrameLayout)findViewById(R.id.frame);
        text_pwd = (TextView)findViewById(R.id.text_pwd);
        selected_file = (TextView)findViewById(R.id.text_selected_file);
        list_file = (ListView)findViewById(R.id.list_file);


        getDirectory(root);
        list_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(path.get(position));

                if(file.isDirectory()){
                    if(file.canRead()) {
                        getDirectory(path.get(position));
                    }
                    else
                        Toast.makeText(context, "No file in this folder", Toast.LENGTH_SHORT).show();
                }else{
                    mFileName = file.getName();
                    selected_file.setText(mFileName);
                    Log.i("Board", "ext:" + mFileName.substring(mFileName.lastIndexOf('.') + 1, mFileName.length()));
                    selectedFile = file.getPath();
                    frame.setVisibility(View.GONE);
                }
            }
        });
        btn_board_write_file = (Button)findViewById(R.id.file);
        btn_board_write_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "file", Toast.LENGTH_SHORT).show();
                frame.setVisibility(View.VISIBLE);
            }
        });

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

                Log.d("board", selectedFile);

                Toast.makeText(getApplicationContext(), "confirmed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoardWrite.this, BoardView.class);
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

    public void getDirectory(String dirPath){

        item = new ArrayList<String>();
        path = new ArrayList<String>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if(!dirPath.equals(root)){
            item.add("../");
            path.add(root);
        }

        for(int i=0; i<files.length; i++){
            File ff = files[i];
            path.add(ff.getAbsolutePath());

            if(ff.isDirectory())
                item.add(ff.getName() + "/");
            else
                item.add(ff.getName());

        }
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,item);
        list_file.setAdapter(fileList);
    }
}
