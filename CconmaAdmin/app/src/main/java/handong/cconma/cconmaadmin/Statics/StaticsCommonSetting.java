package handong.cconma.cconmaadmin.statics;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

public class StaticsCommonSetting {

    public void commonSetting(PieChart pie) {
        // 차트에 설명문구 제거
        pie.setDescription("");
        pie.setTouchEnabled(true);
    }

    public void commonSetting(LineChart line) {
        // 차트에 설명문구 제거
        line.setDescription("");
        // 확대(두번 터치) 금지
        line.setDoubleTapToZoomEnabled(false);
        // 확대 금지
        line.setScaleEnabled(false);
    }

    public BarChart commonSetting(BarChart bar) {
        // 차트에 설명문구 제거
        bar.setDescription("");
        // 세로 모드에서 확대(두번 터치) 금지
        bar.setDoubleTapToZoomEnabled(false);
        // 확대 금지
        bar.setScaleEnabled(false);
        return bar;
    }

    public CombinedChart commonSetting(CombinedChart combine) {
        // 차트에 설명문구 제거
        combine.setDescription("");
        // 세로 모드에서 확대(두번 터치) 금지
        combine.setDoubleTapToZoomEnabled(false);
        // 확대 금지
        combine.setScaleEnabled(false);
        return combine;
    }
}
