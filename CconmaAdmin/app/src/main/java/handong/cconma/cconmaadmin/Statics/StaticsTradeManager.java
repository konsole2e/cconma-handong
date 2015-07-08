package handong.cconma.cconmaadmin.Statics;

import android.content.Context;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;

public class StaticsTradeManager {
    Context con;

    public StaticsTradeManager(Context context) {
        con = context;
    }

    public CombinedData hourlyChartSetting() {
        CombinedData data = new CombinedData(generateXVals(24));
        data.setData(generateHourlyLineData());
        data.setData(generateHourlyBarData());

        return data;
    }

    public BarData dailyChartSetting() {
        BarData data = new BarData(generateXVals(7), generateDailyBarData());
        //data.setData(generateDailyLineData());
        data.setGroupSpace(80f);

        return data;
    }

    public LineData weeklyChartSetting() {
        ArrayList<Entry> e = new ArrayList<Entry>();

        for (int i = 0; i < 10; i++) {
            e.add(new Entry((float) Math.random() * 20, i));
        }

        LineDataSet set = new LineDataSet(e, "Weekly DataSet");
        set.setColor(con.getResources().getColor(R.color.statics_red));

        LineData data = new LineData(generateXVals(10), set);

        return data;
    }

    public LineData monthlyChartSetting() {
        ArrayList<Entry> e = new ArrayList<Entry>();

        for (int i = 0; i < 13; i++) {
            e.add(new Entry((float) Math.random() * 800, i));
        }

        LineDataSet set = new LineDataSet(e, "Monthly DataSet");
        set.setColor(con.getResources().getColor(R.color.statics_red));

        LineData data = new LineData(generateXVals(13), set);

        return data;
    }

    public LimitLine weeklkyAVG() {
        LimitLine ll = new LimitLine((float) 10f, "기간평균 (" + 10 + ")");
        ll.setLineWidth(4f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll.setTextSize(10f);

        return ll;
    }

    public LimitLine monthlyAVG() {
        LimitLine ll = new LimitLine((float) 500f, "기간평균 (" + 500 + ")");
        ll.setLineWidth(4f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll.setTextSize(10f);

        return ll;
    }


    private LineData generateHourlyLineData() {

        ArrayList<Entry> e1 = new ArrayList<>();
        ArrayList<Entry> e2 = new ArrayList<>();
        ArrayList<Entry> e3 = new ArrayList<>();
        ArrayList<Entry> e4 = new ArrayList<>();
        ArrayList<Entry> e5 = new ArrayList<>();
        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            e1.add(new Entry((float) Math.random() * 50, i));
            e2.add(new Entry((float) Math.random() * 50, i));
            e3.add(new Entry((float) Math.random() * 50, i));
            e4.add(new Entry((float) Math.random() * 50, i));
            e5.add(new Entry((float) Math.random() * 50, i));
        }

        LineDataSet set1 = new LineDataSet(e1, "Line DataSet1");
        LineDataSet set2 = new LineDataSet(e2, "Line DataSet2");
        LineDataSet set3 = new LineDataSet(e3, "Line DataSet3");
        LineDataSet set4 = new LineDataSet(e4, "Line DataSet4");
        LineDataSet set5 = new LineDataSet(e5, "Line DataSet5");

        set1.setLineWidth(2f);
        set1.setColor(con.getResources().getColor(R.color.statics_black));
        set1.setCircleColor(con.getResources().getColor(R.color.statics_black));
        set1.setCircleSize(3f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        set2.setLineWidth(2f);
        set2.setColor(con.getResources().getColor(R.color.statics_orange));
        set2.setCircleColor(con.getResources().getColor(R.color.statics_orange));
        set2.setCircleSize(3f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        set3.setLineWidth(2f);
        set3.setColor(con.getResources().getColor(R.color.statics_yellow));
        set3.setCircleColor(con.getResources().getColor(R.color.statics_yellow));
        set3.setCircleSize(3f);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);

        set4.setLineWidth(2f);
        set4.setColor(con.getResources().getColor(R.color.statics_green));
        set4.setCircleColor(con.getResources().getColor(R.color.statics_green));
        set4.setCircleSize(3f);
        set4.setAxisDependency(YAxis.AxisDependency.LEFT);

        set5.setLineWidth(2f);
        set5.setColor(con.getResources().getColor(R.color.statics_red));
        set5.setCircleColor(con.getResources().getColor(R.color.statics_red));
        set5.setCircleSize(3f);
        set5.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);

        LineData d = new LineData(generateXVals(24) ,dataSets);

        return d;
    }

    private BarData generateHourlyBarData() {
        ArrayList<BarEntry> e1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 24; i++) {
            e1.add(new BarEntry(0, i));
        }

        e1.set(10, new BarEntry(10, 10));

        BarDataSet set1 = new BarDataSet(e1, "Bar DataSet1");
        set1.setColor(con.getResources().getColor(R.color.statics_blue));
        set1.setDrawValues(false);

        /* set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);*/

        //   BarData d = new BarData();
    /*    d.addDataSet(set1);
        d.addDataSet(set2);
        d.addDataSet(set3);*/

        BarData d = new BarData();
        d.addDataSet(set1);

        return d;
    }

    private ArrayList<String> generateXVals(int numX) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < numX; i++) {
            xVals.add("" + i);
        }
        return xVals;
    }

    private ArrayList<BarDataSet> generateDailyBarData() {
        ArrayList<BarEntry> e1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> e2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> e3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> e4 = new ArrayList<BarEntry>();

        for (int i = 0; i < 7; i++) {
            e1.add(new BarEntry((float) Math.random() * 50, i));
            e2.add(new BarEntry((float) Math.random() * 50, i));
            e3.add(new BarEntry((float) Math.random() * 50, i));
            e4.add(new BarEntry((float) Math.random() * 50, i));
        }

        BarDataSet set1 = new BarDataSet(e1, "Bar DataSet1");
        set1.setColor(con.getResources().getColor(R.color.statics_blue));

        BarDataSet set2 = new BarDataSet(e2, "Bar DataSet2");
        set2.setColor(con.getResources().getColor(R.color.statics_green));

        BarDataSet set3 = new BarDataSet(e3, "Bar DataSet3");
        set3.setColor(con.getResources().getColor(R.color.statics_red));

        BarDataSet set4 = new BarDataSet(e4, "Bar DataSet4");
        set4.setColor(con.getResources().getColor(R.color.statics_gray));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);

     /* set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);*/


        //   BarData d = new BarData();
    /*    d.addDataSet(set1);
        d.addDataSet(set2);
        d.addDataSet(set3);*/

        return dataSets;
    }
}
