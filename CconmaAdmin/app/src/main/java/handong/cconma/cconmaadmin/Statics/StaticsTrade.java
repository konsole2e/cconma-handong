package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.HTTPConnector;
import handong.cconma.cconmaadmin.etc.JSONResponse;

public class StaticsTrade extends Activity implements JSONResponse {
    private CombinedChart hourlyChart;
    private BarChart dailyChart;
    private LineChart weeklyChart;
    private LineChart monthlyChart;
    private boolean mode = false;
    private BackPressCloseHandler backPressCloseHandler;
    private StaticsCommonSetting setting;
    private StaticsTradeManager manager;
    private HashMap<View, ViewGroup.LayoutParams> views = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_trade);

        backPressCloseHandler = new BackPressCloseHandler(this);
        setting = new StaticsCommonSetting();

        manager = new StaticsTradeManager(this);

        StaticsMarkerView mvH = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvD = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvW = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvM = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);

        RelativeLayout hTag = (RelativeLayout)findViewById(R.id.trade_hourly_rl);
        RelativeLayout dTag = (RelativeLayout)findViewById(R.id.trade_daily_rl);
        RelativeLayout wTag = (RelativeLayout)findViewById(R.id.trade_weekly_rl);
        RelativeLayout mTag = (RelativeLayout)findViewById(R.id.trade_monthly_rl);

        hourlyChart = (CombinedChart) findViewById(R.id.trade_hourly_combineChart);
        dailyChart = (BarChart) findViewById(R.id.trade_daily_barChart);
        weeklyChart = (LineChart) findViewById(R.id.trade_weekly_lineChart);
        monthlyChart = (LineChart) findViewById(R.id.trade_monthly_lineChart);

        setting.commonSetting(hourlyChart);
        setting.commonSetting(dailyChart);
        setting.commonSetting(weeklyChart);
        setting.commonSetting(monthlyChart);

        mvH.attachChart(hourlyChart, "", "원", "", 9);
        mvD.attachChart(dailyChart,"", "원","",9);
        mvW.attachChart(weeklyChart,"", "원","",9);
        mvM.attachChart(monthlyChart,"", "원","",9);

        hourlyChart.setMarkerView(mvH);
        dailyChart.setMarkerView(mvD);
        weeklyChart.setMarkerView(mvW);
        monthlyChart.setMarkerView(mvM);

        views.put(hTag, hTag.getLayoutParams());
        views.put(hourlyChart, hourlyChart.getLayoutParams());
        views.put(dTag, dTag.getLayoutParams());
        views.put(dailyChart, dailyChart.getLayoutParams());
        views.put(wTag, wTag.getLayoutParams());
        views.put(weeklyChart, weeklyChart.getLayoutParams());
        views.put(mTag, mTag.getLayoutParams());
        views.put(monthlyChart, monthlyChart.getLayoutParams());

        HTTPConnector hc = new HTTPConnector(this);
        hc.setProgressMessage("차트를 그리고 있습니다.");
        hc.execute(
                "http://api.androidhive.info/contacts",
                "http://api.androidhive.info/contacts",
                "http://api.androidhive.info/contacts",
                "http://api.androidhive.info/contacts"
        );

//        hourlyChart.setDescription("");
        //       dailyChart.setDescription("");
//        weeklyChart.setDescription("");
//        monthlyChart.setDescription("");

//        hourlyChart.setDrawOrder(new DrawOrder[]{DrawOrder.BAR, DrawOrder.LINE});
/*
        hourlyChart.getLegend().setFormSize(5f);
        hourlyChart.getLegend().setFormToTextSpace(2);
        hourlyChart.getLegend().setTextSize(7f);
        hourlyChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
*/

//        dailyChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

/*        XAxis xAxisH = hourlyChart.getXAxis();
        xAxisH.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisH.setDrawGridLines(false);
        xAxisH.setLabelsToSkip(0);*/

      /*  XAxis xAxisD = dailyChart.getXAxis();
        xAxisD.setPosition(XAxis.XAxisPosition.BOTTOM);*/

/*        XAxis xAxisW = weeklyChart.getXAxis();
        xAxisW.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisW.setDrawGridLines(false);

        XAxis xAxisM = monthlyChart.getXAxis();
        xAxisM.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisM.setDrawGridLines(false);*/

  /*      YAxis rightAxisH = hourlyChart.getAxisRight();
        rightAxisH.setEnabled(false);*/

/*        YAxis rightAxisD = dailyChart.getAxisRight();
        rightAxisD.setEnabled(false);*/

/*        YAxis leftAxisD = dailyChart.getAxisLeft();
        leftAxisD.setDrawGridLines(false);*/

  /*      YAxis rightAxisW = weeklyChart.getAxisRight();
        rightAxisW.setEnabled(false);*/

/*        YAxis leftAxisW = weeklyChart.getAxisLeft();
        //    leftAxisW.setDrawGridLines(false);

       *//* YAxis rightAxisM = monthlyChart.getAxisRight();
        rightAxisM.setEnabled(false);*//*

        YAxis leftAxisM = monthlyChart.getAxisLeft();
        //  leftAxisM.setDrawGridLines(false);*/


        (findViewById(R.id.trade_hourly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    //hourlyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    setting.zoomSetting(hourlyChart);
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
                    //dailyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    setting.zoomSetting(dailyChart);
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
                    // weeklyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    gone();
                    setting.zoomSetting(weeklyChart);
                }
            }
        });

        (findViewById(R.id.trade_monthly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    //   monthlyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    gone();
                    setting.zoomSetting(monthlyChart);
                }
            }
        });

    }

    public void refresh() {
        hourlyChart.invalidate();
        dailyChart.invalidate();
        weeklyChart.invalidate();
        monthlyChart.invalidate();
        return;
    }

    public void visible() {
        for (Map.Entry<View, ViewGroup.LayoutParams> e : views.entrySet()) {
            View v = e.getKey();
            v.setVisibility(View.VISIBLE);
            v.setLayoutParams(e.getValue());
        }
    }

    public void gone() {
        for (Map.Entry<View, ViewGroup.LayoutParams> e : views.entrySet()) {
            View v = e.getKey();
            v.setVisibility(View.GONE);
        }
    }

    @Override
    public void processFinish(ArrayList<JSONObject> output) {
        int i = 0;
        hourlyChart.setData(manager.hourlyChartSetting(output.get(i++)));
        dailyChart.setData(manager.dailyChartSetting(output.get(i++)));
        weeklyChart.setData(manager.weeklyChartSetting(output.get(i)));
        weeklyChart.getAxisLeft().addLimitLine(manager.weeklkyAVG(output.get(i++)));
        monthlyChart.setData(manager.monthlyChartSetting(output.get(i)));
        monthlyChart.getAxisLeft().addLimitLine(manager.monthlyAVG(output.get(i++)));
        refresh();
        return;
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
                visible();
            } else if (mode && config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 세로전환
                mode = false;
                visible();
            } else {
                activity.finish();
            }
        }
    }
/*
    class ConnectToUrl extends AsyncTask<String, String, String> {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(StaticsTrade.this, "", "차트를 그리고 있습니다.", true, true);
        }

        @Override
        protected String doInBackground(String... str) {
            if (manager.getData(str)) {
                return "success";
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s != null) {

                hourlyChart.setData(manager.hourlyChartSetting());
                dailyChart.setData(manager.dailyChartSetting());
                weeklyChart.setData(manager.weeklyChartSetting());
                monthlyChart.setData(manager.monthlyChartSetting());

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(StaticsTrade.this);
                builder.setTitle("네트워크 오류");
                builder.setMessage("데이터를 읽어 올 수 없습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
    }*/
}

