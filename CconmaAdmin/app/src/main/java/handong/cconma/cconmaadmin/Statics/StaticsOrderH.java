package handong.cconma.cconmaadmin.Statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;

public class StaticsOrderH extends Activity {
    private LineChart pcChart;
    private LineChart mobChart;
    private ArrayList<String> xVal;
    private ArrayList<String> yVal;
    private BackPressCloseHandler backPressCloseHandler;
    private boolean mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics_order_h);

        backPressCloseHandler = new BackPressCloseHandler(this);

        StaticsOrderHManager manager = new StaticsOrderHManager(this);

        pcChart = (LineChart) findViewById(R.id.order_hourly_pc_chart);
        mobChart = (LineChart) findViewById(R.id.order_hourly_mobile_chart);

        pcChart.setDescription("pc");
        mobChart.setDescription("mobile");

        // scaling can now only be done on x- and y-axis separately
        pcChart.setPinchZoom(false);
        mobChart.setPinchZoom(false);

        pcChart.setDrawGridBackground(true);
        mobChart.setDrawGridBackground(true);

        XAxis xAxis = pcChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelsToSkip(0);
        xAxis.setDrawGridLines(false);

        XAxis xAxis2 = mobChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setLabelsToSkip(0);
        xAxis2.setDrawGridLines(false);

        //pcChart.getAxisLeft().setDrawGridLines(false);
//        mobChart.getAxisLeft().setDrawGridLines(false);

        pcChart.getAxisRight().setEnabled(false);
        mobChart.getAxisRight().setEnabled(false);

        pcChart.getLegend().setEnabled(true);
        mobChart.getLegend().setEnabled(true);

        pcChart.setData(manager.setting());
        mobChart.setData(manager.setting());

        pcChart.setDoubleTapToZoomEnabled(false);
        mobChart.setDoubleTapToZoomEnabled(false);

        pcChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    pcChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    mobChart.setVisibility(View.GONE);
                }
            }
        });

        mobChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == false) {
                    mode = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
                    mobChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    pcChart.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public class BackPressCloseHandler {
        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            Configuration config = getResources().getConfiguration();
            if (mode && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {// 가로
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 가로전환
                mode = false;
                activity.recreate();
            } else if (mode && config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 세로전환
                mode = false;
                activity.recreate();
            } else {
                activity.finish();
            }

        }
    }
}
