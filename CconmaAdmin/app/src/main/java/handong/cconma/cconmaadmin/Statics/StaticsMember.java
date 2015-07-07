package handong.cconma.cconmaadmin.Statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;

import handong.cconma.cconmaadmin.R;

public class StaticsMember extends Activity {
    //Combine Chart private CombinedChart dailyChart;
    private BarChart dailyChart;
    private LineChart weeklyChart;
    private LineChart monthlyChart;
    private boolean mode = false;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics_member);

        backPressCloseHandler = new BackPressCloseHandler(this);

        StaticsMemberManager manager = new StaticsMemberManager(this);

//        dailyChart = (CombinedChart) findViewById(R.id.member_daily_combineChart);
        dailyChart = (BarChart)findViewById(R.id.member_daily_barChart);
        weeklyChart = (LineChart) findViewById(R.id.member_weekly_lineChart);
        monthlyChart = (LineChart) findViewById(R.id.member_monthly_lineChart);

//        dailyChart.setDrawOrder(new DrawOrder[]{  DrawOrder.LINE, DrawOrder.BAR});

        XAxis xAxisD = dailyChart.getXAxis();
        xAxisD.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxisW = weeklyChart.getXAxis();
        xAxisW.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxisM = monthlyChart.getXAxis();
        xAxisM.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rightAxisD = dailyChart.getAxisRight();
        rightAxisD.setEnabled(false);

        YAxis leftAxisD = dailyChart.getAxisLeft();
        leftAxisD.setDrawGridLines(false);

        YAxis leftAxisW = weeklyChart.getAxisLeft();
        leftAxisW.setDrawGridLines(false);

        YAxis rightAxisW = weeklyChart.getAxisRight();
        rightAxisW.setEnabled(false);

        YAxis leftAxisM = monthlyChart.getAxisLeft();

        YAxis rightAxisM = monthlyChart.getAxisRight();
        rightAxisM.setEnabled(false);

        dailyChart.setData(manager.dailyChartSetting());
        dailyChart.invalidate();
        weeklyChart.setData(manager.weeklyChartSetting());
        leftAxisW.addLimitLine(manager.weeklkyAVG());
        monthlyChart.setData(manager.monthlyChartSetting());
        leftAxisM.addLimitLine(manager.monthlyAVG());


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

