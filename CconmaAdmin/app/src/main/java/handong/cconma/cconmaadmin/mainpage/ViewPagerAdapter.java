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
    final int pageCount = 13;
    private String tabTitles[] = new String[] { "꽃마보드", "개발", "CM", "CS", "마케팅", "행밥", "디자인", "물류", "스크랩", "초안", "별도메일", "해외장터", "IT자산/차량" };
    private int tablTitles_no[] = new int[] {12, 17, 39, 46, 30, 45, 37, 18, 32, 33, 26, 48, 27 };

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return BoardFragment.newInstance(position + 1, tablTitles_no[position]);
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
