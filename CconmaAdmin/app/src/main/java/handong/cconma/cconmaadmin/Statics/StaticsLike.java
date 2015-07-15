package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONObject;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.HTTPConnector;
import handong.cconma.cconmaadmin.etc.JSONResponse;

public class StaticsLike extends Activity implements JSONResponse {
    //Combine Chart private CombinedChart dailyChart;
    private BarChart dailyChart;
    private LineChart weeklyChart;
    private LineChart monthlyChart;
    private boolean mode = false;
    private BackPressCloseHandler backPressCloseHandler;
    private StaticsCommonSetting setting;
    private StaticsLikeManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_like);

        backPressCloseHandler = new BackPressCloseHandler(this);
        setting = new StaticsCommonSetting();
        manager = new StaticsLikeManager(this);

        StaticsMarkerView mvD = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvW = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvM = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);

//        dailyChart = (CombinedChart) findViewById(R.id.member_daily_combineChart);
        dailyChart = (BarChart) findViewById(R.id.like_daily_barChart);
        weeklyChart = (LineChart) findViewById(R.id.like_weekly_lineChart);
        monthlyChart = (LineChart) findViewById(R.id.like_monthly_lineChart);

//        dailyChart.setDrawOrder(new DrawOrder[]{  DrawOrder.LINE, DrawOrder.BAR});

        setting.commonSetting(dailyChart);
        setting.commonSetting(weeklyChart);
        setting.commonSetting(monthlyChart);



        mvD.attachChart(dailyChart, "명");
        mvW.attachChart(weeklyChart, "명");
        mvM.attachChart(monthlyChart, "명");

        dailyChart.setMarkerView(mvD);
        weeklyChart.setMarkerView(mvW);
        monthlyChart.setMarkerView(mvM);

        HTTPConnector hc = new HTTPConnector(this);
        hc.setProgressMessage("차트를 그리고 있습니다.");
        hc.execute(
                "http://api.androidhive.info/contacts",
                "http://api.androidhive.info/contacts",
                "http://api.androidhive.info/contacts"
        );

/*       dailyChart.setDescription("");
        dailyChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        weeklyChart.setDescription("");
        monthlyChart.setDescription("");*/

    /*    XAxis xAxisD = dailyChart.getXAxis();
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

        (findViewById(R.id.like_daily_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    //dailyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    setting.zoomSetting(dailyChart);
                    weeklyChart.setVisibility(View.GONE);
                    monthlyChart.setVisibility(View.GONE);
                    (findViewById(R.id.like_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.like_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.like_monthly_rl)).setVisibility(View.GONE);
                }
            }
        });

        (findViewById(R.id.like_weekly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    // weeklyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    setting.zoomSetting(weeklyChart);
                    dailyChart.setVisibility(View.GONE);
                    monthlyChart.setVisibility(View.GONE);
                    (findViewById(R.id.like_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.like_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.like_monthly_rl)).setVisibility(View.GONE);
                }
            }
        });

        (findViewById(R.id.like_monthly_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    //monthlyChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    setting.zoomSetting(monthlyChart);
                    dailyChart.setVisibility(View.GONE);
                    weeklyChart.setVisibility(View.GONE);
                    (findViewById(R.id.like_daily_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.like_weekly_rl)).setVisibility(View.GONE);
                    (findViewById(R.id.like_monthly_rl)).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void processFinish(ArrayList<JSONObject> output) {
        int i = 0;
        dailyChart.setData(manager.dailyChartSetting(output.get(i++)));
        weeklyChart.setData(manager.weeklyChartSetting(output.get(i)));
        weeklyChart.getAxisLeft().addLimitLine(manager.weeklkyAVG(output.get(i++)));
        monthlyChart.setData(manager.monthlyChartSetting(output.get(i)));
        monthlyChart.getAxisLeft().addLimitLine(manager.monthlyAVG(output.get(i++)));
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
/*
    class ConnectToUrl extends AsyncTask<String, String, String> {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(StaticsLike.this, "", "차트를 그리고 있습니다.", true, true);
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
                dailyChart.setData(manager.dailyChartSetting());
                weeklyChart.setData(manager.weeklyChartSetting());
                monthlyChart.setData(manager.monthlyChartSetting());
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(StaticsLike.this);
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

