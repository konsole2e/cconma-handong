package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.mainpage.AdminApplication;


/**
 * Created by on 2015-07-06.
 */
public class BoardWriteActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Context context = this;

    Spinner spinner_notice;
    LinearLayout layout_write_notice;
    EditText edit_title;
    EditText edit_content;

    ArrayList<String> noticeArr;
    ArrayList<String> noticeTagArr;
    boolean[] noticeAdd;

    BasicData basicData;
    int board;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write_new);

        board = getIntent().getExtras().getInt("board", 12);

        Log.d("debugging", "accepted board no: " + board);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar haha = getSupportActionBar();
        haha.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spinner_notice = (Spinner)findViewById(R.id.spinner_notice);
        layout_write_notice = (LinearLayout)findViewById(R.id.layout_write_notice);
        edit_title = (EditText)findViewById(R.id.edit_board_title);
        edit_content = (EditText)findViewById(R.id.edit_board_content);

        noticeArr = new ArrayList<String>();
        noticeTagArr = new ArrayList<String>();

        // notice spinner
        basicData = BasicData.getInstance();
        noticeAdd = new boolean[basicData.getHashTagList().size()/2 + 1];
        noticeAdd[0] = true;
        noticeArr.add("알림");
        noticeTagArr.add("notice");
        for(int i=0; i<basicData.getHashTagList().size()/2; i++){
            if(basicData.getHashTagList().get("hash_tag_type"+i).equals("notice_team"))
                noticeArr.add("[" + basicData.getHashTagList().get("hash_tag"+i).toString() + "]");
            else
                noticeArr.add(basicData.getHashTagList().get("hash_tag"+i).toString());

            noticeTagArr.add(basicData.getHashTagList().get("hash_tag_type"+i).toString());
            noticeAdd[i+1]=false;
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_simple_dropdown_item_1line, noticeArr);
        spinner_notice.setAdapter(adapter);
        spinner_notice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = context.getResources();
                final TextView selectedItem = (TextView) view;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 10, 15, 20);

                if(noticeAdd[position] == false) {
                    noticeAdd[position] = true;
                    TextView textView = new TextView(getApplicationContext());

                    if (noticeTagArr.get(position).equals("notice_team")) {
                        Drawable d = res.getDrawable(R.drawable.notice_team);
                        textView.setBackgroundDrawable(d);
                        textView.setTextColor(Color.rgb(42, 117, 0));
                    } else {
                        Drawable d = res.getDrawable(R.drawable.notice_member);
                        textView.setBackgroundDrawable(d);
                        textView.setTextColor(Color.rgb(59, 89, 152));
                    }

                    textView.setTag(position);
                    textView.setTextSize(17);
                    textView.setText(" " + selectedItem.getText() + " ");
                    textView.setLayoutParams(lp);
                    textView.setGravity(Gravity.CENTER);
                    layout_write_notice.addView(textView);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_write_notice.removeView(v);
                            noticeAdd[Integer.parseInt(v.getTag().toString())] = false;
                        }


                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.write:
                if((!edit_title.getText().toString().equals("")) && (!edit_content.getText().toString().equals(""))) {
                    String tag = "";
                    for (int i = 0; i < noticeArr.size() - 1; i++) {
                        if (noticeAdd[i + 1]) {
                            tag = tag + "@" + basicData.getHashTagList().get("hash_tag" + i) + " ";
                        }
                    }

                    try {
                        String content = edit_content.getText().toString().replace("\n", "<br>");

                        String requestBody = "subject=" + String.valueOf(edit_title.getText())
                                + "&content=" + tag + "<br>" + content + "<br><br><br> - From App" + "&filename=" +
                                "" + "&filename2=" + "";

                        Log.d("debugging", requestBody);
                        Log.d("debugging", "async task: " + String.valueOf(board));
                        new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/" + board, "POST", requestBody).execute().get();
                    } catch (Exception e) {
                        Log.d("debugging", "Exception in BoardWriteActivity line 212 " + e.getMessage());
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                    AdminApplication.getInstance().setRefresh(true);
                    finish();
                }else{
                    Toast.makeText(context, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);

        return true;
    }

}
