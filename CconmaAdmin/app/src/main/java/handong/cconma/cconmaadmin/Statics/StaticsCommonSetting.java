package handong.cconma.cconmaadmin.Statics;

import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;

/**
 * Created by JS on 2015-07-09.
 */
public class StaticsCommonSetting {
    private LineChart lineChart;
    private BarChart barChart;
    private CombinedChart combinedChart;

    public void commonSetting(LineChart line) {
        lineChart = line;
        // 차트 관련 : 설명, 확대, 격자
        lineChart.setDescription("");
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        // 축 관련
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setLabelsToSkip(0);
        lineChart.getXAxis().setDrawGridLines(false);
        // 범례 관련
        lineChart.getLegend().setEnabled(true);
    }

    public void zoomSetting(LineChart line) {
        line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        line.setScaleEnabled(true);
        line.setPinchZoom(true);
    }

    public BarChart commonSetting(BarChart bar) {
        barChart = bar;
        // 차트 관련 : 설명, 확대, 격자
        barChart.setDescription("");
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setDoubleTapToZoomEnabled(false);
        // 축 관련
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        // 범례 관련
        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        return barChart;
    }

    public void zoomSetting(BarChart bar) {
        bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        bar.setScaleEnabled(true);
        bar.setPinchZoom(true);
    }

    public CombinedChart commonSetting(CombinedChart combine) {
        combinedChart = combine;
        // 차트 관련 : 설명, 확대, 격자
        combinedChart.setDescription("");
        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});
        combinedChart.setScaleEnabled(false);
        combinedChart.setPinchZoom(false);
        combinedChart.setDrawGridBackground(true);
        combinedChart.setDoubleTapToZoomEnabled(false);
        // 축 관련
        combinedChart.getAxisRight().setEnabled(false);
        combinedChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        combinedChart.getXAxis().setDrawGridLines(false);
        combinedChart.getXAxis().setLabelsToSkip(0);
        // 범례 관련
        combinedChart.getLegend().setEnabled(true);
        combinedChart.getLegend().setFormSize(5f);
        combinedChart.getLegend().setFormToTextSpace(2);
        combinedChart.getLegend().setTextSize(7f);
        combinedChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        return combinedChart;
    }

    public void zoomSetting(CombinedChart combine) {
        combine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        combine.setScaleEnabled(true);
        combine.setPinchZoom(true);
    }
}
