package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StaticsPieManager {
    private PieChart chart;
    private Context con;
    private float sum = 0f;
    private ArrayList<Entry> yVals;

    public StaticsPieManager(Context context) {
        con = context;
    }

    public PieData parsingPie(JSONObject obj, PieChart pieChart) {
        try {
            chart = pieChart;
            chartSetting(obj);
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
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "chartSetting");
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

    public PieData dataSetting(JSONArray arr) {
        PieData pieData = null;
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject data = arr.getJSONObject(i);
                String unit = "";
                String format = "";

                PieDataSet dataSet = new PieDataSet(parsingYValues(data.getJSONArray(StaticsVariables.yValues)), data.getString(StaticsVariables.label));
                dataSet.setColors(parsingColors(data.getJSONArray(StaticsVariables.pie_colors)));

                if (!data.optString(StaticsVariables.textEnable).equals("") && data.getString(StaticsVariables.textEnable).toLowerCase().equals("false")) {
                    dataSet.setDrawValues(false);
                }
          /*      if (!data.optString(StaticsVariables.axisDepend).equals("")) {
                    dataSet.setAxisDependency(YAxis.AxisDependency.valueOf(data.getString(StaticsVariables.axisDepend).toUpperCase()));
                }*/
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
                if (!data.optString(StaticsVariables.pie_sliceSpace).equals("")) {
                    dataSet.setSliceSpace(Float.valueOf(data.getString(StaticsVariables.pie_sliceSpace)));
                }
                if (!data.optString(StaticsVariables.pie_emptyCenter).equals("")) {
                    chart.setHoleRadius(Float.valueOf(data.getString(StaticsVariables.pie_emptyCenter)));
                    chart.setTransparentCircleRadius(chart.getHoleRadius() + 3f);
                }
                if (!data.optString(StaticsVariables.pie_centerText).equals("")) {
                    chart.setCenterText(data.getString(StaticsVariables.pie_centerText));
                }
                if (!data.optString(StaticsVariables.pie_centerTextSize).equals("")) {
                    chart.setCenterTextSize(Float.valueOf(data.getString(StaticsVariables.pie_centerTextSize)));
                }
                if (!data.optString(StaticsVariables.pie_centerTextColor).equals("")) {
                    chart.setCenterTextColor(Color.parseColor(data.getString(StaticsVariables.pie_centerTextColor)));
                }
                pieData = new PieData(parsingXValues(data.getJSONArray(StaticsVariables.xValues)), dataSet);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "dataSetting");
        }
        return pieData;
    }

    public ArrayList<Integer> parsingColors(JSONArray jColors) {
        ArrayList<Integer> colors = new ArrayList<>();
        try {
            for (int i = 0; i < jColors.length(); i++) {
                colors.add(Color.parseColor(jColors.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "parsingXValues");
        }
        return colors;
    }

    public ArrayList<String> parsingXValues(JSONArray jXs) {
        ArrayList<String> xValues = new ArrayList<>();
        try {
            for (int i = 0; i < jXs.length(); i++) {
                xValues.add(jXs.getString(i) + "(" + String.format("%.1f", yVals.get(i).getVal() / sum * 100) + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "parsingXValues");
        }
        return xValues;
    }

    public ArrayList<Entry> parsingYValues(JSONArray jYs) {
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            for (int i = 0; i < jYs.length(); i++) {
                float value = Float.valueOf(jYs.getString(i));
                entries.add(new Entry(value, i));
                sum += value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Parsing", "parsingYValues");
        }
        yVals = entries;
        return entries;
    }
}
