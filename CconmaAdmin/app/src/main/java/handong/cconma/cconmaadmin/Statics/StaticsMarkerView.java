package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import handong.cconma.cconmaadmin.R;

public class StaticsMarkerView extends MarkerView {
    private LineChart lineChart = null;
    private BarChart barChart = null;
    private CombinedChart combinedChart = null;
    /*    private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;*/
    private ArrayList<TextView> tvs;
    private String unit;
    private Entry entry;

    public StaticsMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
     /*   tv1 = (TextView) findViewById(R.id.marker_tv1);
        tv2 = (TextView) findViewById(R.id.marker_tv2);
        tv3 = (TextView) findViewById(R.id.marker_tv3);
        tv4 = (TextView) findViewById(R.id.marker_tv4);
        tv5 = (TextView) findViewById(R.id.marker_tv5);
        tv6 = (TextView) findViewById(R.id.marker_tv6);*/
        tvs = new ArrayList<>();
        tvs.add((TextView) findViewById(R.id.marker_tv1));
        tvs.add((TextView) findViewById(R.id.marker_tv2));
        tvs.add((TextView) findViewById(R.id.marker_tv3));
        tvs.add((TextView) findViewById(R.id.marker_tv4));
        tvs.add((TextView) findViewById(R.id.marker_tv5));
        tvs.add((TextView) findViewById(R.id.marker_tv6));
    }

    public void attachChart(LineChart line, String str) {
        lineChart = line;
        unit = str;
    }

    public void attachChart(BarChart bar, String str) {
        barChart = bar;
        unit = str;
    }

    public void attachChart(CombinedChart combine, String str) {
        combinedChart = combine;
        unit = str;
    }
/*
    public void invisible(){
        for(int i = 0; i < tvs.size(); i++){
            tvs.get(i).setVisibility(GONE);
        }
    }*/

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        entry = e;
        int eXIndex = e.getXIndex();
        //    invisible();
        if (lineChart != null) {
            List<Entry> set = lineChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
                TextView tv = tvs.get(i);
                tv.setVisibility(VISIBLE);
                tv.setTextColor(lineChart.getLineData().getDataSetByIndex(i).getColor());
                tv.setText(lineChart.getLineData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (int) set.get(i).getVal()) + unit);
            }
        } else if (barChart != null) {
            List<Entry> set = barChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
                TextView tv = tvs.get(i);
                tv.setVisibility(VISIBLE);
                tv.setTextColor(barChart.getBarData().getDataSetByIndex(i).getColor());
                tv.setText(barChart.getBarData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (int) set.get(i).getVal()) + unit);
            }
        } else if (combinedChart != null) {
            List<Entry> set = combinedChart.getEntriesAtIndex(eXIndex);
            for (int i = 0; i < set.size(); i++) {
                TextView tv = tvs.get(i);
                if (combinedChart.getData().getDataSetByIndex(i).getLabel().equals("현재 시각") && (int) set.get(i).getVal() == 0) {
                    tv.setVisibility(GONE);
                    continue;
                }
                tv.setVisibility(VISIBLE);
                tv.setTextColor(combinedChart.getData().getDataSetByIndex(i).getColor());
                tv.setText(combinedChart.getData().getDataSetByIndex(i).getLabel() + " : " + String.format("%,d", (int) set.get(i).getVal()) + unit);
            }
        } else {
            return;
        }
    /*    tv1.setText("" + e.getVal()); // set the entry-value as the display text
        tv2.setText("" + e.getVal());
        tv3.setText("" + e.getVal());
        tv4.setText("" + e.getVal());
        tv5.setText("" + e.getVal());*/
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

