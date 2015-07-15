package handong.cconma.cconmaadmin.etc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import handong.cconma.cconmaadmin.R;
/**
 * Created by eundi on 15. 7. 15..
 */
public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar;

    LinearLayout layout_setting_items;

    Switch switch_push;
    Switch switch_push_board;
    Switch switch_push_happyrice;
    Switch switch_push_qna;
    Switch switch_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        layout_setting_items = (LinearLayout)findViewById(R.id.layout_setting_items);

        switch_push = (Switch)findViewById(R.id.switch_push);
        switch_push_board = (Switch)findViewById(R.id.switch_push_board);
        switch_push_happyrice = (Switch)findViewById(R.id.switch_push_happyrice);
        switch_push_qna = (Switch)findViewById(R.id.switch_push_qna);
        switch_icon = (Switch)findViewById(R.id.switch_icon);

        switch_push.setOnCheckedChangeListener(switchCheck);
        switch_push_board.setOnCheckedChangeListener(switchCheck);
        switch_push_happyrice.setOnCheckedChangeListener(switchCheck);
        switch_push_qna.setOnCheckedChangeListener(switchCheck);
        switch_icon.setOnCheckedChangeListener(switchCheck);
    }

    CompoundButton.OnCheckedChangeListener switchCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()){
                case R.id.switch_push:
                    if(isChecked){
                        layout_setting_items.setVisibility(View.VISIBLE);
                    }else{
                        layout_setting_items.setVisibility(View.GONE);
                    }
                    break;
                case R.id.switch_push_board:
                    if(isChecked){
                        Toast.makeText(getApplicationContext(), "게시판 알림 켜기", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "게시판 알림 끄기", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.switch_push_happyrice:
                    if(isChecked){
                        Toast.makeText(getApplicationContext(), "행밥 알림 켜기", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "행밥 알림 끄기", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.switch_push_qna:
                    if(isChecked){
                        Toast.makeText(getApplicationContext(), "1:1문의 게시판 알림 켜기", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "1:1문의 게시판 알림 끄기", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.switch_icon:
                    if(isChecked){
                        Toast.makeText(getApplicationContext(), "알림 바로가기 켜기", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "알림 바로가기 끄기", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
