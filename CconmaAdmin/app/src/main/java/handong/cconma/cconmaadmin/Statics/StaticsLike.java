package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.HTTPConnector;
import handong.cconma.cconmaadmin.etc.JSONResponse;

public class StaticsLike extends Activity implements JSONResponse {
    //Combine Chart private CombinedChart dailyChart;
    private View dailyChart;
    private View weeklyChart;
    private View monthlyChart;
    private LineData data;
    private boolean mode = false;
    private BackPressCloseHandler backPressCloseHandler;
    private StaticsCommonSetting setting;
    private StaticsLikeManager manager;
    private HashMap<View, ViewGroup.LayoutParams> views = new HashMap<>();
    private ArrayList<JSONObject> result = null;

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
        RelativeLayout dTag = (RelativeLayout) findViewById(R.id.like_daily_rl);
        RelativeLayout wTag = (RelativeLayout) findViewById(R.id.like_weekly_rl);
        RelativeLayout mTag = (RelativeLayout) findViewById(R.id.like_monthly_rl);
        dailyChart = (BarChart) findViewById(R.id.like_daily_barChart);
        weeklyChart = (LineChart) findViewById(R.id.like_weekly_lineChart);
        monthlyChart = (LineChart) findViewById(R.id.like_monthly_lineChart);

        views.put(dTag, dTag.getLayoutParams());
        views.put(dailyChart, dailyChart.getLayoutParams());
        views.put(wTag, wTag.getLayoutParams());
        views.put(weeklyChart, weeklyChart.getLayoutParams());
        views.put(mTag, mTag.getLayoutParams());
        views.put(monthlyChart, monthlyChart.getLayoutParams());

//        dailyChart.setDrawOrder(new DrawOrder[]{  DrawOrder.LINE, DrawOrder.BAR});

        setting.commonSetting((BarChart) dailyChart);
        setting.commonSetting((LineChart) weeklyChart);
        setting.commonSetting((LineChart) monthlyChart);

        mvD.attachChart((BarChart) dailyChart, "", "명", "", 9);
        mvW.attachChart((LineChart) weeklyChart, "", "명", "", 9);
        mvM.attachChart((LineChart) monthlyChart, "", "명", "", 9);

        ((BarChart) dailyChart).setMarkerView(mvD);
        ((LineChart) weeklyChart).setMarkerView(mvW);
        ((LineChart) monthlyChart).setMarkerView(mvM);

        HTTPConnector hc = new HTTPConnector(this);
        hc.setProgressMessage("차트를 그리고 있습니다.");
        hc.execute(
                "http://api.geonames.org/citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demo",
                "http://api.geonames.org/citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demo",
                "http://api.geonames.org/citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demo"
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
                    gone();
                    setting.zoomSetting((BarChart) dailyChart);
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
                    gone();
                    setting.zoomSetting((LineChart) weeklyChart);
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
                    gone();
                    setting.zoomSetting((LineChart) monthlyChart);
                }
            }
        });
    }

    public void refresh() {
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
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void processFinish(ArrayList<JSONObject> output) {

        int i = 0;
        ((BarChart) dailyChart).setData(manager.dailyChartSetting(output.get(i++)));
        ((LineChart) weeklyChart).setData(manager.weeklyChartSetting(output.get(i)));
        ((LineChart) weeklyChart).getAxisLeft().addLimitLine(manager.weeklkyAVG(output.get(i++)));
        ((LineChart) monthlyChart).setData(manager.monthlyChartSetting(output.get(i)));
        ((LineChart) monthlyChart).getAxisLeft().addLimitLine(manager.monthlyAVG(output.get(i++)));
        refresh();
        return;
    }

    public class BackPressCloseHandler {
        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            Configuration config = getResources().getConfiguration();
            if (mode && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {// 가로
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
                mode = false;
                visible();
              /*  dailyChart.setVisibility(View.VISIBLE);
                weeklyChart.setVisibility(View.VISIBLE);
                monthlyChart.setVisibility(View.VISIBLE);
                (findViewById(R.id.like_daily_rl)).setVisibility(View.VISIBLE);
                (findViewById(R.id.like_weekly_rl)).setVisibility(View.VISIBLE);
                (findViewById(R.id.like_monthly_rl)).setVisibility(View.VISIBLE);*/
            } else if (mode && config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
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

