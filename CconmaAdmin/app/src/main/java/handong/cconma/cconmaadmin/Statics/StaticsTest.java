package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.JSONResponse;

import static android.widget.RelativeLayout.*;

public class StaticsTest extends Activity implements JSONResponse {
    private BackPressCloseHandler backPressCloseHandler;
    private JSONObject result;
    private LinkedHashMap<View, ViewGroup.LayoutParams> charts;
    private ArrayList<RelativeLayout> btns;
    private LinearLayout ll;
    private boolean mode = false;
    private StaticsCommonSetting setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics_test);

        backPressCloseHandler = new BackPressCloseHandler(this);

        charts = new LinkedHashMap<>();
        btns = new ArrayList<>();
        setting = new StaticsCommonSetting();

        //  HTTPConnector hc = new HTTPConnector(this, "GET");
        // hc.execute("");

        ll = (LinearLayout) findViewById(R.id.statics_test_ll);
        processFinish(new ArrayList<JSONObject>());

    }

    public void parsingcCharts() {
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateZoomBtn(final View chart) {
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView tv = new TextView(this);
        tv.setText("크게보기 >");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        int dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()); // 10dp
        tv.setPadding(dpInPx, dpInPx, dpInPx, dpInPx);
        tv.setBackgroundColor(getResources().getColor(R.color.transparent));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(CENTER_VERTICAL);
        tv.setLayoutParams(params);
        rl.addView(tv);

        ll.addView(rl);
        btns.add(rl);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
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
        });
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

        chart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics())));
        generateZoomBtn(chart);
        chart.invalidate();

        ll.addView(chart);
        charts.put(chart, chart.getLayoutParams());
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
    public void processFinish(ArrayList<JSONObject> output) {
        //result = output.get(0);

        try {
            result = new JSONObject("{" +
                    "  \"chart\": [" +
                    "    {" +
                    "      \"category\": \"line\"," +
                    "      \"chartBGColor\": \"#00ffffff\"," +
                    "      \"graphBGColor\": \"#ffffff\"," +
                    "      \"xAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"gap\": \"0\"," +
                    "        \"position\": \"BOTTOM\"" +
                    "      }," +
                    "      \"leftYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"-10\"," +
                    "        \"max\": \"100\"," +
                    "        \"invert\": \"true\"," +
                    "        \"spaceTop\": \"20\"," +
                    "        \"spaceBottom\": \"20\"," +
                    "        \"unit\": \"명\"" +
                    "      }," +
                    "      \"rightYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"0\"," +
                    "        \"max\": \"110\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"30\"," +
                    "        \"spaceBottom\": \"30\"," +
                    "        \"unit\": \"마리\"" +
                    "      }," +
                    "      \"tooltip\": {" +
                    "        \"leftSide\": \"index\"," +
                    "        \"unit\": \"대\"," +
                    "        \"skipZero\": \"bar\"" +
                    "      }," +
                    "      \"legend\": {" +
                    "        \"position\": \"BELOW_CHART_CENTER\"," +
                    "        \"form\": \"CIRCLE\"," +
                    "        \"formSize\": \"2\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"7\"," +
                    "        \"legendSpace\": \"1\"" +
                    "      }," +
                    "      \"data\": [" +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일\"," +
                    "            \"월\"," +
                    "            \"화\"," +
                    "            \"수\"," +
                    "            \"목\"," +
                    "            \"금\"," +
                    "            \"토\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"0\"," +
                    "            \"10\"," +
                    "            \"20\"," +
                    "            \"25\"," +
                    "            \"30\"," +
                    "            \"40\"," +
                    "            \"50\"" +
                    "          ]," +
                    "          \"label\": \"테스트1\"," +
                    "          \"axisDepend\": \"LEFT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ff0000\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"비\"," +
                    "          \"color\": \"#ff0000\"," +
                    "          \"line_width\": \"2\"," +
                    "          \"line_circleSize\": \"4\"," +
                    "          \"line_circleColor\": \"#00b2ff\"," +
                    "          \"line_innerCircleColor\": \"#ffffff\"," +
                    "          \"line_dash\": {" +
                    "            \"line_length\": \"4\"," +
                    "            \"line_spaceLength\": \"6\"" +
                    "          }" +
                    "        }," +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일\"," +
                    "            \"월\"," +
                    "            \"화\"," +
                    "            \"수\"," +
                    "            \"목\"," +
                    "            \"금\"," +
                    "            \"토\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"13\"," +
                    "            \"34\"," +
                    "            \"44\"," +
                    "            \"45\"," +
                    "            \"66\"," +
                    "            \"69\"," +
                    "            \"71\"" +
                    "          ]," +
                    "          \"label\": \"테스트2\"," +
                    "          \"axisDepend\": \"RIGHT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ffd200\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"눈\"," +
                    "          \"color\": \"#ffd200\"," +
                    "          \"line_width\": \"1\"," +
                    "          \"line_circleSize\": \"2\"," +
                    "          \"line_circleColor\": \"#ffffff\"," +
                    "          \"line_innerCircleColor\": \"#99d9ea\"" +
                    "        }" +
                    "      ]" +
                    "    }," +
                    "    {" +
                    "      \"category\": \"line\"," +
                    "      \"chartBGColor\": \"#00ffffff\"," +
                    "      \"graphBGColor\": \"#ffffff\"," +
                    "      \"xAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"gap\": \"0\"," +
                    "        \"position\": \"BOTTOM\"" +
                    "      }," +
                    "      \"leftYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"-10\"," +
                    "        \"max\": \"100\"," +
                    "        \"invert\": \"true\"," +
                    "        \"spaceTop\": \"20\"," +
                    "        \"spaceBottom\": \"20\"," +
                    "        \"unit\": \"명\"" +
                    "      }," +
                    "      \"rightYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"0\"," +
                    "        \"max\": \"110\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"30\"," +
                    "        \"spaceBottom\": \"30\"," +
                    "        \"unit\": \"마리\"" +
                    "      }," +
                    "      \"tooltip\": {" +
                    "        \"leftSide\": \"index\"," +
                    "        \"unit\": \"대\"," +
                    "        \"skipZero\": \"bar\"" +
                    "      }," +
                    "      \"legend\": {" +
                    "        \"position\": \"BELOW_CHART_CENTER\"," +
                    "        \"form\": \"CIRCLE\"," +
                    "        \"formSize\": \"2\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"7\"," +
                    "        \"legendSpace\": \"1\"" +
                    "      }," +
                    "      \"data\": [" +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일1\"," +
                    "            \"월2\"," +
                    "            \"화3\"," +
                    "            \"수4\"," +
                    "            \"목5\"," +
                    "            \"금6\"," +
                    "            \"토7\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"0\"," +
                    "            \"10\"," +
                    "            \"20\"," +
                    "            \"25\"," +
                    "            \"30\"," +
                    "            \"40\"," +
                    "            \"50\"" +
                    "          ]," +
                    "          \"label\": \"테스트3\"," +
                    "          \"axisDepend\": \"LEFT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ff0000\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"비\"," +
                    "          \"color\": \"#ff00ff\"," +
                    "          \"line_width\": \"2\"," +
                    "          \"line_circleSize\": \"4\"," +
                    "          \"line_circleColor\": \"#00b2ff\"," +
                    "          \"line_innerCircleColor\": \"#ffffff\"," +
                    "          \"line_dash\": {" +
                    "            \"line_length\": \"4\"," +
                    "            \"line_spaceLength\": \"6\"" +
                    "          }" +
                    "        }," +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일2\"," +
                    "            \"월3\"," +
                    "            \"화3\"," +
                    "            \"수3\"," +
                    "            \"목2\"," +
                    "            \"금2\"," +
                    "            \"토2\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"13\"," +
                    "            \"34\"," +
                    "            \"44\"," +
                    "            \"45\"," +
                    "            \"66\"," +
                    "            \"69\"," +
                    "            \"71\"" +
                    "          ]," +
                    "          \"label\": \"테스트4\"," +
                    "          \"axisDepend\": \"RIGHT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ffd200\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"눈\"," +
                    "          \"color\": \"#ffd210\"," +
                    "          \"line_width\": \"1\"," +
                    "          \"line_circleSize\": \"2\"," +
                    "          \"line_circleColor\": \"#ffffff\"," +
                    "          \"line_innerCircleColor\": \"#99d9ea\"" +
                    "        }" +
                    "      ]" +
                    "    }," +
                    "    {" +
                    "      \"category\": \"bar\"," +
                    "      \"chartBGColor\": \"#00ffffff\"," +
                    "      \"graphBGColor\": \"#ffffff\"," +
                    "      \"xAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"gap\": \"-1\"," +
                    "        \"position\": \"BOTTOM\"" +
                    "      }," +
                    "      \"leftYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"-10\"," +
                    "        \"max\": \"100\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"20\"," +
                    "        \"spaceBottom\": \"20\"," +
                    "        \"unit\": \"명\"" +
                    "      }," +
                    "      \"rightYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"0\"," +
                    "        \"max\": \"110\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"30\"," +
                    "        \"spaceBottom\": \"30\"," +
                    "        \"unit\": \"마리\"" +
                    "      }," +
                    "      \"tooltip\": {" +
                    "        \"leftSide\": \"index\"," +
                    "        \"unit\": \"대\"," +
                    "        \"skipZero\": \"all\"" +
                    "      }," +
                    "      \"legend\": {" +
                    "        \"position\": \"BELOW_CHART_CENTER\"," +
                    "        \"form\": \"CIRCLE\"," +
                    "        \"formSize\": \"2\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"7\"," +
                    "        \"legendSpace\": \"1\"" +
                    "      }," +
                    "      \"data\": [" +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일11\"," +
                    "            \"월21\"," +
                    "            \"화31\"," +
                    "            \"수41\"," +
                    "            \"목51\"," +
                    "            \"금61\"," +
                    "            \"토71\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"0\"," +
                    "            \"10\"," +
                    "            \"20\"," +
                    "            \"25\"," +
                    "            \"30\"," +
                    "            \"40\"," +
                    "            \"50\"" +
                    "          ]," +
                    "          \"label\": \"테스트5\"," +
                    "          \"axisDepend\": \"LEFT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ff0000\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"비\"," +
                    "          \"color\": \"#ff00ff\"," +
                    "          \"bar_space\": \"15\"," +
                    "          \"bar_groupSpace\": \"80\"" +
                    "        }," +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일12\"," +
                    "            \"월22\"," +
                    "            \"화32\"," +
                    "            \"수42\"," +
                    "            \"목52\"," +
                    "            \"금62\"," +
                    "            \"토72\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"10\"," +
                    "            \"10\"," +
                    "            \"20\"," +
                    "            \"25\"," +
                    "            \"30\"," +
                    "            \"40\"," +
                    "            \"50\"" +
                    "          ]," +
                    "          \"label\": \"테스트6\"," +
                    "          \"axisDepend\": \"LEFT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ff0000\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"비\"," +
                    "          \"color\": \"#ff00ff\"," +
                    "          \"bar_space\": \"15\"," +
                    "          \"bar_groupSpace\": \"80\"" +
                    "        }," +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일23\"," +
                    "            \"월33\"," +
                    "            \"화33\"," +
                    "            \"수33\"," +
                    "            \"목23\"," +
                    "            \"금23\"," +
                    "            \"토23\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"13\"," +
                    "            \"34\"," +
                    "            \"44\"," +
                    "            \"45\"," +
                    "            \"66\"," +
                    "            \"69\"," +
                    "            \"71\"" +
                    "          ]," +
                    "          \"label\": \"테스트7\"," +
                    "          \"axisDepend\": \"RIGHT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ffd200\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"눈\"," +
                    "          \"color\": \"#ffd210\"," +
                    "          \"bar_space\": \"15\"," +
                    "          \"bar_groupSpace\": \"80\"" +
                    "        }" +
                    "      ]" +
                    "    }," +
                    "    {" +
                    "      \"category\": \"combined\"," +
                    "      \"chartBGColor\": \"#00ffffff\"," +
                    "      \"graphBGColor\": \"#ffffff\"," +
                    "      \"xAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"gap\": \"-1\"," +
                    "        \"position\": \"BOTTOM\"" +
                    "      }," +
                    "      \"leftYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"0\"," +
                    "        \"max\": \"100\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"20\"," +
                    "        \"spaceBottom\": \"20\"," +
                    "        \"unit\": \"명\"" +
                    "      }," +
                    "      \"rightYAxis\": {" +
                    "        \"enabled\": \"true\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"0\"," +
                    "        \"max\": \"100\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"20\"," +
                    "        \"spaceBottom\": \"20\"," +
                    "        \"unit\": \"마리\"" +
                    "      }," +
                    "      \"tooltip\": {" +
                    "        \"leftSide\": \"label\"," +
                    "        \"unit\": \"다스\"," +
                    "        \"skipZero\": \"bar\"" +
                    "      }," +
                    "      \"legend\": {" +
                    "        \"position\": \"BELOW_CHART_CENTER\"," +
                    "        \"form\": \"line\"," +
                    "        \"formSize\": \"7\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"5\"," +
                    "        \"legendSpace\": \"0\"" +
                    "      }," +
                    "      \"data\": [" +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일\"," +
                    "            \"월\"," +
                    "            \"화\"," +
                    "            \"수\"," +
                    "            \"목\"," +
                    "            \"금\"," +
                    "            \"토\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"0\"," +
                    "            \"10\"," +
                    "            \"20\"," +
                    "            \"25\"," +
                    "            \"30\"," +
                    "            \"40\"," +
                    "            \"50\"" +
                    "          ]," +
                    "          \"label\": \"테스트8\"," +
                    "          \"axisDepend\": \"LEFT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ff0000\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"비\"," +
                    "          \"color\": \"#ff0da0\"," +
                    "          \"line_width\": \"2\"," +
                    "          \"line_circleSize\": \"4\"," +
                    "          \"line_circleColor\": \"#00b2ff\"," +
                    "          \"line_innerCircleColor\": \"#ffffff\"," +
                    "          \"line_dash\": {" +
                    "            \"line_length\": \"4\"," +
                    "            \"line_spaceLength\": \"6\"" +
                    "          }" +
                    "        }," +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일\"," +
                    "            \"월\"," +
                    "            \"화\"," +
                    "            \"수\"," +
                    "            \"목\"," +
                    "            \"금\"," +
                    "            \"토\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"13\"," +
                    "            \"34\"," +
                    "            \"44\"," +
                    "            \"45\"," +
                    "            \"66\"," +
                    "            \"69\"," +
                    "            \"71\"" +
                    "          ]," +
                    "          \"label\": \"테스트9\"," +
                    "          \"axisDepend\": \"RIGHT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ffd200\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"눈\"," +
                    "          \"color\": \"#ffd299\"," +
                    "          \"line_width\": \"1\"," +
                    "          \"line_circleSize\": \"2\"," +
                    "          \"line_circleColor\": \"#ffffff\"," +
                    "          \"line_innerCircleColor\": \"#99d9ea\"" +
                    "        }," +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일\"," +
                    "            \"월\"," +
                    "            \"화\"," +
                    "            \"수\"," +
                    "            \"목\"," +
                    "            \"금\"," +
                    "            \"토\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"0\"," +
                    "            \"0\"," +
                    "            \"0\"," +
                    "            \"75\"," +
                    "            \"0\"," +
                    "            \"0\"," +
                    "            \"0\"" +
                    "          ]," +
                    "          \"graphType\": \"bar\"," +
                    "          \"bar_space\": \"15\"," +
                    "          \"bar_groupSpace\": \"80\"," +
                    "          \"label\": \"테스트10\"," +
                    "          \"axisDepend\": \"LEFT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#ff0022\"," +
                    "          \"textSize\": \"5\"," +
                    "          \"unit\": \"풍\"," +
                    "          \"color\": \"#ff0022\"" +
                    "        }" +
                    "      ]" +
                    "    }," +
                    "    {" +
                    "      \"category\": \"pie\"," +
                    "      \"chartBGColor\": \"#ffffff\"," +
                    "      \"xAxis\": {" +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"gap\": \"-1\"," +
                    "        \"position\": \"BOTTOM\"" +
                    "      }," +
                    "      \"leftYAxis\": {" +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"0\"," +
                    "        \"max\": \"100\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"20\"," +
                    "        \"spaceBottom\": \"20\"," +
                    "        \"unit\": \"명\"" +
                    "      }," +
                    "      \"rightYAxis\": {" +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"10\"," +
                    "        \"gridColor\": \"#f0f0f0\"," +
                    "        \"gridWidth\": \"1\"," +
                    "        \"min\": \"0\"," +
                    "        \"max\": \"100\"," +
                    "        \"invert\": \"false\"," +
                    "        \"spaceTop\": \"20\"," +
                    "        \"spaceBottom\": \"20\"," +
                    "        \"unit\": \"마리\"" +
                    "      }," +
                    "      \"tooltip\": {" +
                    "        \"leftSide\": \"label\"," +
                    "        \"unit\": \"다스\"," +
                    "        \"skipZero\": \"bar\"" +
                    "      }," +
                    "      \"legend\": {" +
                    "        \"position\": \"BELOW_CHART_CENTER\"," +
                    "        \"form\": \"square\"," +
                    "        \"formSize\": \"7\"," +
                    "        \"textColor\": \"#000000\"," +
                    "        \"textSize\": \"5\"," +
                    "        \"legendSpace\": \"3\"" +
                    "      }," +
                    "      \"data\": [" +
                    "        {" +
                    "          \"xValues\": [" +
                    "            \"일\"," +
                    "            \"월\"," +
                    "            \"화\"," +
                    "            \"수\"," +
                    "            \"목\"," +
                    "            \"금\"," +
                    "            \"토\"" +
                    "          ]," +
                    "          \"yValues\": [" +
                    "            \"0\"," +
                    "            \"10\"," +
                    "            \"20\"," +
                    "            \"25\"," +
                    "            \"30\"," +
                    "            \"40\"," +
                    "            \"50\"" +
                    "          ]," +
                    "          \"label\": \"테스트11\"," +
                    "          \"axisDepend\": \"LEFT\"," +
                    "          \"textEnable\": \"true\"," +
                    "          \"textColor\": \"#123a0d\"," +
                    "          \"textSize\": \"10\"," +
                    "          \"unit\": \"비\"," +
                    "          \"pie_colors\": [" +
                    "            \"#ff0da0\"," +
                    "            \"#ffaec9\"," +
                    "            \"#b5e61d\"," +
                    "            \"#99d9ea\"," +
                    "            \"#c8bfe7\"," +
                    "            \"#efe4b0\"," +
                    "            \"#b97a57\"" +
                    "          ]," +
                    "          \"pie_sliceSpace\": \"3\"," +
                    "          \"pie_emptyCenter\": \"60\"," +
                    "          \"pie_centerText\": \"테스트11\"," +
                    "          \"pie_centerTextColor\": \"#010101\"," +
                    "          \"pie_centerTextSize\": \"15\"" +
                    "        }" +
                    "      ]" +
                    "    }" +
                    "  ]" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            if (mode && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {// 가로
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
                mode = false;
                visible();
            } else if (mode && config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                mode = false;
                visible();
            } else {
                activity.finish();
            }
        }
    }
}




