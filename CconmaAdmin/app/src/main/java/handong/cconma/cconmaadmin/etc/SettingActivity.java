package handong.cconma.cconmaadmin.etc;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;

/**
 * Created by eundi on 15. 7. 15..
 */
public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar;

    Switch switch_push;
    IntegratedSharedPreferences pref;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        pref = new IntegratedSharedPreferences(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        switch_push = (Switch) findViewById(R.id.switch_push);
        switch_push.setOnCheckedChangeListener(switchCheck);
        switch_push.setChecked(pref.getValue("PUSH", true));
    }

    CompoundButton.OnCheckedChangeListener switchCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.switch_push:
                    if (isChecked) {
                        pref.put("PUSH", true);
                    } else {
                        pref.put("PUSH", false);
                    }
                    break;
            }
        }
    };

}
