package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import handong.cconma.cconmaadmin.data.BasicData;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class StaticsViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private HashMap chart_list;
    private ArrayList<String> chartPaths;
    private ArrayList<String> chartNames;

    public StaticsViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        BasicData boardData = BasicData.getInstance();
        chart_list = BasicData.getInstance().getChartList();

        chartNames = new ArrayList<>();
        chartPaths = new ArrayList<>();

        for (int i = 0; i < chart_list.size() / 2; i++) {
            chartPaths.add(chart_list.get("chart_path" + i).toString());
            Log.d("Data", "data" + i + ": " + chartPaths.get(i));
            chartNames.add(chart_list.get("chart_name" + i).toString());
            Log.d("Data", "data" + i + ": " + chartNames.get(i));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return StaticsFragment.newInstance(chartPaths.get(position));
    }

    @Override
    public int getCount() {
        return chartPaths.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return chartNames.get(position);
    }
}
