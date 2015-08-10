package handong.cconma.cconmaadmin.textStatics;

import android.app.Activity;
import android.app.ProgressDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.http.HttpConnection;

import static android.widget.RelativeLayout.CENTER_VERTICAL;

public class TextStaticsFragment extends Fragment {
    public static final String ARG_CHART_PATH = "ARG_PAGE_PATH";
    private String mChartPath;
    private View view;
    private LinearLayout ll;
    private JSONObject result = null;
    private ArrayList<RelativeLayout> textCharts;
    private Activity thisActivity;
    private ProgressDialog pd;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.statics_reload && !pd.isShowing()) {
            gone();
             new TextStaticsAsyncTask("http://www.cconma.com" + mChartPath, "GET", "").execute();
        }
        return super.onOptionsItemSelected(item);
    }

    public static TextStaticsFragment newInstance(String chartPath) {
        Bundle args = new Bundle();
        args.putString(ARG_CHART_PATH, chartPath);
        TextStaticsFragment fragment = new TextStaticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_statics, menu);
        menu.removeItem(R.id.statics_zoom);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mChartPath = getArguments().getString(ARG_CHART_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.text_statics_fragment, container, false);
        textCharts = new ArrayList<>();
        ll = (LinearLayout) view.findViewById(R.id.text_statics_ll);
        thisActivity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      new TextStaticsAsyncTask("http://www.cconma.com" + mChartPath, "GET", "").execute();
    }


    public void parsingTextCharts() {
        try {
            if (result == null) {
                Toast.makeText(thisActivity.getApplicationContext(), "받아온 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jArray = result.getJSONArray(TextStaticsVariables.stat_list);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    generateTextChart(obj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gone() {
        ll.removeAllViews();
        textCharts.clear();
    }

    public void generateTextChart(JSONObject json) {
        RelativeLayout rl = (RelativeLayout)thisActivity.getLayoutInflater().inflate(R.layout.text_statics_chart, null);
        LinearLayout left = (LinearLayout) rl.findViewById(R.id.stat_left_ll);
        LinearLayout right = (LinearLayout) rl.findViewById(R.id.stat_right_ll);

        TextStaticsManager manager = new TextStaticsManager(thisActivity);
        manager.setData(json, left, right);

        generateTitle(json.optString(TextStaticsVariables.stat_title, "텍스트 차트"));

        ll.addView(rl);
        textCharts.add(rl);
    }

    public void generateTitle(String chartTitle) {
        int dpInPx = 0;
        if (textCharts.size() == 0) {
            dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()); // 10dp
        } else {
            dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()); // 10dp
        }
        RelativeLayout rl = new RelativeLayout(thisActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = dpInPx;
        rl.setLayoutParams(layoutParams);

        TextView title = new TextView(thisActivity);
        title.setText(chartTitle);
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

    public class TextStaticsAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
        private String url;
        private String method;
        private String requestBody;
        private String sResult;

        public TextStaticsAsyncTask(String url, String method, String requestBody) {
            this.url = url;
            this.method = method;
            this.requestBody = requestBody;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(thisActivity);
            pd.setMessage("표를 그리고 있습니다.");
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
            parsingTextCharts();
            pd.dismiss();
        }
    }
}
