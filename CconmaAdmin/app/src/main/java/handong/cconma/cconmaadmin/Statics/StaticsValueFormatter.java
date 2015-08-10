package handong.cconma.cconmaadmin.statics;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.FormatFlagsConversionMismatchException;

public class StaticsValueFormatter implements ValueFormatter {
    private String unit = "";
    private String format = "";

    public StaticsValueFormatter(String mFormat, String mUnit) {
        if (mFormat.equals("")) {
            mFormat = "%,d";
        }
        format = mFormat;
        unit = mUnit;
    }

    @Override
    public String getFormattedValue(float value) {
        try {
            if (format.contains("f")) {
                return String.format(format, value) + unit;
            } else {
                format = format.replace(".", "");
                if (format.contains("d")) {
                    return String.format(format, (long) value) + unit;
                } else {
                    format = format.replace(",", "").replace("+", "");
                    return String.format(format, (long) value) + unit;
                }
            }
        }catch (FormatFlagsConversionMismatchException e){
            e.printStackTrace();
            return String.format("%,d", (long) value) + unit;
        }
    }
}
