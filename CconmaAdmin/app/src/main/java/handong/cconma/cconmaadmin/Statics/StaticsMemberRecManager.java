package handong.cconma.cconmaadmin.statics;

import android.content.Context;

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

public class StaticsMemberRecManager {
    Context con;

    public StaticsMemberRecManager(Context con) {
        this.con = con;
    }

    public LineData setting(String str, JSONObject json) {

        LineData d = new LineData(generateXVals(31), generateDataLine(str, json));
        d.setValueFormatter(new StaticsValueFormatter());
        return d;
    }

    public ArrayList<String> generateXVals(int numX) {
        ArrayList<String> xVal = new ArrayList<>();
        for (int i = 0; i < numX; i++) {
            xVal.add(i + "");
        }
        return xVal;
    }

    private ArrayList<LineDataSet> generateDataLine(String str, JSONObject json) {

        ArrayList<Entry> e1 = new ArrayList<>();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int i = 0; i < 31; i++) {
            e1.add(new Entry((int) (Math.random() * 50), i));
        }

        LineDataSet d1 = new LineDataSet(e1, str);
        d1.setLineWidth(2.5f);
        d1.setCircleSize(3.5f);
        if (str.equals("PC")) {
            d1.setCircleColor(con.getResources().getColor(R.color.statics_green));
            d1.setColor(con.getResources().getColor(R.color.statics_green));
        } else {
            d1.setCircleColor(con.getResources().getColor(R.color.statics_red));
            d1.setColor(con.getResources().getColor(R.color.statics_red));
        }

        dataSets.add(d1);

        return dataSets;
    }
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
    }*/
}