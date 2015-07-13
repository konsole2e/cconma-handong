package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    final int pageCount = 8;
    private String tabTitles[] = new String[] { "꽃마보드", "IT개발", "CM", "CS", "마케팅", "행밥", "디자인", "물류" };

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
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
