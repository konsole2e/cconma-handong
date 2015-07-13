package handong.cconma.cconmaadmin.statics;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class StaticsValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public StaticsValueFormatter() {
        mFormat = new DecimalFormat("###,###,###"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);

    }
}
