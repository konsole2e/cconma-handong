package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import handong.cconma.cconmaadmin.R;

public class StaticsMember extends Activity {
    //Combine Chart private CombinedChart dailyChart;
    private BarChart dailyChart;
    private LineChart weeklyChart;
    private LineChart monthlyChart;
    private boolean mode = false;
    private BackPressCloseHandler backPressCloseHandler;
    private StaticsCommonSetting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_member);

        backPressCloseHandler = new BackPressCloseHandler(this);

        setting = new StaticsCommonSetting();

        StaticsMemberManager manager = new StaticsMemberManager(this);

        StaticsMarkerView mvD = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvW = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvM = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);

//        dailyChart = (CombinedChart) findViewById(R.id.member_daily_combineChart);
        dailyChart = (BarChart)findViewById(R.id.member_daily_barChart);
        weeklyChart = (LineChart) findViewById(R.id.member_weekly_lineChart);
        monthlyChart = (LineChart) findViewById(R.id.member_monthly_lineChart);

        setting.commonSetting(dailyChart);
        setting.commonSetting(weeklyChart);
        setting.commonSetting(monthlyChart);

        setting.commonSetting(dailyChart);
        setting.commonSetting(weeklyChart);
        setting.commonSetting(monthlyChart);

        mvD.attachChart(dailyChart, "명");
        mvW.attachChart(weeklyChart, "명");
        mvM.attachChart(monthlyChart, "명");

        dailyChart.setMarkerView(mvD);
        weeklyChart.setMarkerView(mvW);
        monthlyChart.setMarkerView(mvM);

//        dailyChart.setDrawOrder(new DrawOrder[]{  DrawOrder.LINE, DrawOrder.BAR});

     /*   dailyChart.setDescription("");
        dailyChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        weeklyChart.setDescription("");
        monthlyChart.setDescription("");

        XAxis xAxisD = dailyChart.getXAxis();
        xAxisD.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxisW = weeklyChart.getXAxis();
        xAxisW.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisW.setDrawGridLines(false);

        XAxis xAxisM = monthlyChart.getXAxis();
        xAxisM.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisM.setDrawGridLines(false);
        xAxisM.setLabelsToSkip(0);

        YAxis rightAxisD = dailyChart.getAxisRight();
        rightAxisD.setEnabled(false);

        YAxis leftAxisD = dailyChart.getAxisLeft();
        leftAxisD.setDrawGridLines(false);

        YAxis rightAxisW = weeklyChart.getAxisRight();
        rightAxisW.setEnabled(false);

        YAxis leftAxisW = weeklyChart.getAxisLeft();
    //    leftAxisW.setDrawGridLines(false);

        YAxis rightAxisM = monthlyChart.getAxisRight();
        rightAxisM.setEnabled(false);

        YAxis leftAxisM = monthlyChart.getAxisLeft();
      //  leftAxisM.setDrawGridLines(false);*/

        dailyChart.setData(manager.dailyChartSetting());
        dailyChart.invalidate();
        weeklyChart.setData(manager.weeklyChartSetting());
        weeklyChart.getAxisLeft().addLimitLine(manager.weeklkyAVG());
        monthlyChart.setData(manager.monthlyChartSetting());
        monthlyChart.getAxisLeft().addLimitLine(manager.monthlyAVG());

        (findViewById(R.id.member_daily_zoom)).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mode == false) {
                     mode = true;
                     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    // dailyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                     setting.zoomSetting(dailyChart);
                     weeklyChart.setVisibility(View.GONE);
                     monthlyChart.setVisibility(View.GONE);
                     (findViewById(R.id.member_daily_rl)).setVisibility(View.GONE);
                     (findViewById(R.id.member_weekly_rl)).setVisibility(View.GONE);
                     (findViewById(R.id.member_monthly_rl)).setVisibility(View.GONE);
                 }
             }
         });

        (findViewById(R.id.member_weekly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    //weeklyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    setting.zoomSetting(weeklyChart);
                    dailyChart.setVisibility(View.GONE);
                    monthlyChart.setVisibility(View.GONE);
                    (findViewById(R.id.member_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.member_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.member_monthly_rl)).setVisibility(View.GONE);
                }
            }
        });

        (findViewById(R.id.member_monthly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    //monthlyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    setting.zoomSetting(monthlyChart);
                    dailyChart.setVisibility(View.GONE);
                    weeklyChart.setVisibility(View.GONE);
                    (findViewById(R.id.member_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.member_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.member_monthly_rl)).setVisibility(View.GONE);
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

