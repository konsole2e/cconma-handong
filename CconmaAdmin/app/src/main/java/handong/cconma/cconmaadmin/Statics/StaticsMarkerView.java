package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import handong.cconma.cconmaadmin.R;

public class StaticsMarkerView extends MarkerView {
    private LineChart lineChart = null;
    private BarChart barChart = null;
    private CombinedChart combinedChart = null;
    private Context con;
    private LinearLayout ll;
    private int dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()); // 1dp
    private String unit = "";
    private Entry entry;
    private String left = "LABEL";
    private String skipZero = "";
    private int textSize;

    public StaticsMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        con = context;
        ll = (LinearLayout) findViewById(R.id.marker_ll);
    }

    public void attachChart(LineChart line, String leftSide, String u, String skip, int size) {
        lineChart = line;
        left = leftSide;
        unit = u;
        skipZero = skip;
        textSize = size;
    }

    public void attachChart(BarChart bar, String leftSide, String u, String skip, int size) {
        barChart = bar;
        left = leftSide;
        unit = u;
        skipZero = skip;
        textSize = size;
    }

    public void attachChart(CombinedChart combine, String leftSide, String u, String skip, int size) {
        combinedChart = combine;
        left = leftSide;
        unit = u;
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
                    tv.setText(lineChart.getXValue(eXIndex) + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                } else {
                    tv.setText(lineChart.getLineData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                }
                ll.addView(tv);
            }
        } else if (barChart != null) {
            List<Entry> set = barChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
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
                    tv.setText(barChart.getXValue(eXIndex) + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                } else {
                    tv.setText(barChart.getBarData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                }
                ll.addView(tv);
            }
        } else if (combinedChart != null) {
            List<Entry> set = combinedChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
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
                    tv.setText(combinedChart.getXValue(eXIndex) + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                } else {
                    tv.setText(combinedChart.getData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (long) set.get(i).getVal()) + unit);
                }
                ll.addView(tv);
            }
        } else {
            return;
        }
    }

    @Override
    public int getXOffset() {
        if (lineChart != null) {
            if (lineChart.getLineData().getDataSetForEntry(entry).getValueCount() == entry.getXIndex() + 1) {
                return -getWidth();
            } else if (entry.getXIndex() == 0) {
                return 0;
            }
            return -(getWidth() / 2);
        } else if (barChart != null) {
            if (barChart.getBarData().getDataSetForEntry(entry).getValueCount() == entry.getXIndex() + 1) {
                return -getWidth();
            } else if (entry.getXIndex() == 0) {
                return 0;
            }
            return -(getWidth() / 2);
        } else if (combinedChart != null) {
            if (combinedChart.getData().getDataSetForEntry(entry).getValueCount() == entry.getXIndex() + 1) {
                return -getWidth();
            } else if (entry.getXIndex() == 0) {
                return 0;
            }
            return -(getWidth() / 2);
        } else {
            return 0;
        }
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -(getHeight() + 10);
    }
}

