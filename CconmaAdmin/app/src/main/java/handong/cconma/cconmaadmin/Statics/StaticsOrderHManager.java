package handong.cconma.cconmaadmin.Statics;

import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StaticsOrderHManager {
    String dSet1 = "test";
    String dSet2;
    String dSet3;
    String dSet4;
    ArrayList<LineDataSet> sets = new ArrayList<>();
    int cnt = 0;

    public LineData setting(){
        sets.clear();
        for(int i = 0; i < 4; i++){
            generateDataLine();
        }
        return new LineData(generateXValues(), sets);
    }

    public ArrayList<String> generateXValues() {
        ArrayList<String> xVal = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            xVal.add(i + "");
        }
        return xVal;
    }

    private void generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            e1.add(new Entry((int) (Math.random() * 50), i));
        }

        LineDataSet d1 = new LineDataSet(e1, dSet1 + cnt++);
        d1.setLineWidth(2.5f);
        d1.setCircleSize(3.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setColor(ColorTemplate.VORDIPLOM_COLORS[cnt % 5]);
        d1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[cnt % 5]);
        d1.setDrawValues(false);

        sets.add(d1);
    }
}
