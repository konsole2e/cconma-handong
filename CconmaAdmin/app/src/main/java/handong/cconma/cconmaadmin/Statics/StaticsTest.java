package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.http.JSONResponse;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;

import static android.widget.RelativeLayout.*;

public class StaticsTest extends Activity implements JSONResponse {
    private BackPressCloseHandler backPressCloseHandler;
    private JSONObject result = null;
    private LinkedHashMap<View, ViewGroup.LayoutParams> charts;
    private ArrayList<RelativeLayout> btns;
    private LinearLayout ll;
    private StaticsCommonSetting setting;
    private TextView zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics_test);

        backPressCloseHandler = new BackPressCloseHandler(this);

        charts = new LinkedHashMap<>();
        btns = new ArrayList<>();
        setting = new StaticsCommonSetting();
        ll = (LinearLayout) findViewById(R.id.statics_test_ll);

        MainAsyncTask mat = new MainAsyncTask("http://local.cconma.com/admin/api/stat/v1/sample_chart", "GET", "", this);
        mat.setMessage("차트를 그리고 있습니다.");
        mat.execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { // 세로 전환시 발생
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 가로 전환시 발생
        }
    }

    public void parsingcCharts() {

        try {
            if (result == null) {
                Toast.makeText(getApplicationContext(), "받아온 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jArray = result.getJSONArray(StaticsVariables.chart);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    String category = obj.getString(StaticsVariables.category).toLowerCase();
                    generateChart(obj, category);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        return;
    }

    public void visible() {
        int i = 0;
        for (Map.Entry<View, ViewGroup.LayoutParams> e : charts.entrySet()) {
            View v = e.getKey();
            v.setVisibility(View.VISIBLE);
            v.setLayoutParams(e.getValue());
            btns.get(i++).setVisibility(View.VISIBLE);
            if (v instanceof PieChart) {
                setting.commonSetting((PieChart) v);
            } else if (v instanceof BarChart) {
                setting.commonSetting((BarChart) v);
            } else if (v instanceof CombinedChart) {
                setting.commonSetting((CombinedChart) v);
            } else if (v instanceof LineChart) {
                setting.commonSetting((LineChart) v);
            }
        }
    }

    public void gone() {
        int i = 0;
        for (Map.Entry<View, ViewGroup.LayoutParams> e : charts.entrySet()) {
            View v = e.getKey();
            v.setVisibility(View.GONE);
            v = btns.get(i++);
            v.setVisibility(View.GONE);
        }
    }

    public void generateChart(JSONObject json, String category) {
        View chart;
        if (category.equals(StaticsVariables.line)) {
            chart = new LineChart(this);
            StaticsLineManager manager = new StaticsLineManager(this);
            ((LineChart) chart).setData(manager.parsingLine(json, (LineChart) chart));
            setting.commonSetting((LineChart) chart);

        } else if (category.equals(StaticsVariables.bar)) {
            chart = new BarChart(this);
            StaticsBarManager manager = new StaticsBarManager(this);
            ((BarChart) chart).setData(manager.parsingBar(json, (BarChart) chart));
            setting.commonSetting((BarChart) chart);

        } else if (category.equals(StaticsVariables.combined)) {
            chart = new CombinedChart(this);
            StaticsCombinedManager manager = new StaticsCombinedManager(this);
            ((CombinedChart) chart).setData(manager.parsingCombined(json, (CombinedChart) chart));
            setting.commonSetting((CombinedChart) chart);

        } else if (category.equals(StaticsVariables.pie)) {
            chart = new PieChart(this);
            StaticsPieManager manager = new StaticsPieManager(this);
            ((PieChart) chart).setData(manager.parsingPie(json, (PieChart) chart));
            setting.commonSetting((PieChart) chart);

        } else {
            return;
        }

        chart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Integer.valueOf(json.optString(StaticsVariables.height, "200")), getResources().getDisplayMetrics())));
        generateZoomBtn(json.optString(StaticsVariables.description, "차트"));
        chart.invalidate();

        ll.addView(chart);
        charts.put(chart, chart.getLayoutParams());
    }

    public void generateZoomBtn(String desc) {
        int dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()); // 10dp
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams) rl.getLayoutParams()).setMargins(0, dpInPx, 0, 0);

        TextView title = new TextView(this);
        title.setText(desc);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        title.setPadding(dpInPx, dpInPx, dpInPx, dpInPx);
        title.setBackgroundColor(getResources().getColor(R.color.transparent));
        RelativeLayout.LayoutParams titleParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        titleParam.addRule(CENTER_VERTICAL);
        title.setLayoutParams(titleParam);
        rl.addView(title);

        ll.addView(rl);
        btns.add(rl);

    }

    @Override
    public void processFinish(JSONObject output) {
        result = output;
        Log.d("통계 fin", result.toString());
        parsingcCharts();
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
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {// 가로
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
            } else {
                activity.finish();
            }
        }
    }
}




