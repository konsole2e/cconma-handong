package handong.cconma.cconmaadmin.statics;

import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by JS on 2015-07-09.
 */
public class StaticsCommonSetting {

    public void commonSetting(PieChart pie) {
        // 차트에 설명문구 제거
        pie.setDescription("");
        pie.setTouchEnabled(true);
        // 세로 모드에서 Tooltip 금지
//        pie.setDrawMarkerViews(false);
        // 세로 모드에서 차트 조작 불가
        //pie.setTouchEnabled(false);
        // 세로 모드에서 하이라이트 제거
        //   pie.highlightValues(null);
    }

  /*  public void zoomSetting(PieChart pie) {
        pie.setVisibility(View.VISIBLE);
        pie.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // 가로 모드에서 차트 조작 가능
    }*/

    public void commonSetting(LineChart line) {
        // 차트에 설명문구 제거
        line.setDescription("");
        // 확대(두번 터치) 금지
        line.setDoubleTapToZoomEnabled(false);
        // 확대 금지
        line.setScaleEnabled(false);

        // 차트 드래그 가속도 비활성화
//        line.setDragDecelerationEnabled(false);
        // 차트 확대 상태 초기화
//        line.fitScreen();
        // 세로 모드에서 확대 금지
//        line.setScaleEnabled(false);
        // 세로 모드에서 확대(pinch) 금지
//        line.setPinchZoom(false);
        // 세로 모드에서 Tooltip 금지
//        line.setDrawMarkerViews(false);
        // 세로 모드에서 확대(pinch) 가능
        //line.setPinchZoom(true);
    }

    /*public void zoomSetting(LineChart line) {
        line.setVisibility(View.VISIBLE);
        line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // 차트 드래그 가능
        line.setDragEnabled(true);
        // 가로 모드에서 확대 가능
        line.setScaleEnabled(true);
        // 가로 모드에서 확대(pinch) 가능
        line.setPinchZoom(true);
        // 가로 모드에서 Tooltip 가능
        line.setDrawMarkerViews(true);
    }*/

    public BarChart commonSetting(BarChart bar) {
        // 차트에 설명문구 제거
        bar.setDescription("");
        // 세로 모드에서 확대(두번 터치) 금지
        bar.setDoubleTapToZoomEnabled(false);
        // 확대 금지
        bar.setScaleEnabled(false);

        // 차트 드래그 가속도 비활성화
//        bar.setDragDecelerationEnabled(false);
        // 차트 확대 상태 초기화
//        bar.fitScreen();
        // 세로 모드에서 확대 금지
//        bar.setScaleEnabled(false);
        // 세로 모드에서 확대(pinch) 금지
//        bar.setPinchZoom(false);
        // 세로 모드에서 Tooltip 금지
//        bar.setDrawMarkerViews(false);
        // 세로 모드에서 확대(pinch) 가능
        //bar.setPinchZoom(true);
        return bar;
    }

   /* public void zoomSetting(BarChart bar) {
        bar.setVisibility(View.VISIBLE);
        bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // 차트 드래그 가능
        bar.setDragEnabled(true);

        // 가로 모드에서 확대(pinch) 가능
        bar.setPinchZoom(true);
        // 가로 모드에서 Tooltip 가능
        bar.setDrawMarkerViews(true);
    }*/

    public CombinedChart commonSetting(CombinedChart combine) {
        // 차트에 설명문구 제거
        combine.setDescription("");
        // 세로 모드에서 확대(두번 터치) 금지
        combine.setDoubleTapToZoomEnabled(false);
        // 확대 금지
        combine.setScaleEnabled(false);


        // 차트 드래그 가속도 비활성화
//        combine.setDragDecelerationEnabled(false);
        // 차트 확대 상태 초기화
//        combine.fitScreen();
        // 세로 모드에서 확대 금지
        //       combine.setScaleEnabled(false);
        // 세로 모드에서 확대(pinch) 금지
//        combine.setPinchZoom(false);
        // 세로 모드에서 Tooltip 금지
//        combine.setDrawMarkerViews(false);
        // 세로 모드에서 확대(pinch) 가능
//        combine.setPinchZoom(true);
        return combine;
    }

  /*  public void zoomSetting(CombinedChart combine) {
        combine.setVisibility(View.VISIBLE);
        combine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // 차트 드래그 가능
        combine.setDragEnabled(true);
        // 가로 모드에서 확대 가능
        // 가로 모드에서 Tooltip 가능
        combine.setDrawMarkerViews(true);
    }*/
}
