package handong.cconma.cconmaadmin.textStatics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.statics.StaticsFragment;

public class TextStaticsViewPagerAdapter extends FragmentPagerAdapter {
    private HashMap stat_list;
    private ArrayList<String> statPaths;
    private ArrayList<String> statNames;

    public TextStaticsViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        stat_list = BasicData.getInstance().getTextChartList();

        statNames = new ArrayList<>();
        statPaths = new ArrayList<>();

        for (int i = 0; i < stat_list.size() / 2; i++) {
            statPaths.add(stat_list.get("stat_path" + i).toString());
            statNames.add(stat_list.get("stat_name" + i).toString());
        }
    }

    @Override
    public Fragment getItem(int position) {
        return TextStaticsFragment.newInstance(statPaths.get(position));
    }

    @Override
    public int getCount() {
        return statPaths.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return statNames.get(position);
    }
}
