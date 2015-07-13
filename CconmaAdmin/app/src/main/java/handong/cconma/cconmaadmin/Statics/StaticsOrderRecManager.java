package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import handong.cconma.cconmaadmin.R;

public class StaticsOrderRecManager {
    Context con;

    public StaticsOrderRecManager(Context con) {
        this.con = con;
    }

    public LineData setting(String str){

        LineData d = new LineData(generateXVals(31), generateDataLine(str));
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

    private ArrayList<LineDataSet> generateDataLine(String str) {

        ArrayList<Entry> e1 = new ArrayList<>();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int i = 0; i < 31; i++) {
            e1.add(new Entry((int) (Math.random() * 50), i));
        }

        LineDataSet d1 = new LineDataSet(e1, "Line DataSet " + str.toUpperCase());
        d1.setLineWidth(2.5f);
        d1.setCircleSize(3.5f);
        if(str.equals("pc")){
            d1.setCircleColor(con.getResources().getColor(R.color.statics_green));
            d1.setColor(con.getResources().getColor(R.color.statics_green));
        }else{
            d1.setCircleColor(con.getResources().getColor(R.color.statics_red));
            d1.setColor(con.getResources().getColor(R.color.statics_red));
        }

        dataSets.add(d1);

        return dataSets;
    }
}
