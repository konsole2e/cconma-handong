package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;

public class StaticsCombinedManager {
    private CombinedChart chart;
    private Context con;

    public StaticsCombinedManager(Context context) {
        con = context;
    }

    public CombinedData parsingCombined(JSONObject obj, CombinedChart combinedChart) {
        try {
            chart = combinedChart;
            chartSetting(obj);
            if (obj.has(StaticsVariables.xAxis)) {
                xAxisSetting(obj.getJSONObject(StaticsVariables.xAxis));
            }
            if (obj.has(StaticsVariables.leftYAxis)) {
                leftYAxisSetting(obj.getJSONObject(StaticsVariables.leftYAxis));
            }
            if (obj.has(StaticsVariables.rightYAxis)) {
                rightYAxisSetting(obj.getJSONObject(StaticsVariables.rightYAxis));
            }
            if (obj.has(StaticsVariables.tooltip)) {
                toolTipSetting(obj.getJSONObject(StaticsVariables.tooltip));
            }
            if (obj.has(StaticsVariables.legend)) {
                legendSetting(obj.getJSONObject(StaticsVariables.legend));
            }
            return dataSetting(obj.getJSONArray(StaticsVariables.data));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void chartSetting(JSONObject obj) {
        try {
            if (!obj.optString(StaticsVariables.chartBGColor).equals("")) {
                chart.setBackgroundColor(Color.parseColor(obj.getString(StaticsVariables.chartBGColor)));
            }
            if (!obj.optString(StaticsVariables.graphBGColor).equals("")) {
                chart.setGridBackgroundColor(Color.parseColor(obj.getString(StaticsVariables.graphBGColor)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "chartSetting");
        }
    }

    public void xAxisSetting(JSONObject xAxis) {
        try {
            if (!xAxis.optString(StaticsVariables.textColor).equals("")) {
                chart.getXAxis().setTextColor(Color.parseColor(xAxis.getString(StaticsVariables.textColor)));
            }
            if (!xAxis.optString(StaticsVariables.textSize).equals("")) {
                chart.getXAxis().setTextSize(Float.valueOf(xAxis.getString(StaticsVariables.textSize)));
            }
            if (!xAxis.optString(StaticsVariables.gridColor).equals("")) {
                chart.getXAxis().setGridColor(Color.parseColor(xAxis.getString(StaticsVariables.gridColor)));
            }
            if (!xAxis.optString(StaticsVariables.gridWidth).equals("")) {
                chart.getXAxis().setGridLineWidth(Float.valueOf(xAxis.getString(StaticsVariables.gridWidth)));
            }
            if (!xAxis.optString(StaticsVariables.gap).equals("")) {
                int gapToSkip = Integer.valueOf(xAxis.getString(StaticsVariables.gap));
                if (gapToSkip >= 0) {
                    chart.getXAxis().setLabelsToSkip(gapToSkip);

                } else {
                    chart.getXAxis().resetLabelsToSkip();
                }
            } else {
                chart.getXAxis().resetLabelsToSkip();
            }
            if (!xAxis.optString(StaticsVariables.position).equals("")) {
                chart.getXAxis().setPosition(XAxis.XAxisPosition.valueOf(xAxis.getString(StaticsVariables.position).toUpperCase()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "xAxisSetting");
        }
    }

    public void leftYAxisSetting(JSONObject yAxis) {
        try {
            if (!yAxis.optString(StaticsVariables.textColor).equals("")) {
                chart.getAxisLeft().setTextColor(Color.parseColor(yAxis.getString(StaticsVariables.textColor)));
            }
            if (!yAxis.optString(StaticsVariables.textSize).equals("")) {
                chart.getAxisLeft().setTextSize(Float.valueOf(yAxis.getString(StaticsVariables.textSize)));
            }
            if (!yAxis.optString(StaticsVariables.gridColor).equals("")) {
                chart.getAxisLeft().setGridColor(Color.parseColor(yAxis.getString(StaticsVariables.gridColor)));
            }
            if (!yAxis.optString(StaticsVariables.gridWidth).equals("")) {
                chart.getAxisLeft().setGridLineWidth(Float.valueOf(yAxis.getString(StaticsVariables.gridWidth)));
            }
            if (!yAxis.optString(StaticsVariables.min).equals("")) {
                chart.getAxisLeft().setAxisMinValue(Float.valueOf(yAxis.getString(StaticsVariables.min)));
            }
            if (!yAxis.optString(StaticsVariables.max).equals("")) {
                chart.getAxisLeft().setAxisMaxValue(Float.valueOf(yAxis.getString(StaticsVariables.max)));
            }
            if (!yAxis.optString(StaticsVariables.invert).equals("")) {
                chart.getAxisLeft().setInverted(Boolean.valueOf(yAxis.getString(StaticsVariables.invert)));
            }
            if (!yAxis.optString(StaticsVariables.spaceTop).equals("")) {
                chart.getAxisLeft().setSpaceTop(Float.valueOf(yAxis.getString(StaticsVariables.spaceTop)));
            }
            if (!yAxis.optString(StaticsVariables.spaceBottom).equals("")) {
                chart.getAxisLeft().setSpaceBottom(Float.valueOf(yAxis.getString(StaticsVariables.spaceBottom)));
            }
            if (!yAxis.optString(StaticsVariables.unit).equals("")) {
                chart.getAxisLeft().setValueFormatter(new StaticsValueFormatter(yAxis.getString(StaticsVariables.unit)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "leftYAxisSetting");
        }
    }

    public void rightYAxisSetting(JSONObject yAxis) {
        try {
            if (!yAxis.optString(StaticsVariables.textColor).equals("")) {
                chart.getAxisRight().setTextColor(Color.parseColor(yAxis.getString(StaticsVariables.textColor)));
            }
            if (!yAxis.optString(StaticsVariables.textSize).equals("")) {
                chart.getAxisRight().setTextSize(Float.valueOf(yAxis.getString(StaticsVariables.textSize)));
            }
            if (!yAxis.optString(StaticsVariables.gridColor).equals("")) {
                chart.getAxisRight().setGridColor(Color.parseColor(yAxis.getString(StaticsVariables.gridColor)));
            }
            if (!yAxis.optString(StaticsVariables.gridWidth).equals("")) {
                chart.getAxisRight().setGridLineWidth(Float.valueOf(yAxis.getString(StaticsVariables.gridWidth)));
            }
            if (!yAxis.optString(StaticsVariables.min).equals("")) {
                chart.getAxisRight().setAxisMinValue(Float.valueOf(yAxis.getString(StaticsVariables.min)));
            }
            if (!yAxis.optString(StaticsVariables.max).equals("")) {
                chart.getAxisRight().setAxisMaxValue(Float.valueOf(yAxis.getString(StaticsVariables.max)));
            }
            if (!yAxis.optString(StaticsVariables.invert).equals("")) {
                chart.getAxisRight().setInverted(Boolean.valueOf(yAxis.getString(StaticsVariables.invert)));
            }
            if (!yAxis.optString(StaticsVariables.spaceTop).equals("")) {
                chart.getAxisRight().setSpaceTop(Float.valueOf(yAxis.getString(StaticsVariables.spaceTop)));
            }
            if (!yAxis.optString(StaticsVariables.spaceBottom).equals("")) {
                chart.getAxisRight().setSpaceBottom(Float.valueOf(yAxis.getString(StaticsVariables.spaceBottom)));
            }
            if (!yAxis.optString(StaticsVariables.unit).equals("")) {
                chart.getAxisRight().setValueFormatter(new StaticsValueFormatter(yAxis.getString(StaticsVariables.unit)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "rightYAxisSetting");
        }
    }

    public void legendSetting(JSONObject legend) {
        try {
            if (!legend.optString(StaticsVariables.position).equals("")) {
                chart.getLegend().setPosition(Legend.LegendPosition.valueOf(legend.getString(StaticsVariables.position).toUpperCase()));
            }
            if (!legend.optString(StaticsVariables.form).equals("")) {
                chart.getLegend().setForm(Legend.LegendForm.valueOf(legend.getString(StaticsVariables.form).toUpperCase()));
            }
            if (!legend.optString(StaticsVariables.formSize).equals("")) {
                chart.getLegend().setFormSize(Float.valueOf(legend.getString(StaticsVariables.formSize)));
            }
            if (!legend.optString(StaticsVariables.textColor).equals("")) {
                chart.getLegend().setTextColor(Color.parseColor(legend.getString(StaticsVariables.textColor)));
            }
            if (!legend.optString(StaticsVariables.textSize).equals("")) {
                chart.getLegend().setTextSize(Float.valueOf(legend.getString(StaticsVariables.textSize)));
            }
            if (!legend.optString(StaticsVariables.legendSpace).equals("")) {
                chart.getLegend().setFormToTextSpace(Float.valueOf(legend.getString(StaticsVariables.legendSpace)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "legendSetting");
        }
    }

    public void toolTipSetting(JSONObject tt) {
        String mLeft = "LABEL";
        String mUnit = "";
        String mSkipZero = "";
        int mTextSize = 9;
        try {
            if (!tt.optString(StaticsVariables.leftSide).equals("")) {
                if (tt.getString(StaticsVariables.leftSide).toUpperCase().equals("INDEX")) {
                    mLeft = tt.getString(StaticsVariables.leftSide);
                }
            }
            if (!tt.optString(StaticsVariables.unit).equals("")) {
                mUnit = tt.getString(StaticsVariables.unit);
            }
            if(!tt.optString(StaticsVariables.skipZero).equals("")){
                mSkipZero = tt.getString(StaticsVariables.skipZero).toLowerCase();
            }
            if(!tt.optString(StaticsVariables.textSize).equals("")){
                mTextSize = Integer.valueOf(tt.getString(StaticsVariables.textSize));
            }
            StaticsMarkerView mv = new StaticsMarkerView(con, R.layout.statics_marker_view_layout);
            mv.attachChart(chart, mLeft, mUnit, mSkipZero, mTextSize);
            chart.setMarkerView(mv);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "toolTipSetting");
        }
    }

    public CombinedData dataSetting(JSONArray arr) {
        CombinedData combinedData = null;
        BarData barData = null;
        LineData lineData = new LineData();

        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject data = arr.getJSONObject(i);
                if (combinedData == null) {
                    combinedData = new CombinedData(parsingXValues(data.getJSONArray(StaticsVariables.xValues)));
                }
                if (barData == null && data.optString(StaticsVariables.graphType).toLowerCase().equals(StaticsVariables.bar)) {
                    barData = parsingBarData(data);
                } else {
                    lineData.addDataSet(parsingLineData(data));
                }
            }
            combinedData.setData(barData);
            combinedData.setData(lineData);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "dataSetting");
        }
        return combinedData;
    }

    public BarData parsingBarData(JSONObject jBar) {
        BarData barData = new BarData();
        try {
            BarDataSet dataSet = new BarDataSet(parsingBarYValues(jBar.getJSONArray(StaticsVariables.yValues)), jBar.getString(StaticsVariables.label));
            dataSet.setColor(Color.parseColor(jBar.getString(StaticsVariables.color)));

            if (!jBar.optString(StaticsVariables.textEnable).equals("") && jBar.getString(StaticsVariables.textEnable).toLowerCase().equals("false")) {
                dataSet.setDrawValues(false);
            }
            if (!jBar.optString(StaticsVariables.axisDepend).equals("")) {
                dataSet.setAxisDependency(YAxis.AxisDependency.valueOf(jBar.getString(StaticsVariables.axisDepend).toUpperCase()));
            }
            if (!jBar.optString(StaticsVariables.textColor).equals("")) {
                dataSet.setValueTextColor(Color.parseColor(jBar.getString(StaticsVariables.textColor)));
            }
            if (!jBar.optString(StaticsVariables.textSize).equals("")) {
                dataSet.setValueTextSize(Float.valueOf(jBar.getString(StaticsVariables.textSize)));
            }
            if (!jBar.optString(StaticsVariables.unit).equals("")) {
                dataSet.setValueFormatter(new StaticsValueFormatter(jBar.getString(StaticsVariables.unit)));
            }
            if (!jBar.optString(StaticsVariables.bar_space).equals("")) {
                dataSet.setBarSpacePercent(Float.valueOf(jBar.getString(StaticsVariables.bar_space)));
            }
            barData.addDataSet(dataSet);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "barDataSetting");
        }
        return barData;
    }

    public LineDataSet parsingLineData(JSONObject jLine) {
        LineDataSet dataSet = null;
        try {
            dataSet = new LineDataSet(parsingLineYValues(jLine.getJSONArray(StaticsVariables.yValues)), jLine.getString(StaticsVariables.label));
            dataSet.setColor(Color.parseColor(jLine.getString(StaticsVariables.color)));

            if (!jLine.optString(StaticsVariables.textEnable).equals("") && jLine.getString(StaticsVariables.textEnable).toLowerCase().equals("false")) {
                dataSet.setDrawValues(false);
            }
            if (!jLine.optString(StaticsVariables.axisDepend).equals("")) {
                dataSet.setAxisDependency(YAxis.AxisDependency.valueOf(jLine.getString(StaticsVariables.axisDepend).toUpperCase()));
            }
            if (!jLine.optString(StaticsVariables.textColor).equals("")) {
                dataSet.setValueTextColor(Color.parseColor(jLine.getString(StaticsVariables.textColor)));
            }
            if (!jLine.optString(StaticsVariables.textSize).equals("")) {
                dataSet.setValueTextSize(Float.valueOf(jLine.getString(StaticsVariables.textSize)));
            }
            if (!jLine.optString(StaticsVariables.unit).equals("")) {
                dataSet.setValueFormatter(new StaticsValueFormatter(jLine.getString(StaticsVariables.unit)));
            }
            if (!jLine.optString(StaticsVariables.line_width).equals("")) {
                dataSet.setLineWidth(Float.valueOf(jLine.getString(StaticsVariables.line_width)));
            }
            if (!jLine.optString(StaticsVariables.line_circleSize).equals("")) {
                dataSet.setCircleSize(Float.valueOf(jLine.getString(StaticsVariables.line_circleSize)));
            }
            if (!jLine.optString(StaticsVariables.line_circleColor).equals("")) {
                dataSet.setCircleColor(Color.parseColor(jLine.getString(StaticsVariables.line_circleColor)));
            }else{
                dataSet.setCircleColor(Color.parseColor(jLine.getString(StaticsVariables.color)));
            }
            if (!jLine.optString(StaticsVariables.line_innerCircleColor).equals("")) {
                dataSet.setCircleColorHole(Color.parseColor(jLine.getString(StaticsVariables.line_innerCircleColor)));
            }
            if (jLine.has(StaticsVariables.line_dash)) {
                JSONObject j = jLine.getJSONObject(StaticsVariables.line_dash);
                if (!(j.optString(StaticsVariables.line_length).equals("") || j.optString(StaticsVariables.line_spaceLength).equals(""))) {
                    dataSet.enableDashedLine(Float.valueOf(j.getString(StaticsVariables.line_length)), Float.valueOf(j.getString(StaticsVariables.line_spaceLength)), 0f);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "lineDataSetting");
        }
        return dataSet;
    }

    public ArrayList<String> parsingXValues(JSONArray jXs) {
        ArrayList<String> xValues = new ArrayList<>();
        try {
            for (int i = 0; i < jXs.length(); i++) {
                xValues.add(jXs.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "parsingXValues");
        }
        return xValues;
    }

    public ArrayList<Entry> parsingLineYValues(JSONArray jYs) {
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            for (int i = 0; i < jYs.length(); i++) {
                entries.add(new Entry(Float.valueOf(jYs.getString(i)), i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "parsingLineYValues");
        }
        return entries;
    }

    public ArrayList<BarEntry> parsingBarYValues(JSONArray jYs) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        try {
            for (int i = 0; i < jYs.length(); i++) {
                entries.add(new BarEntry(Float.valueOf(jYs.getString(i)), i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "parsingBarYValues");
        }
        return entries;
    }
}
