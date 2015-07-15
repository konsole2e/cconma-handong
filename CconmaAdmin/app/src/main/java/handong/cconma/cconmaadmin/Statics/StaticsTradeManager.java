package handong.cconma.cconmaadmin.statics;

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

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;

public class StaticsTradeManager {
    Context con;
    ArrayList<String> result;

    public StaticsTradeManager(Context context) {
        con = context;
    }

    public CombinedData hourlyChartSetting(JSONObject json) {
        CombinedData data = new CombinedData(generateXVals(24));
        data.setData(generateHourlyLineData(json));
        data.setData(generateHourlyBarData(json));
        data.setValueFormatter(new StaticsValueFormatter());
        return data;
    }

    public BarData dailyChartSetting(JSONObject json) {
        BarData data = new BarData(generateDailyXVals(), generateDailyBarData(json));
        //data.setData(generateDailyLineData());
        data.setGroupSpace(80f);
        data.setValueFormatter(new StaticsValueFormatter());
        return data;
    }

    public LineData weeklyChartSetting(JSONObject json) {
        ArrayList<Entry> e = new ArrayList<Entry>();

        for (int i = 0; i < 10; i++) {
            e.add(new Entry((float) Math.random() * 20, i));
        }

        LineDataSet set = new LineDataSet(e, "거래액(주간)");
        set.setColor(con.getResources().getColor(R.color.statics_red));
        set.setCircleColor(set.getColor());
        set.setLineWidth(2.5f);
        set.setCircleSize(3.5f);

        LineData data = new LineData(generateXVals(10), set);
        data.setValueFormatter(new StaticsValueFormatter());
        return data;
    }

    public LineData monthlyChartSetting(JSONObject json) {
        ArrayList<Entry> e = new ArrayList<Entry>();

        for (int i = 0; i < 13; i++) {
            e.add(new Entry((float) Math.random() * 800, i));
        }

        LineDataSet set = new LineDataSet(e, "거래액(월간)");
        set.setColor(con.getResources().getColor(R.color.statics_red));
        set.setCircleColor(set.getColor());
        set.setLineWidth(2.5f);
        set.setCircleSize(3.5f);


        LineData data = new LineData(generateXVals(13), set);
        data.setValueFormatter(new StaticsValueFormatter());
        return data;
    }

    public LimitLine weeklkyAVG() {
        LimitLine ll = new LimitLine((float) 10f, "기간평균 (" + 10 + ")");
        ll.setLineWidth(4f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll.setLineColor(con.getResources().getColor(R.color.statics_green));
        ll.setTextSize(10f);

        return ll;
    }

    public LimitLine monthlyAVG() {
        LimitLine ll = new LimitLine((float) 500f, "기간평균 (" + 500 + ")");
        ll.setLineWidth(4f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll.setLineColor(con.getResources().getColor(R.color.statics_green));
        ll.setTextSize(10f);

        return ll;
    }


    private LineData generateHourlyLineData(JSONObject json) {

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

        LineDataSet set1 = new LineDataSet(e1, "90일 평균");
        LineDataSet set2 = new LineDataSet(e2, "Line DataSet2");
        LineDataSet set3 = new LineDataSet(e3, "Line DataSet3");
        LineDataSet set4 = new LineDataSet(e4, "Line DataSet4");
        LineDataSet set5 = new LineDataSet(e5, "Line DataSet5");

        set1.setLineWidth(1.5f);
        set1.enableDashedLine(5f, 5f, 0);
        set1.setColor(con.getResources().getColor(R.color.black));
        set1.setCircleColor(con.getResources().getColor(R.color.black));
        set1.setCircleSize(2f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        set2.setLineWidth(1.5f);
        set2.setColor(con.getResources().getColor(R.color.statics_orange));
        set2.setCircleColor(con.getResources().getColor(R.color.statics_orange));
        set2.setCircleSize(2f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        set3.setLineWidth(1.5f);
        set3.setColor(con.getResources().getColor(R.color.statics_yellow));
        set3.setCircleColor(con.getResources().getColor(R.color.statics_yellow));
        set3.setCircleSize(2f);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);

        set4.setLineWidth(1.5f);
        set4.setColor(con.getResources().getColor(R.color.statics_green));
        set4.setCircleColor(con.getResources().getColor(R.color.statics_green));
        set4.setCircleSize(2f);
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

        LineData d = new LineData(generateXVals(24), dataSets);
        d.setValueFormatter(new StaticsValueFormatter());
        return d;
    }

    private BarData generateHourlyBarData(JSONObject json) {
        ArrayList<BarEntry> e1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 24; i++) {
            e1.add(new BarEntry(0, i));
        }

        e1.set(10, new BarEntry(10, 10));

        BarDataSet set1 = new BarDataSet(e1, "현재 시각");
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
        d.setValueFormatter(new StaticsValueFormatter());
        return d;
    }

    private String[] generateDailyXVals() {
        String[] xVals = {"일", "월", "화", "수", "목", "금", "토"};

        return xVals;
    }

    private ArrayList<String> generateXVals(int numX) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < numX; i++) {
            xVals.add("" + i);
        }
        return xVals;
    }

    private ArrayList<BarDataSet> generateDailyBarData(JSONObject json) {
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

        BarDataSet set4 = new BarDataSet(e4, "요일 평균");
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

    /*public boolean getData(String[] str) {
        for (String url : str) {
            try {
                String line = null;
                String appended = null;
                HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
                con.setRequestMethod("GET");
                //con.setDoOutput(true);
                con.setDoInput(true);
                BufferedInputStream is;
                //BufferedOutputStream os;

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //os = new BufferedInputStream(con.getOutputStream());
                    is = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while ((line = reader.readLine()) != null) {
                        appended = appended + line;
                    }
                    //  os.close();
                    is.close();
                    reader.close();
                    result = new ArrayList<>();
                    result.add(appended);
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }*/
}
