package handong.cconma.cconmaadmin.Statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import handong.cconma.cconmaadmin.R;

public class StaticsTrade extends Activity {
    private CombinedChart hourlyChart;
    private BarChart dailyChart;
    private LineChart weeklyChart;
    private LineChart monthlyChart;
    private boolean mode = false;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics_trade);

        backPressCloseHandler = new BackPressCloseHandler(this);

        StaticsTradeManager manager = new StaticsTradeManager(this);

        hourlyChart = (CombinedChart) findViewById(R.id.trade_hourly_combineChart);
        dailyChart = (BarChart)findViewById(R.id.trade_daily_barChart);
        weeklyChart = (LineChart) findViewById(R.id.trade_weekly_lineChart);
        monthlyChart = (LineChart) findViewById(R.id.trade_monthly_lineChart);

        hourlyChart.setDescription("");
        dailyChart.setDescription("");
        weeklyChart.setDescription("");
        monthlyChart.setDescription("");

        hourlyChart.setDrawOrder(new DrawOrder[]{DrawOrder.BAR, DrawOrder.LINE});
        hourlyChart.getLegend().setFormSize(5f);
        hourlyChart.getLegend().setFormToTextSpace(2);
        hourlyChart.getLegend().setTextSize(7f);
        hourlyChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        dailyChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        XAxis xAxisH = hourlyChart.getXAxis();
        xAxisH.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisH.setDrawGridLines(false);
        xAxisH.setLabelsToSkip(0);

        XAxis xAxisD = dailyChart.getXAxis();
        xAxisD.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxisW = weeklyChart.getXAxis();
        xAxisW.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisW.setDrawGridLines(false);

        XAxis xAxisM = monthlyChart.getXAxis();
        xAxisM.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisM.setDrawGridLines(false);

        YAxis rightAxisH = hourlyChart.getAxisRight();
        rightAxisH.setEnabled(false);

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
        //  leftAxisM.setDrawGridLines(false);

        hourlyChart.setData(manager.hourlyChartSetting());
        dailyChart.setData(manager.dailyChartSetting());
        dailyChart.invalidate();
        weeklyChart.setData(manager.weeklyChartSetting());
        leftAxisW.addLimitLine(manager.weeklkyAVG());
        monthlyChart.setData(manager.monthlyChartSetting());
        leftAxisM.addLimitLine(manager.monthlyAVG());

        (findViewById(R.id.trade_hourly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    hourlyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    dailyChart.setVisibility(View.GONE);
                    weeklyChart.setVisibility(View.GONE);
                    monthlyChart.setVisibility(View.GONE);
                    (findViewById(R.id.trade_hourly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_monthly_rl)).setVisibility(View.GONE);
                }
            }
        });

        (findViewById(R.id.trade_daily_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    dailyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    hourlyChart.setVisibility(View.GONE);
                    weeklyChart.setVisibility(View.GONE);
                    monthlyChart.setVisibility(View.GONE);
                    (findViewById(R.id.trade_hourly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_monthly_rl)).setVisibility(View.GONE);
                }
            }
        });

        (findViewById(R.id.trade_weekly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    weeklyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    hourlyChart.setVisibility(View.GONE);
                    dailyChart.setVisibility(View.GONE);
                    monthlyChart.setVisibility(View.GONE);
                    (findViewById(R.id.trade_hourly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_monthly_rl)).setVisibility(View.GONE);
                }
            }
        });

        (findViewById(R.id.trade_monthly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    monthlyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    hourlyChart.setVisibility(View.GONE);
                    dailyChart.setVisibility(View.GONE);
                    weeklyChart.setVisibility(View.GONE);
                    (findViewById(R.id.trade_hourly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.trade_monthly_rl)).setVisibility(View.GONE);
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

