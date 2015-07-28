package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;

import handong.cconma.cconmaadmin.R;

public class StaticsMarkerViewRecent extends MarkerView {
    private LineChart lineChart = null;
    private TextView tv1;
    /*  private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;*/
//    private ArrayList<TextView> tvs;
    private String unit;
    private Entry entry;

    public StaticsMarkerViewRecent(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
      //  tv1 = (TextView) findViewById(R.id.marker_tv1);
     /*
        tv2 = (TextView) findViewById(R.id.marker_tv2);
        tv3 = (TextView) findViewById(R.id.marker_tv3);
        tv4 = (TextView) findViewById(R.id.marker_tv4);
        tv5 = (TextView) findViewById(R.id.marker_tv5);
        tv6 = (TextView) findViewById(R.id.marker_tv6);*/
    /*    tvs = new ArrayList<>();
        tvs.add((TextView) findViewById(R.id.marker_tv1));*/
        /*
        tvs.add((TextView) findViewById(R.id.marker_tv2));
        tvs.add((TextView) findViewById(R.id.marker_tv3));
        tvs.add((TextView) findViewById(R.id.marker_tv4));
        tvs.add((TextView) findViewById(R.id.marker_tv5));
        tvs.add((TextView) findViewById(R.id.marker_tv6));*/
    }

    public void attachChart(LineChart line, String str) {
        lineChart = line;
        unit = str;
    }

 /*   public void attachChart(BarChart bar, String str) {
        barChart = bar;
        unit = str;
    }

    public void attachChart(CombinedChart combine, String str) {
        combinedChart = combine;
        unit = str;
    }*/

   /* public void invisible() {
        for (int i = 0; i < tvs.size(); i++) {
            tvs.get(i).setVisibility(GONE);
        }
    }*/

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        entry = e;
        tv1.setVisibility(VISIBLE);
        tv1.setTextColor(lineChart.getLineData().getDataSetForEntry(e).getColor());
        tv1.setText(lineChart.getXValue(e.getXIndex()) + " : " + String.format("%,d", (long) e.getVal()) + unit);

/*        int eXIndex = e.getXIndex();
//        invisible();
        List<Entry> set = lineChart.getEntriesAtIndex(eXIndex);
        for (int i = 0; i < set.size(); i++) {
            TextView tv = tvs.get(i);
            tv.setVisibility(VISIBLE);
            tv.setTextColor(lineChart.getLineData().getDataSetByIndex(i).getColor());
            tv.setText(lineChart.getLineData().getDataSetByIndex(i).getLabel() + " : " + set.get(i).getVal() + unit);
        }
*/
    /*
    // set the entry-value as the display text
        tv2.setText("" + e.getVal());
        tv3.setText("" + e.getVal());
        tv4.setText("" + e.getVal());
        tv5.setText("" + e.getVal());*/
    }

    @Override
    public int getXOffset() {
        if (lineChart.getLineData().getDataSetForEntry(entry).getValueCount() == entry.getXIndex() + 1) {
            return -getWidth();
        } else if (entry.getXIndex() == 0) {
            return 0;
        }
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -(getHeight() + 10);
    }
}

