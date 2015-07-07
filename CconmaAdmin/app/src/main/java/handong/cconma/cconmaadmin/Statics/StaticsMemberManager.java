package handong.cconma.cconmaadmin.Statics;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;

public class StaticsMemberManager {
    Context con;
    String dSet1 = "test";
    String dSet2;
    String dSet3;
    String dSet4;

    public StaticsMemberManager(Context context) {
        con = context;
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
        set.setColor(con.getResources().getColor(R.color.statics_color_red));

        LineData data = new LineData(generateXVals(10), set);

        return data;
    }

    public LineData monthlyChartSetting() {
        ArrayList<Entry> e = new ArrayList<Entry>();

        for (int i = 0; i < 13; i++) {
            e.add(new Entry((float) Math.random() * 800, i));
        }

        LineDataSet set = new LineDataSet(e, "Monthly DataSet");
        set.setColor(con.getResources().getColor(R.color.statics_color_red));

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


/*

    public CombinedData dailyChartSetting() {
        CombinedData data = new CombinedData(generateXValues());
        data.setData(generateDailyLineData());
        data.setData(generateDailyBarData());

        return data;
    }
*/

/*    private LineData generateDailyLineData() {
        LineData d = new LineData();

        ArrayList<Entry> e = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            e.add(new Entry((float) Math.random() * 50, i));
        }

        LineDataSet set = new LineDataSet(e, "Line DataSet");

        set.setLineWidth(2.5f);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setColor(con.getResources().getColor(R.color.statics_color_gray));
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleSize(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }*/

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
        set1.setColor(con.getResources().getColor(R.color.statics_color_red));

        BarDataSet set2 = new BarDataSet(e2, "Bar DataSet2");
        set2.setColor(con.getResources().getColor(R.color.statics_color_yellow));

        BarDataSet set3 = new BarDataSet(e3, "Bar DataSet3");
        set3.setColor(con.getResources().getColor(R.color.statics_color_blue));

        BarDataSet set4 = new BarDataSet(e3, "Bar DataSet4");
        set4.setColor(con.getResources().getColor(R.color.statics_color_gray));

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
