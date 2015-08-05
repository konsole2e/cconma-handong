package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.http.HttpConnection;

import static android.widget.RelativeLayout.CENTER_VERTICAL;

public class StaticsFragment extends Fragment {
    public static final String ARG_CHART_PATH = "ARG_PAGE_PATH";
    private String mChartPath;
    private View view;
    private LinearLayout ll;
    private JSONObject result = null;
    private ArrayList<View> charts;
    private StaticsCommonSetting setting;
    private Activity thisActivity;
    private ProgressDialog pd;

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.statics_reload && !pd.isShowing()) {
            gone();
            new StaticsAsyncTask("http://www.cconma.com" + mChartPath, "GET", "").execute();
        } else if (id == R.id.statics_zoom && !pd.isShowing()) {
            Configuration config = getResources().getConfiguration();
            if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {// 세로
                thisActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); // 가로전환
            } else {
                thisActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 가로전환
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static StaticsFragment newInstance(String chartPath) {
        Bundle args = new Bundle();
        args.putString(ARG_CHART_PATH, chartPath);
        StaticsFragment fragment = new StaticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_statics, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //     setMenuVisibility(true);
        mChartPath = getArguments().getString(ARG_CHART_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statics_fragment, container, false);
        charts = new ArrayList<>();
        ll = (LinearLayout) view.findViewById(R.id.statics_test_ll);
        setting = new StaticsCommonSetting();
        thisActivity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new StaticsAsyncTask("http://www.cconma.com" + mChartPath, "GET", "").execute();
    }

    public void parsingcCharts() {
        try {
            if (result == null) {
                Toast.makeText(thisActivity.getApplicationContext(), "받아온 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jArray = result.getJSONArray(StaticsVariables.chart);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    String category = obj.getString(StaticsVariables.category).toLowerCase();
                    generateChart(obj, category);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gone() {
        ll.removeAllViews();
        charts.clear();
    }

    public void refresh() {
        for (int i = 0; i < charts.size(); i++) {
            Chart v = (Chart)charts.get(i);
            Bitmap b = v.getChartBitmap();
            b.recycle();
        }
    }

    public void generateChart(JSONObject json, String category) {
        View chart;
        if (category.equals(StaticsVariables.line)) {
            chart = new LineChart(thisActivity);
            StaticsLineManager manager = new StaticsLineManager(thisActivity);
            ((LineChart) chart).setData(manager.parsingLine(json, (LineChart) chart));
            setting.commonSetting((LineChart) chart);

        } else if (category.equals(StaticsVariables.bar)) {
            chart = new BarChart(thisActivity);
            StaticsBarManager manager = new StaticsBarManager(thisActivity);
            ((BarChart) chart).setData(manager.parsingBar(json, (BarChart) chart));
            setting.commonSetting((BarChart) chart);

        } else if (category.equals(StaticsVariables.combined)) {
            chart = new CombinedChart(thisActivity);
            StaticsCombinedManager manager = new StaticsCombinedManager(thisActivity);
            ((CombinedChart) chart).setData(manager.parsingCombined(json, (CombinedChart) chart));
            setting.commonSetting((CombinedChart) chart);

        } else if (category.equals(StaticsVariables.pie)) {
            chart = new PieChart(thisActivity);
            StaticsPieManager manager = new StaticsPieManager(thisActivity);
            ((PieChart) chart).setData(manager.parsingPie(json, (PieChart) chart));
            setting.commonSetting((PieChart) chart);

        } else {
            return;
        }

        chart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Integer.valueOf(json.optString(StaticsVariables.height, "200")), getResources().getDisplayMetrics())));
        generateZoomBtn(json.optString(StaticsVariables.description, "차트"));

        ll.addView(chart);
        charts.add(chart);
    }

    public void generateZoomBtn(String desc) {
        int dpInPx = 0;
        if (charts.size() == 0) {
            dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()); // 10dp
        } else {
            dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()); // 10dp
        }
        RelativeLayout rl = new RelativeLayout(thisActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = dpInPx;
        rl.setLayoutParams(layoutParams);

        TextView title = new TextView(thisActivity);
        title.setText(desc);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        title.setPadding(dpInPx, dpInPx, dpInPx, dpInPx);
        title.setBackgroundColor(getResources().getColor(R.color.transparent));
        RelativeLayout.LayoutParams titleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        titleParam.addRule(CENTER_VERTICAL);
        title.setLayoutParams(titleParam);
        rl.addView(title);

        ll.addView(rl);
    }

    public class StaticsAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
        private String url;
        private String method;
        private String requestBody;
        private String sResult;

        public StaticsAsyncTask(String url, String method, String requestBody) {
            this.url = url;
            this.method = method;
            this.requestBody = requestBody;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(thisActivity);
            pd.setMessage("차트를 그리고 있습니다.");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            HttpConnection connection = new HttpConnection(url, method, requestBody);
            sResult = connection.init();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(sResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            result = jsonObject;
            parsingcCharts();
            pd.dismiss();
        }
    }
}
