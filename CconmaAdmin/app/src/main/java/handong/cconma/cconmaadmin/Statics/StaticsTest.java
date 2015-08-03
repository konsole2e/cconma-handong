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
    private boolean mode = false;
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
        zoom = (TextView) findViewById(R.id.zoom_tv);

        TextView reload = (TextView) findViewById(R.id.reload_tv);
        reload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration config = getResources().getConfiguration();
                if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {// 세로
                    recreate();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 가로전환

                } else {
                    recreate();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); // 가로전환
                }
            }
        });

        zoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration config = getResources().getConfiguration();
                if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {// 세로
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); // 가로전환
                    zoom.setText("돌아가기");
      /*              gone();
                    if (chart instanceof PieChart) {
                        setting.zoomSetting((PieChart) chart);
                    } else if (chart instanceof BarChart) {
                        setting.zoomSetting((BarChart) chart);
                    } else if (chart instanceof CombinedChart) {
                        setting.zoomSetting((CombinedChart) chart);
                    } else if (chart instanceof LineChart) {
                        setting.zoomSetting((LineChart) chart);
                    }*/
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 가로전환
                    zoom.setText("크게보기");
                }
            }
        });

        MainAsyncTask mat = new MainAsyncTask("http://local.cconma.com/admin/api/stat/v1/sample_chart", "GET", "", this);
        mat.setMessage("차트를 그리고 있습니다.");
        mat.execute();
 //       Log.d("통계", result.toString());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { // 세로 전환시 발생
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 가로 전환시 발생
        }
    }

    public void downloadChart() {
        try {
            MainAsyncTask mat = new MainAsyncTask("http://local.cconma.com/admin/api/stat/v1/sample_chart", "GET", "", this);
            mat.setMessage("차트를 그리고 있습니다.");
            result = mat.execute().get();
            Log.d("통계", result.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //processFinish(new ArrayList<JSONObject>());
        parsingcCharts();
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

               /* if (category.equals("line")) {
                    generateLineChart(obj);
                } else if (category.equals("bar")) {
                    generateBarChart(obj);
                } else if (category.equals("combined")) {
                    generateCombinedChart(obj);
                } else if (category.equals("pie")) {
                    generatePieChart(obj);
                } else {
                    return;
                }*/
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

     /*   TextView zoom = new TextView(this);
        zoom.setText("크게보기 >");
        zoom.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        zoom.setPadding(dpInPx, dpInPx, dpInPx, dpInPx);
        zoom.setBackgroundColor(getResources().getColor(R.color.transparent));
        RelativeLayout.LayoutParams zoomParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        zoomParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        zoomParams.addRule(CENTER_VERTICAL);
        zoom.setLayoutParams(zoomParams);
        rl.addView(zoom);*/

        ll.addView(rl);
        btns.add(rl);
    /*    zoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); // 가로전환
                    gone();
                    if (chart instanceof PieChart) {
                        setting.zoomSetting((PieChart) chart);
                    } else if (chart instanceof BarChart) {
                        setting.zoomSetting((BarChart) chart);
                    } else if (chart instanceof CombinedChart) {
                        setting.zoomSetting((CombinedChart) chart);
                    } else if (chart instanceof LineChart) {
                        setting.zoomSetting((LineChart) chart);
                    }
                }
            }
        });*/
    }

/*   public void generateLineChart(JSONObject json) {
        LineChart lineChart = new LineChart(this);
        lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics())));
        generateZoomBtn(lineChart);

        ll.addView(lineChart);
        charts.put(lineChart, lineChart.getLayoutParams());
    }

    public void generateBarChart(JSONObject json) {
        BarChart barChart = new BarChart(this);
        barChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics())));

        generateZoomBtn(barChart);

        ll.addView(barChart);
        charts.put(barChart, barChart.getLayoutParams());
    }

    public void generateCombinedChart(JSONObject json) {
        CombinedChart combinedChart = new CombinedChart(this);
        combinedChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics())));

        generateZoomBtn(combinedChart);

        ll.addView(combinedChart);
        charts.put(combinedChart, combinedChart.getLayoutParams());
    }

    public void generatePieChart(JSONObject json) {
        PieChart pieChart = new PieChart(this);
        pieChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics())));

        generateZoomBtn(pieChart);

        ll.addView(pieChart);
        charts.put(pieChart, pieChart.getLayoutParams());
    }*/

    @Override
    public void processFinish(JSONObject output) {
        result = output;
        Log.d("통계 fin", result.toString());
/*
        try {
            result = new JSONObject("{\n" +
                    "  \"chart\": [\n" +
                    "    {\n" +
                    "      \"category\": \"line\",\n" +
                    "      \"chartBGColor\": \"#00ffffff\",\n" +
                    "      \"graphBGColor\": \"#ffffff\",\n" +
                    "      \"xAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"gap\": \"0\",\n" +
                    "        \"position\": \"BOTTOM\"\n" +
                    "      },\n" +
                    "      \"leftYAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"-10\",\n" +
                    "        \"max\": \"100\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"20\",\n" +
                    "        \"spaceBottom\": \"20\",\n" +
                    "        \"unit\": \"명\",\n" +
                    "        \"valueFormat\" : \"%,d\"\n" +
                    "      },\n" +
                    "      \"rightYAxis\": {\n" +
                    "        \"enabled\": \"false\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"0\",\n" +
                    "        \"max\": \"110\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"30\",\n" +
                    "        \"spaceBottom\": \"30\",\n" +
                    "        \"unit\": \"마리\",\n" +
                    "        \"valueFormat\" : \"%,d\"\n" +
                    "      },\n" +
                    "      \"tooltip\": {\n" +
                    "        \"leftSide\": \"index\",\n" +
                    "        \"skipZero\": \"bar\"\n" +
                    "      },\n" +
                    "      \"legend\": {\n" +
                    "        \"position\": \"BELOW_CHART_CENTER\",\n" +
                    "        \"form\": \"CIRCLE\",\n" +
                    "        \"formSize\": \"2\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"7\",\n" +
                    "        \"legendSpace\": \"1\"\n" +
                    "      },\n" +
                    "      \"data\": [\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\",\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\",\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\",\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"80\",\n" +
                    "            \"0\",\n" +
                    "            \"92\",\n" +
                    "            \"2\",\n" +
                    "            \"0\",\n" +
                    "            \"99\",\n" +
                    "            \"95\",\n" +
                    "            \"80\",\n" +
                    "            \"0\",\n" +
                    "            \"92\",\n" +
                    "            \"2\",\n" +
                    "            \"0\",\n" +
                    "            \"99\",\n" +
                    "            \"95\",\n" +
                    "            \"80\",\n" +
                    "            \"0\",\n" +
                    "            \"92\",\n" +
                    "            \"2\",\n" +
                    "            \"0\",\n" +
                    "            \"99\",\n" +
                    "            \"95\",\n" +
                    "            \"80\",\n" +
                    "            \"0\",\n" +
                    "            \"92\",\n" +
                    "            \"2\",\n" +
                    "            \"0\",\n" +
                    "            \"99\",\n" +
                    "            \"95\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트1\",\n" +
                    "          \"axisDepend\": \"LEFT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ff0000\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"비\",\n" +
                    "          \"valueFormat\" : \"%,d\",\n" +
                    "          \"color\": \"#ff0000\",\n" +
                    "          \"line_width\": \"2\",\n" +
                    "          \"line_circleSize\": \"4\",\n" +
                    "          \"line_circleColor\": \"#00b2ff\",\n" +
                    "          \"line_innerCircleColor\": \"#ffffff\",\n" +
                    "          \"line_dash\": {\n" +
                    "            \"line_length\": \"4\",\n" +
                    "            \"line_spaceLength\": \"6\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"83\",\n" +
                    "            \"84\",\n" +
                    "            \"94\",\n" +
                    "            \"0\",\n" +
                    "            \"6\",\n" +
                    "            \"0\",\n" +
                    "            \"97\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트2\",\n" +
                    "          \"axisDepend\": \"RIGHT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ffd200\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"눈\",\n" +
                    "          \"valueFormat\" : \"%,d\",\n" +
                    "          \"color\": \"#ffd200\",\n" +
                    "          \"line_width\": \"1\",\n" +
                    "          \"line_circleSize\": \"2\",\n" +
                    "          \"line_circleColor\": \"#ffffff\",\n" +
                    "          \"line_innerCircleColor\": \"#99d9ea\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"category\": \"line\",\n" +
                    "      \"chartBGColor\": \"#00ffffff\",\n" +
                    "      \"graphBGColor\": \"#ffffff\",\n" +
                    "      \"xAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"gap\": \"0\",\n" +
                    "        \"position\": \"BOTTOM\"\n" +
                    "      },\n" +
                    "      \"leftYAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"-10\",\n" +
                    "        \"max\": \"100\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"20\",\n" +
                    "        \"spaceBottom\": \"20\",\n" +
                    "        \"unit\": \"명\",\n" +
                    "        \"valueFormat\" : \"%,.1f\"\n" +
                    "      },\n" +
                    "      \"rightYAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"0\",\n" +
                    "        \"max\": \"110\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"30\",\n" +
                    "        \"spaceBottom\": \"30\",\n" +
                    "        \"unit\": \"마리\",\n" +
                    "        \"valueFormat\" : \"%,.1f\"\n" +
                    "      },\n" +
                    "      \"tooltip\": {\n" +
                    "        \"leftSide\": \"index\",\n" +
                    "        \"skipZero\": \"bar\"\n" +
                    "      },\n" +
                    "      \"legend\": {\n" +
                    "        \"position\": \"BELOW_CHART_CENTER\",\n" +
                    "        \"form\": \"CIRCLE\",\n" +
                    "        \"formSize\": \"2\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"7\",\n" +
                    "        \"legendSpace\": \"1\"\n" +
                    "      },\n" +
                    "      \"data\": [\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일1\",\n" +
                    "            \"월2\",\n" +
                    "            \"화3\",\n" +
                    "            \"수4\",\n" +
                    "            \"목5\",\n" +
                    "            \"금6\",\n" +
                    "            \"토7\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"100\",\n" +
                    "            \"0\",\n" +
                    "            \"0\",\n" +
                    "            \"5\",\n" +
                    "            \"80\",\n" +
                    "            \"84\",\n" +
                    "            \"5\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트3\",\n" +
                    "          \"axisDepend\": \"LEFT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ff0000\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"비\",\n" +
                    "          \"valueFormat\" : \"%,.2f\",\n" +
                    "          \"color\": \"#ff00ff\",\n" +
                    "          \"line_width\": \"2\",\n" +
                    "          \"line_circleSize\": \"4\",\n" +
                    "          \"line_circleColor\": \"#00b2ff\",\n" +
                    "          \"line_innerCircleColor\": \"#ffffff\",\n" +
                    "          \"line_dash\": {\n" +
                    "            \"line_length\": \"4\",\n" +
                    "            \"line_spaceLength\": \"6\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일2\",\n" +
                    "            \"월3\",\n" +
                    "            \"화3\",\n" +
                    "            \"수3\",\n" +
                    "            \"목2\",\n" +
                    "            \"금2\",\n" +
                    "            \"토2\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"3\",\n" +
                    "            \"4\",\n" +
                    "            \"4\",\n" +
                    "            \"95\",\n" +
                    "            \"96\",\n" +
                    "            \"99\",\n" +
                    "            \"1\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트4\",\n" +
                    "          \"axisDepend\": \"RIGHT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ffd200\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"눈\",\n" +
                    "          \"valueFormat\" : \"%,.3f\",\n" +
                    "          \"color\": \"#ffd210\",\n" +
                    "          \"line_width\": \"1\",\n" +
                    "          \"line_circleSize\": \"2\",\n" +
                    "          \"line_circleColor\": \"#ffffff\",\n" +
                    "          \"line_innerCircleColor\": \"#99d9ea\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"category\": \"bar\",\n" +
                    "      \"chartBGColor\": \"#00ffffff\",\n" +
                    "      \"graphBGColor\": \"#ffffff\",\n" +
                    "      \"xAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"gap\": \"-1\",\n" +
                    "        \"position\": \"BOTTOM\"\n" +
                    "      },\n" +
                    "      \"leftYAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"-10\",\n" +
                    "        \"max\": \"100\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"20\",\n" +
                    "        \"spaceBottom\": \"20\",\n" +
                    "        \"unit\": \"명\",\n" +
                    "        \"valueFormat\" : \"%1o\"\n" +
                    "      },\n" +
                    "      \"rightYAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"0\",\n" +
                    "        \"max\": \"110\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"30\",\n" +
                    "        \"spaceBottom\": \"30\",\n" +
                    "        \"unit\": \"마리\",\n" +
                    "        \"valueFormat\" : \"%2o\"\n" +
                    "      },\n" +
                    "      \"tooltip\": {\n" +
                    "        \"leftSide\": \"index\",\n" +
                    "        \"skipZero\": \"all\"\n" +
                    "      },\n" +
                    "      \"legend\": {\n" +
                    "        \"position\": \"BELOW_CHART_CENTER\",\n" +
                    "        \"form\": \"CIRCLE\",\n" +
                    "        \"formSize\": \"2\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"7\",\n" +
                    "        \"legendSpace\": \"1\"\n" +
                    "      },\n" +
                    "      \"data\": [\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일11\",\n" +
                    "            \"월21\",\n" +
                    "            \"화31\",\n" +
                    "            \"수41\",\n" +
                    "            \"목51\",\n" +
                    "            \"금61\",\n" +
                    "            \"토71\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"0\",\n" +
                    "            \"0\",\n" +
                    "            \"92\",\n" +
                    "            \"95\",\n" +
                    "            \"93\",\n" +
                    "            \"49\",\n" +
                    "            \"5\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트5\",\n" +
                    "          \"axisDepend\": \"LEFT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ff0000\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"비\",\n" +
                    "          \"valueFormat\" : \"%3o\",\n" +
                    "          \"color\": \"#ff00ff\",\n" +
                    "          \"bar_space\": \"15\",\n" +
                    "          \"bar_groupSpace\": \"80\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일12\",\n" +
                    "            \"월22\",\n" +
                    "            \"화32\",\n" +
                    "            \"수42\",\n" +
                    "            \"목52\",\n" +
                    "            \"금62\",\n" +
                    "            \"토72\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"1\",\n" +
                    "            \"1\",\n" +
                    "            \"92\",\n" +
                    "            \"92\",\n" +
                    "            \"90\",\n" +
                    "            \"90\",\n" +
                    "            \"5\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트6\",\n" +
                    "          \"axisDepend\": \"LEFT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ff0000\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"비\",\n" +
                    "          \"valueFormat\" : \"%o\",\n" +
                    "          \"color\": \"#ff00ff\",\n" +
                    "          \"bar_space\": \"15\",\n" +
                    "          \"bar_groupSpace\": \"80\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일23\",\n" +
                    "            \"월33\",\n" +
                    "            \"화33\",\n" +
                    "            \"수33\",\n" +
                    "            \"목23\",\n" +
                    "            \"금23\",\n" +
                    "            \"토23\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"73\",\n" +
                    "            \"4\",\n" +
                    "            \"84\",\n" +
                    "            \"85\",\n" +
                    "            \"6\",\n" +
                    "            \"99\",\n" +
                    "            \"1\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트7\",\n" +
                    "          \"axisDepend\": \"RIGHT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ffd200\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"눈\",\n" +
                    "          \"valueFormat\" : \"%o\",\n" +
                    "          \"color\": \"#ffd210\",\n" +
                    "          \"bar_space\": \"15\",\n" +
                    "          \"bar_groupSpace\": \"80\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"category\": \"combined\",\n" +
                    "      \"chartBGColor\": \"#00ffffff\",\n" +
                    "      \"graphBGColor\": \"#ffffff\",\n" +
                    "      \"xAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"gap\": \"-1\",\n" +
                    "        \"position\": \"BOTTOM\"\n" +
                    "      },\n" +
                    "      \"leftYAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"0\",\n" +
                    "        \"max\": \"100\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"20\",\n" +
                    "        \"spaceBottom\": \"20\",\n" +
                    "        \"unit\": \"명\",\n" +
                    "        \"valueFormat\" : \"%x\"\n" +
                    "      },\n" +
                    "      \"rightYAxis\": {\n" +
                    "        \"enabled\": \"true\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"10\",\n" +
                    "        \"gridColor\": \"#f0f0f0\",\n" +
                    "        \"gridWidth\": \"1\",\n" +
                    "        \"min\": \"0\",\n" +
                    "        \"max\": \"100\",\n" +
                    "        \"invert\": \"false\",\n" +
                    "        \"spaceTop\": \"20\",\n" +
                    "        \"spaceBottom\": \"20\",\n" +
                    "        \"unit\": \"마리\",\n" +
                    "        \"valueFormat\" : \"%1x\"\n" +
                    "      },\n" +
                    "      \"tooltip\": {\n" +
                    "        \"leftSide\": \"label\",\n" +
                    "        \"skipZero\": \"bar\"\n" +
                    "      },\n" +
                    "      \"legend\": {\n" +
                    "        \"position\": \"BELOW_CHART_CENTER\",\n" +
                    "        \"form\": \"line\",\n" +
                    "        \"formSize\": \"7\",\n" +
                    "        \"textColor\": \"#000000\",\n" +
                    "        \"textSize\": \"5\",\n" +
                    "        \"legendSpace\": \"0\"\n" +
                    "      },\n" +
                    "      \"data\": [\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"100\",\n" +
                    "            \"0\",\n" +
                    "            \"0\",\n" +
                    "            \"75\",\n" +
                    "            \"90\",\n" +
                    "            \"0\",\n" +
                    "            \"0\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트8\",\n" +
                    "          \"axisDepend\": \"LEFT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ff0000\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"비\",\n" +
                    "          \"valueFormat\" : \"%2x\",\n" +
                    "          \"color\": \"#ff0da0\",\n" +
                    "          \"line_width\": \"2\",\n" +
                    "          \"line_circleSize\": \"4\",\n" +
                    "          \"line_circleColor\": \"#00b2ff\",\n" +
                    "          \"line_innerCircleColor\": \"#ffffff\",\n" +
                    "          \"line_dash\": {\n" +
                    "            \"line_length\": \"4\",\n" +
                    "            \"line_spaceLength\": \"6\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"83\",\n" +
                    "            \"94\",\n" +
                    "            \"4\",\n" +
                    "            \"95\",\n" +
                    "            \"96\",\n" +
                    "            \"9\",\n" +
                    "            \"1\"\n" +
                    "          ],\n" +
                    "          \"label\": \"테스트9\",\n" +
                    "          \"axisDepend\": \"RIGHT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ffd200\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"눈\",\n" +
                    "          \"valueFormat\" : \"%3x\",\n" +
                    "          \"color\": \"#ffd299\",\n" +
                    "          \"line_width\": \"1\",\n" +
                    "          \"line_circleSize\": \"2\",\n" +
                    "          \"line_circleColor\": \"#ffffff\",\n" +
                    "          \"line_innerCircleColor\": \"#99d9ea\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"xValues\": [\n" +
                    "            \"일\",\n" +
                    "            \"월\",\n" +
                    "            \"화\",\n" +
                    "            \"수\",\n" +
                    "            \"목\",\n" +
                    "            \"금\",\n" +
                    "            \"토\"\n" +
                    "          ],\n" +
                    "          \"yValues\": [\n" +
                    "            \"0\",\n" +
                    "            \"0\",\n" +
                    "            \"0\",\n" +
                    "            \"95\",\n" +
                    "            \"0\",\n" +
                    "            \"0\",\n" +
                    "            \"0\"\n" +
                    "          ],\n" +
                    "          \"graphType\": \"bar\",\n" +
                    "          \"bar_space\": \"15\",\n" +
                    "          \"bar_groupSpace\": \"80\",\n" +
                    "          \"label\": \"테스트10\",\n" +
                    "          \"axisDepend\": \"LEFT\",\n" +
                    "          \"textEnable\": \"true\",\n" +
                    "          \"textColor\": \"#ff0022\",\n" +
                    "          \"textSize\": \"5\",\n" +
                    "          \"unit\": \"풍\",\n" +
                    "          \"valueFormat\" : \"%4x\",\n" +
                    "          \"color\": \"#ff0022\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }," +
                    "{\n" +
                    "  \"category\": \"pie\",\n" +
                    "  \"chartBGColor\": \"#ffffff\",\n" +
                    "  \"xAxis\": {\n" +
                    "    \"textColor\": \"#000000\",\n" +
                    "    \"textSize\": \"10\",\n" +
                    "    \"gridColor\": \"#f0f0f0\",\n" +
                    "    \"gridWidth\": \"1\",\n" +
                    "    \"gap\": \"-1\",\n" +
                    "    \"position\": \"BOTTOM\"\n" +
                    "  },\n" +
                    "  \"leftYAxis\": {\n" +
                    "    \"textColor\": \"#000000\",\n" +
                    "    \"textSize\": \"10\",\n" +
                    "    \"gridColor\": \"#f0f0f0\",\n" +
                    "    \"gridWidth\": \"1\",\n" +
                    "    \"min\": \"0\",\n" +
                    "    \"max\": \"100\",\n" +
                    "    \"invert\": \"false\",\n" +
                    "    \"spaceTop\": \"20\",\n" +
                    "    \"spaceBottom\": \"20\",\n" +
                    "    \"unit\": \"명\"\n" +
                    "  },\n" +
                    "  \"rightYAxis\": {\n" +
                    "    \"textColor\": \"#000000\",\n" +
                    "    \"textSize\": \"10\",\n" +
                    "    \"gridColor\": \"#f0f0f0\",\n" +
                    "    \"gridWidth\": \"1\",\n" +
                    "    \"min\": \"0\",\n" +
                    "    \"max\": \"100\",\n" +
                    "    \"invert\": \"false\",\n" +
                    "    \"spaceTop\": \"20\",\n" +
                    "    \"spaceBottom\": \"20\",\n" +
                    "    \"unit\": \"마리\"\n" +
                    "  },\n" +
                    "  \"tooltip\": {\n" +
                    "    \"leftSide\": \"label\",\n" +
                    "    \"unit\": \"다스\",\n" +
                    "    \"skipZero\": \"bar\"\n" +
                    "  },\n" +
                    "  \"legend\": {\n" +
                    "    \"position\": \"BELOW_CHART_CENTER\",\n" +
                    "    \"form\": \"square\",\n" +
                    "    \"formSize\": \"7\",\n" +
                    "    \"textColor\": \"#000000\",\n" +
                    "    \"textSize\": \"5\",\n" +
                    "    \"legendSpace\": \"3\"\n" +
                    "  },\n" +
                    "  \"data\": [\n" +
                    "    {\n" +
                    "      \"xValues\": [\n" +
                    "        \"일\",\n" +
                    "        \"월\",\n" +
                    "        \"화\",\n" +
                    "        \"수\",\n" +
                    "        \"목\",\n" +
                    "        \"금\",\n" +
                    "        \"토\"\n" +
                    "      ],\n" +
                    "      \"yValues\": [\n" +
                    "        \"0\",\n" +
                    "        \"10\",\n" +
                    "        \"20\",\n" +
                    "        \"25\",\n" +
                    "        \"30\",\n" +
                    "        \"40\",\n" +
                    "        \"50\"\n" +
                    "      ],\n" +
                    "      \"label\": \"테스트11\",\n" +
                    "      \"axisDepend\": \"LEFT\",\n" +
                    "      \"textEnable\": \"true\",\n" +
                    "      \"textColor\": \"#123a0d\",\n" +
                    "      \"textSize\": \"10\",\n" +
                    "      \"unit\": \"비\",\n" +
                    "      \"pie_colors\": [\n" +
                    "        \"#ff0da0\",\n" +
                    "        \"#ffaec9\",\n" +
                    "        \"#b5e61d\",\n" +
                    "        \"#99d9ea\",\n" +
                    "        \"#c8bfe7\",\n" +
                    "        \"#efe4b0\",\n" +
                    "        \"#b97a57\"\n" +
                    "      ],\n" +
                    "      \"pie_sliceSpace\": \"3\",\n" +
                    "      \"pie_emptyCenter\": \"60\",\n" +
                    "      \"pie_centerText\": \"테스트11\",\n" +
                    "      \"pie_centerTextColor\": \"#010101\",\n" +
                    "      \"pie_centerTextSize\": \"15\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}" +
                    "  ]" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
                    */
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




