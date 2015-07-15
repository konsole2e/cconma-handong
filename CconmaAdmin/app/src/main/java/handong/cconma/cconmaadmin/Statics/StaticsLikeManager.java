package handong.cconma.cconmaadmin.statics;

import android.content.Context;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

public class StaticsLikeManager {
    Context con;

    public StaticsLikeManager(Context context) {
        con = context;
    }

    public BarData dailyChartSetting(JSONObject json) {

        BarData data = new BarData(generateDailyXVals(), generateDailyBarData());
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

        LineDataSet set = new LineDataSet(e, "단골등록(주간)");
        set.setColor(con.getResources().getColor(R.color.statics_red));
        set.setLineWidth(2.5f);
        set.setCircleSize(3.5f);
        set.setCircleColor(set.getColor());

        LineData data = new LineData(generateXVals(10), set);
        data.setValueFormatter(new StaticsValueFormatter());

        return data;
    }

    public LineData monthlyChartSetting(JSONObject json) {
        ArrayList<Entry> e = new ArrayList<Entry>();

        for (int i = 0; i < 13; i++) {
            e.add(new Entry((float) Math.random() * 10000000, i));
        }

        LineDataSet set = new LineDataSet(e, "단골등록(월간)");
        set.setColor(con.getResources().getColor(R.color.statics_red));
        set.setLineWidth(2.5f);
        set.setCircleSize(3.5f);
        set.setCircleColor(set.getColor());

        LineData data = new LineData(generateXVals(13), set);
        data.setValueFormatter(new StaticsValueFormatter());

        return data;
    }

    public LimitLine weeklkyAVG() {
        LimitLine ll = new LimitLine((float) 10f, "기간평균 (" + 10 + ")");
        ll.setLineWidth(4f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll.setTextSize(10f);
        ll.setLineColor(con.getResources().getColor(R.color.statics_green));

        return ll;
    }

    public LimitLine monthlyAVG() {
        LimitLine ll = new LimitLine((float) 500f, "기간평균 (" + 500 + ")");
        ll.setLineWidth(4f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll.setTextSize(10f);
        ll.setLineColor(con.getResources().getColor(R.color.statics_green));

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


    private ArrayList<BarDataSet> generateDailyBarData() {
        ArrayList<BarEntry> e1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> e2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> e3 = new ArrayList<BarEntry>();

        for (int i = 0; i < 7; i++) {
            e1.add(new BarEntry((float) Math.random() * 50, i));
            e2.add(new BarEntry((float) Math.random() * 50, i));
            e3.add(new BarEntry((float) Math.random() * 50, i));
        }

        BarDataSet set1 = new BarDataSet(e1, "Bar DataSet1");
        set1.setColor(con.getResources().getColor(R.color.statics_green));

        BarDataSet set2 = new BarDataSet(e2, "Bar DataSet2");
        set2.setColor(con.getResources().getColor(R.color.statics_red));

        BarDataSet set3 = new BarDataSet(e3, "요일 평균");
        set3.setColor(con.getResources().getColor(R.color.statics_gray));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

     /* set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);*/


        //   BarData d = new BarData();
    /*    d.addDataSet(set1);
        d.addDataSet(set2);
        d.addDataSet(set3);*/

        return dataSets;
    }


  /*  class ConnectToUrl extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... str) {
            return getData(str[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                result = s;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle("네트워크 오류");
                builder.setMessage("데이터를 읽어 올 수 없습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }

        public boolean getData(String url) {
            try {
                String line = null;
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
                        line = line + line;
                    }
                    //  os.close();
                    is.close();
                    reader.close();
                } else {
                    return false;
                }
                return line;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }*/
/*

    public boolean getData(String[] str) {
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
    }
*/

}