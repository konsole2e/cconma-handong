package handong.cconma.cconmaadmin.Statics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

import handong.cconma.cconmaadmin.R;

public class StaticsMemberRecent extends Activity {
    private BackPressCloseHandler backPressCloseHandler;
    private boolean mode = false;
    private LineChart pcChart;
    private LineChart mobChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics_order_recent);
        backPressCloseHandler = new BackPressCloseHandler(this);

        StaticsMemberRecManager manager = new StaticsMemberRecManager();

        pcChart = (LineChart) findViewById(R.id.order_recent_pc_chart);
        mobChart = (LineChart) findViewById(R.id.order_recent_mobile_chart);

        pcChart.setDescription("pc");
        mobChart.setDescription("mobile");

        // scaling can now only be done on x- and y-axis separately
        pcChart.setPinchZoom(false);
        mobChart.setPinchZoom(false);

        pcChart.setDrawGridBackground(false);
        mobChart.setDrawGridBackground(false);

        XAxis xAxis = pcChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelsToSkip(0);
        xAxis.setDrawGridLines(false);

        XAxis xAxis2 = mobChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setLabelsToSkip(0);
        xAxis2.setDrawGridLines(false);

        pcChart.getAxisLeft().setDrawGridLines(false);
        mobChart.getAxisLeft().setDrawGridLines(false);

        pcChart.getAxisRight().setEnabled(false);
        mobChart.getAxisRight().setEnabled(false);

        pcChart.getLegend().setEnabled(false);
        mobChart.getLegend().setEnabled(false);

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
