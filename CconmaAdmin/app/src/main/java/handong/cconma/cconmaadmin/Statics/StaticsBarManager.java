package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;

public class StaticsBarManager {
    private BarChart chart;
    private Context con;

    public StaticsBarManager(Context context) {
        con = context;
    }

    public BarData parsingBar(JSONObject obj, BarChart barChart) {
        try {
            chart = barChart;
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
        }
    }

    public void xAxisSetting(JSONObject xAxis) {
        try {
            if (xAxis.optString(StaticsVariables.enabled).toLowerCase().equals("false")) {
                chart.getXAxis().setEnabled(false);
            }
            if (!xAxis.optString(StaticsVariables.axisWidth).equals("")) {
                chart.getXAxis().setAxisLineWidth(Float.valueOf(xAxis.getString(StaticsVariables.axisWidth)));
            }
            if (!xAxis.optString(StaticsVariables.axisColor).equals("")) {
                chart.getXAxis().setAxisLineColor(Color.parseColor(xAxis.getString(StaticsVariables.axisColor)));
            }
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
        }
    }

    public void leftYAxisSetting(JSONObject yAxis) {
        String unit = "";
        String format = "";
        try {
            if (yAxis.optString(StaticsVariables.enabled).toLowerCase().equals("false")) {
                chart.getAxisLeft().setEnabled(false);
            }
            if (!yAxis.optString(StaticsVariables.axisWidth).equals("")) {
                chart.getAxisLeft().setAxisLineWidth(Float.valueOf(yAxis.getString(StaticsVariables.axisWidth)));
            }
            if (!yAxis.optString(StaticsVariables.axisColor).equals("")) {
                chart.getAxisLeft().setAxisLineColor(Color.parseColor(yAxis.getString(StaticsVariables.axisColor)));
            }
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
                chart.getAxisLeft().setStartAtZero(false);
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
                unit = yAxis.getString(StaticsVariables.unit);
            }
            if (!yAxis.optString(StaticsVariables.valueFormat).equals("")) {
                format = yAxis.getString(StaticsVariables.valueFormat);
            }
            chart.getAxisLeft().setValueFormatter(new StaticsValueFormatter(format, unit));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void rightYAxisSetting(JSONObject yAxis) {
        String unit = "";
        String format = "";
        try {
            if (yAxis.optString(StaticsVariables.enabled).toLowerCase().equals("false")) {
                chart.getAxisRight().setEnabled(false);
            }
            if (!yAxis.optString(StaticsVariables.axisWidth).equals("")) {
                chart.getAxisRight().setAxisLineWidth(Float.valueOf(yAxis.getString(StaticsVariables.axisWidth)));
            }
            if (!yAxis.optString(StaticsVariables.axisColor).equals("")) {
                chart.getAxisRight().setAxisLineColor(Color.parseColor(yAxis.getString(StaticsVariables.axisColor)));
            }
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
                chart.getAxisRight().setStartAtZero(false);
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
                unit = yAxis.getString(StaticsVariables.unit);
            }
            if (!yAxis.optString(StaticsVariables.valueFormat).equals("")) {
                format = yAxis.getString(StaticsVariables.valueFormat);
            }
            chart.getAxisRight().setValueFormatter(new StaticsValueFormatter(format, unit));
        } catch (JSONException e) {
            e.printStackTrace();
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
        }
    }

    public void toolTipSetting(JSONObject tt) {
        String mLeft = "LABEL";
        String mSkipZero = "";
        int mTextSize = 9;
        try {
            if (!tt.optString(StaticsVariables.leftSide).equals("")) {
                if (tt.getString(StaticsVariables.leftSide).toUpperCase().equals("INDEX")) {
                    mLeft = tt.getString(StaticsVariables.leftSide);
                }
            }
            if (!tt.optString(StaticsVariables.skipZero).equals("")) {
                mSkipZero = tt.getString(StaticsVariables.skipZero).toLowerCase();
            }
            if (!tt.optString(StaticsVariables.textSize).equals("")) {
                mTextSize = Integer.valueOf(tt.getString(StaticsVariables.textSize));
            }
            StaticsMarkerView mv = new StaticsMarkerView(con, R.layout.statics_marker_view_layout);
            mv.attachChart(chart, mLeft, mSkipZero, mTextSize);
            chart.setMarkerView(mv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public BarData dataSetting(JSONArray arr) {
        BarData barData = null;
        float mBarGroupSpace = -1f;

        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject data = arr.getJSONObject(i);
                String unit = "";
                String format = "";
                if (barData == null) {
                    barData = new BarData(parsingXValues(data.getJSONArray(StaticsVariables.xValues)));
                }

                BarDataSet dataSet = new BarDataSet(parsingYValues(data.getJSONArray(StaticsVariables.yValues)), data.getString(StaticsVariables.label));
                dataSet.setColor(Color.parseColor(data.getString(StaticsVariables.color)));

                if (!data.optString(StaticsVariables.textEnable).equals("") && data.getString(StaticsVariables.textEnable).toLowerCase().equals("false")) {
                    dataSet.setDrawValues(false);
                }
                if (!data.optString(StaticsVariables.axisDepend).equals("")) {
                    dataSet.setAxisDependency(YAxis.AxisDependency.valueOf(data.getString(StaticsVariables.axisDepend).toUpperCase()));
                }
                if (!data.optString(StaticsVariables.textColor).equals("")) {
                    dataSet.setValueTextColor(Color.parseColor(data.getString(StaticsVariables.textColor)));
                }
                if (!data.optString(StaticsVariables.textSize).equals("")) {
                    dataSet.setValueTextSize(Float.valueOf(data.getString(StaticsVariables.textSize)));
                }
                if (!data.optString(StaticsVariables.unit).equals("")) {
                    unit = data.getString(StaticsVariables.unit);
                }
                if (!data.optString(StaticsVariables.valueFormat).equals("")) {
                    format = data.getString(StaticsVariables.valueFormat);
                }
                dataSet.setValueFormatter(new StaticsValueFormatter(format, unit));
                if (!data.optString(StaticsVariables.bar_space).equals("")) {
                    dataSet.setBarSpacePercent(Float.valueOf(data.getString(StaticsVariables.bar_space)));
                }
                if (Float.compare(mBarGroupSpace, -1f) == 0 && !data.optString(StaticsVariables.bar_groupSpace).equals("")) {
                    mBarGroupSpace = Float.valueOf(data.getString(StaticsVariables.bar_groupSpace));
                    barData.setGroupSpace(mBarGroupSpace);
                }
                barData.addDataSet(dataSet);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return barData;
    }

    public ArrayList<String> parsingXValues(JSONArray jXs) {
        ArrayList<String> xValues = new ArrayList<>();
        try {
            for (int i = 0; i < jXs.length(); i++) {
                xValues.add(jXs.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return xValues;
    }

    public ArrayList<BarEntry> parsingYValues(JSONArray jYs) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        try {
            for (int i = 0; i < jYs.length(); i++) {
                entries.add(new BarEntry(Float.valueOf(jYs.getString(i)), i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entries;
    }
}
