package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.renderer.LineChartRenderer;

import java.util.List;

import handong.cconma.cconmaadmin.R;

public class StaticsMarkerView extends MarkerView {
    private LineChart lineChart = null;
    private BarChart barChart = null;
    private CombinedChart combinedChart = null;
    private View chart;
    private Context con;
    private LinearLayout ll;
    private int dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()); // 1dp
    private Entry entry;
    private String left = "LABEL";
    private String skipZero = "";
    private int textSize;
    //   private DisplayMetrics dm;

    public StaticsMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        con = context;
        ll = (LinearLayout) findViewById(R.id.marker_ll);
        //      dm = con.getApplicationContext().getResources().getDisplayMetrics();
    }

    public void attachChart(LineChart line, String leftSide, String skip, int size) {
        lineChart = line;
        chart = line;
        left = leftSide;
        skipZero = skip;
        textSize = size;
    }

    public void attachChart(BarChart bar, String leftSide, String skip, int size) {
        barChart = bar;
        chart = bar;
        left = leftSide;
        skipZero = skip;
        textSize = size;
    }

    public void attachChart(CombinedChart combine, String leftSide, String skip, int size) {
        combinedChart = combine;
        chart = combine;
        left = leftSide;
        skipZero = skip;
        textSize = size;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        entry = e;
        int eXIndex = e.getXIndex();
        ll.removeAllViews();
        if (lineChart != null) {
            List<Entry> set = lineChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
                if (set.get(i).getXIndex() != eXIndex) {
                    continue;
                }
                if ((long) set.get(i).getVal() == 0) {
                    if (skipZero.equals("all")) {
                        continue;
                    } else if (skipZero.equals("line")) {
                        continue;
                    } else if (skipZero.equals("bar") && set.get(i) instanceof BarEntry) {
                        continue;
                    }
                }
                TextView tv = new TextView(con);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setPadding(dpInPx, dpInPx, 0, dpInPx);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                tv.setLayoutParams(params);
                tv.setTextColor(lineChart.getLineData().getDataSetByIndex(i).getColor());
                if (left.equals("INDEX")) {
                    tv.setText(lineChart.getXValue(eXIndex) + " : " + lineChart.getLineData().getDataSetByIndex(i).getValueFormatter().getFormattedValue(set.get(i).getVal()));
                    //tv.setText(lineChart.getXValue(eXIndex) + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                } else {
                    tv.setText(lineChart.getLineData().getDataSetByIndex(i).getLabel() + " : " + lineChart.getLineData().getDataSetByIndex(i).getValueFormatter().getFormattedValue(set.get(i).getVal()));
                    //tv.setText(lineChart.getLineData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                }
                ll.addView(tv);
            }
        } else if (barChart != null) {
            List<Entry> set = barChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
                if (set.get(i).getXIndex() != eXIndex) {
                    continue;
                }
                if ((long) set.get(i).getVal() == 0) {
                    if (skipZero.equals("all")) {
                        continue;
                    } else if (skipZero.equals("line")) {
                        continue;
                    } else if (skipZero.equals("bar") && set.get(i) instanceof BarEntry) {
                        continue;
                    }
                }
                TextView tv = new TextView(con);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setPadding(dpInPx, dpInPx, 0, dpInPx);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                tv.setLayoutParams(params);
                tv.setTextColor(barChart.getBarData().getDataSetByIndex(i).getColor());
                if (left.equals("INDEX")) {
                    tv.setText(barChart.getXValue(eXIndex) + " : " + barChart.getBarData().getDataSetByIndex(i).getValueFormatter().getFormattedValue(set.get(i).getVal()));
//                    tv.setText(barChart.getXValue(eXIndex) + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                } else {
                    tv.setText(barChart.getBarData().getDataSetByIndex(i).getLabel() + " : " + barChart.getBarData().getDataSetByIndex(i).getValueFormatter().getFormattedValue(set.get(i).getVal()));
//                    tv.setText(barChart.getBarData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                }
                ll.addView(tv);
            }
        } else if (combinedChart != null) {
            List<Entry> set = combinedChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
                if (set.get(i).getXIndex() != eXIndex) {
                    continue;
                }
                if ((long) set.get(i).getVal() == 0) {
                    if (skipZero.equals("all")) {
                        continue;
                    } else if (skipZero.equals("line")) {
                        continue;
                    } else if (skipZero.equals("bar") && set.get(i) instanceof BarEntry) {
                        continue;
                    }
                }
                TextView tv = new TextView(con);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setPadding(dpInPx, dpInPx, 0, dpInPx);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                tv.setLayoutParams(params);
                tv.setTextColor(combinedChart.getData().getDataSetByIndex(i).getColor());
                if (left.equals("INDEX")) {
                    tv.setText(combinedChart.getXValue(eXIndex) + " : " + combinedChart.getData().getDataSetByIndex(i).getValueFormatter().getFormattedValue(set.get(i).getVal()));
//                    tv.setText(combinedChart.getXValue(eXIndex) + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                } else {
                    tv.setText(combinedChart.getData().getDataSetByIndex(i).getLabel() + " : " + combinedChart.getData().getDataSetByIndex(i).getValueFormatter().getFormattedValue(set.get(i).getVal()));
//                    tv.setText(combinedChart.getData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                }
                ll.addView(tv);
            }
        } else {
            return;
        }
    }

    @Override
    public int getXOffset() {
        int w = getWidth();
        int l = 0;
        int r = 0;
        float posX = 0f;

        if (lineChart != null) {
            posX = ((LineChart) chart).getPosition(entry, ((LineChart) chart).getData().getDataSetForEntry(entry).getAxisDependency()).x;
            l = ((LineChart) chart).getLeft();
            r = ((LineChart) chart).getRight();
            //int dmp = dm.widthPixels;
        } else if (barChart != null) {
            posX = ((BarChart) chart).getPosition(entry, ((BarChart) chart).getBarData().getDataSetForEntry(entry).getAxisDependency()).x;
            l = ((BarChart) chart).getLeft();
            r = ((BarChart) chart).getRight();
        } else if (combinedChart != null) {
            posX = ((CombinedChart) chart).getPosition(entry, ((CombinedChart) chart).getData().getDataSetForEntry(entry).getAxisDependency()).x;
            l = ((CombinedChart) chart).getLeft();
            r = ((CombinedChart) chart).getRight();
        } else {
            return 0;
        }

        if (posX - w / 2 <= l) {
            return 0;
        } else if (posX + w / 2 >= r) {
            return -w;
        }
        return -(w / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -(getHeight() + 10);
    }
}

