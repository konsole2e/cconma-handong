package handong.cconma.cconmaadmin.Statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;

import handong.cconma.cconmaadmin.R;

public class StaticsMemberRecent extends Activity {
    private BackPressCloseHandler backPressCloseHandler;
    private boolean mode = false;
    private LineChart pcChart;
    private LineChart mobChart;
    private StaticsCommonSetting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_member_recent);
        backPressCloseHandler = new BackPressCloseHandler(this);
        setting = new StaticsCommonSetting();

        StaticsMemberRecManager manager = new StaticsMemberRecManager(this);
        StaticsMarkerViewRecent mvPc = new StaticsMarkerViewRecent(this, R.layout.statics_marker_view_layout);
        StaticsMarkerViewRecent mvMob = new StaticsMarkerViewRecent(this, R.layout.statics_marker_view_layout);

        pcChart = (LineChart) findViewById(R.id.member_recent_pc_chart);
        mobChart = (LineChart) findViewById(R.id.member_recent_mobile_chart);

        setting.commonSetting(pcChart);
        setting.commonSetting(mobChart);

        mvPc.attachChart(pcChart, "명");
        mvMob.attachChart(mobChart, "명");

        pcChart.setMarkerView(mvPc);
        mobChart.setMarkerView(mvMob);


    /*    pcChart.setDescription("");
        mobChart.setDescription("");

        // scaling can now only be done on x- and y-axis separately
        pcChart.setPinchZoom(false);
        mobChart.setPinchZoom(false);

        pcChart.setDrawGridBackground(false);
        mobChart.setDrawGridBackground(false);

        XAxis xAxis = pcChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        XAxis xAxis2 = mobChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(false);

    *//*    pcChart.getAxisLeft().setDrawGridLines(false);
        mobChart.getAxisLeft().setDrawGridLines(false);*//*

        pcChart.getAxisRight().setEnabled(false);
        mobChart.getAxisRight().setEnabled(false);*/

/*
        pcChart.setDoubleTapToZoomEnabled(false);
        mobChart.setDoubleTapToZoomEnabled(false)*/;

        pcChart.setData(manager.setting("pc"));
        mobChart.setData(manager.setting("mobile"));

        (findViewById(R.id.member_recent_pc_zoom)).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mode == false) {
                     mode = true;
                     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    // pcChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                     setting.zoomSetting(pcChart);
                     mobChart.setVisibility(View.GONE);
                     (findViewById(R.id.member_recent_pc_rl)).setVisibility(View.GONE);
                     (findViewById(R.id.member_recent_mobile_rl)).setVisibility(View.GONE);
                 }
             }
         });

        (findViewById(R.id.member_recent_mobile_zoom)).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (mode == false) {
                      mode = true;
                      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                      //mobChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                      setting.zoomSetting(mobChart);
                      pcChart.setVisibility(View.GONE);
                      (findViewById(R.id.member_recent_pc_rl)).setVisibility(View.GONE);
                      (findViewById(R.id.member_recent_mobile_rl)).setVisibility(View.GONE);
                  }
              }
          });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public class BackPressCloseHandler {
        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            Configuration config = getResources().getConfiguration();
            if (mode && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {// 가로
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 가로전환
                mode = false;
                activity.recreate();
            } else if (mode && config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 세로전환
                mode = false;
                activity.recreate();
            } else {
                activity.finish();
            }
        }
    }
}
