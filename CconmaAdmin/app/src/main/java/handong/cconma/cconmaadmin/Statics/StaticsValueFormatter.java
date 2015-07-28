package handong.cconma.cconmaadmin.statics;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class StaticsValueFormatter implements ValueFormatter {
    private String unit = "";
    private DecimalFormat mFormat;

    public StaticsValueFormatter() {
        mFormat = new DecimalFormat("###,###,###"); // use one decimal
    }


    public StaticsValueFormatter(String str) {
        mFormat = new DecimalFormat("###,###,###"); // use one decimal
        unit = str;
    }


    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + unit;

    }
}
