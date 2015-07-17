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
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;

import net.htmlparser.jericho.Source;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.HTTPConnector;
import handong.cconma.cconmaadmin.etc.JSONResponse;

public class StaticsOrderH extends Activity implements JSONResponse {
    private LineChart pcChart;
    private LineChart mobChart;
    private BackPressCloseHandler backPressCloseHandler;
    private StaticsCommonSetting setting;
    private boolean mode = false;
    private StaticsOrderHManager manager;
    private HashMap<View, ViewGroup.LayoutParams> views = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_order_h);

        backPressCloseHandler = new BackPressCloseHandler(this);
        setting = new StaticsCommonSetting();
        manager = new StaticsOrderHManager(this);

        StaticsMarkerView mvPc = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);
        StaticsMarkerView mvMob = new StaticsMarkerView(this, R.layout.statics_marker_view_layout);

        RelativeLayout pTag = (RelativeLayout) findViewById(R.id.order_hourly_pc_rl);
        RelativeLayout mTag = (RelativeLayout) findViewById(R.id.order_hourly_mobile_rl);

        pcChart = (LineChart) findViewById(R.id.order_hourly_pc_chart);
        mobChart = (LineChart) findViewById(R.id.order_hourly_mobile_chart);

        mvPc.attachChart(pcChart, "건");
        mvMob.attachChart(mobChart, "건");

        setting.commonSetting(pcChart);
        setting.commonSetting(mobChart);
//        pcChart.setDescription("");
//        mobChart.setDescription("");

        pcChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pcChart.getLegend().setTextSize(7f);
        mobChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        mobChart.getLegend().setTextSize(7f);

        pcChart.setMarkerView(mvPc);
        mobChart.setMarkerView(mvMob);

        views.put(pTag, pTag.getLayoutParams());
        views.put(pcChart, pcChart.getLayoutParams());
        views.put(mTag, mTag.getLayoutParams());
        views.put(mobChart, mobChart.getLayoutParams());

        HTTPConnector hc = new HTTPConnector(this);
        hc.setProgressMessage("차트를 그리고 있습니다.");
        hc.execute(
                "http://api.androidhive.info/contacts",
                "http://api.androidhive.info/contacts"
        );

        // scaling can now only be done on x- and y-axis separately
        //       pcChart.setPinchZoom(false);
        //       mobChart.setPinchZoom(false);

        //       pcChart.setDrawGridBackground(true);
        //       mobChart.setDrawGridBackground(true);

 /*       XAxis xAxis = pcChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelsToSkip(0);
        xAxis.setDrawGridLines(false);

        XAxis xAxis2 = mobChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setLabelsToSkip(0);
        xAxis2.setDrawGridLines(false);
*/
        //pcChart.getAxisLeft().setDrawGridLines(false);
//        mobChart.getAxisLeft().setDrawGridLines(false);

//        pcChart.getAxisRight().setEnabled(false);
//        mobChart.getAxisRight().setEnabled(false);

//        pcChart.getLegend().setEnabled(true);
//        mobChart.getLegend().setEnabled(true);

//        pcChart.setDoubleTapToZoomEnabled(false);
//        mobChart.setDoubleTapToZoomEnabled(false);

        (findViewById(R.id.order_hourly_pc_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    gone();
                    setting.zoomSetting(pcChart);
                  /*  pcChart.setScaleEnabled(true);
                    pcChart.setPinchZoom(true);*/
                }
            }
        });

        (findViewById(R.id.order_hourly_mobile_zoom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    gone();
                    setting.zoomSetting(mobChart);
                    //mobChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                }
            }
        });
    }

    public void refresh() {
        pcChart.invalidate();
        mobChart.invalidate();
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
        pcChart.setData(manager.setting(output.get(i++)));
        mobChart.setData(manager.setting(output.get(i++)));
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
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
                mode = false;
                visible();
            } else if (mode && config.orientation == Configuration.ORIENTATION_PORTRAIT) {// 세로
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
            pd = ProgressDialog.show(StaticsOrderH.this, "", "차트를 그리고 있습니다.", true, true);
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

                pcChart.setData(manager.setting());
                mobChart.setData(manager.setting());

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(StaticsOrderH.this);
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
