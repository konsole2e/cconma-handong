package handong.cconma.cconmaadmin.statics;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;

public class StaticsOrderHManager {
    Context con;

    public StaticsOrderHManager(Context con) {
        this.con = con;
    }

    public LineData setting() {

        LineData d = new LineData(generateXValues(24), generateDataLine());
        d.setValueFormatter(new StaticsValueFormatter());

        return d;
    }

    public ArrayList<String> generateXValues(int numX) {
        ArrayList<String> xVal = new ArrayList<>();
        for (int i = 0; i < numX; i++) {
            xVal.add(i + "");
        }
        return xVal;
    }

    private ArrayList<LineDataSet> generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<>();
        ArrayList<Entry> e2 = new ArrayList<>();
        ArrayList<Entry> e3 = new ArrayList<>();
        ArrayList<Entry> e4 = new ArrayList<>();

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int i = 0; i < 24; i++) {
            e1.add(new Entry((int) (Math.random() * 50), i));
            e2.add(new Entry((int) (Math.random() * 50), i));
            e3.add(new Entry((int) (Math.random() * 50), i));
            e4.add(new Entry((int) (Math.random() * 50), i));
        }

        LineDataSet d1 = new LineDataSet(e1, "Line DataSet 1");
        d1.setLineWidth(1.5f);
        d1.setCircleSize(2f);
        d1.setCircleColor(con.getResources().getColor(R.color.statics_orange));
        d1.setColor(con.getResources().getColor(R.color.statics_orange));
        d1.setDrawValues(false);
        d1.setLabel("7월 2일(목)(1446)");

        LineDataSet d2 = new LineDataSet(e2, "Line DataSet 2");
        d2.setLineWidth(1.5f);
        d2.setCircleSize(2f);
        d2.setCircleColor(con.getResources().getColor(R.color.statics_yellow));
        d2.setColor(con.getResources().getColor(R.color.statics_yellow));
        d2.setDrawValues(false);
        d2.setLabel("7월 7일(화)(1850)");

        LineDataSet d3 = new LineDataSet(e3, "Line DataSet 3");
        d3.setLineWidth(1.5f);
        d3.setCircleSize(2f);
        d3.setCircleColor(con.getResources().getColor(R.color.statics_green));
        d3.setColor(con.getResources().getColor(R.color.statics_green));
        d3.setDrawValues(false);
        d3.setLabel("7월 8일(수)(1643)");

        LineDataSet d4 = new LineDataSet(e4, "Line DataSet 4");
        d4.setLineWidth(2f);
        d4.setCircleSize(3f);
        d4.setCircleColor(con.getResources().getColor(R.color.statics_red));
        d4.setColor(con.getResources().getColor(R.color.statics_red));
        d4.setDrawValues(true);
        d4.setLabel("7월 9일(목)(239)");

        dataSets.add(d1);
        dataSets.add(d2);
        dataSets.add(d3);
        dataSets.add(d4);

        return dataSets;
    }
}
