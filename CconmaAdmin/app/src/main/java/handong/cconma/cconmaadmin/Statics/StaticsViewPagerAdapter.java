package handong.cconma.cconmaadmin.statics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class StaticsViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    final int pageCount = 0;
    private String tabTitles[] = {};
    private int tableTitles_no[] = {};

    public StaticsViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return StaticsFragement.newInstance(position + 1, tableTitles_no[position]);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
