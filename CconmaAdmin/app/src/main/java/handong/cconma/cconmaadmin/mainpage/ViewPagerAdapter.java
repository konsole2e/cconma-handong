package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import handong.cconma.cconmaadmin.data.BasicData;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private HashMap board_list;
    private ArrayList<String> tabBoardNo;
    private ArrayList<String> tabTitles;
    private HashMap<Integer, Fragment> mPageReferenceMap;
    private Fragment fragment;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        board_list = BasicData.getInstance().getBoardList();

        mPageReferenceMap = new HashMap<>();
        tabBoardNo = new ArrayList<>();
        tabTitles = new ArrayList<>();

        for( int i = 0; i < board_list.size() / 2; i++ ){
            tabBoardNo.add(board_list.get("board_no" + i).toString());
            Log.d("Data", "data" + i + ": " + tabBoardNo.get(i));
            tabTitles.add(board_list.get("board_title" + i).toString());
            Log.d("Data", "data" + i + ": " + tabTitles.get(i));
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    @Override
    public Fragment getItem(int position) {
        fragment = BoardFragment.newInstance(tabBoardNo.get(position), tabTitles.get(position));
        mPageReferenceMap.put(position, fragment);
        return fragment;
        //return BoardFragment.newInstance(position + 1, tablTitles_no[position]);
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

}
