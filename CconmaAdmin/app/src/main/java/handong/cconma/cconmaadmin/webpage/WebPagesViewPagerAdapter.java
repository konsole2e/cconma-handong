package handong.cconma.cconmaadmin.webpage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import handong.cconma.cconmaadmin.data.BasicData;

/**
 * Created by Young Bin Kim on 2015-08-10.
 */
public class WebPagesViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private static  ArrayList<String> submenu_urls;
    private ArrayList<String> tabTitles;
    private WebPageFragment fragment;
    private HashMap<Integer, WebPageFragment> mPageReferenceMap;
    public boolean first = false;

    public WebPagesViewPagerAdapter(FragmentManager fm, Context context, int position) {
        super(fm);

        mPageReferenceMap = new HashMap<>();
        ArrayList submenu_name_list;
        HashMap sub_menus;

        mContext = context;
        submenu_name_list = BasicData.getInstance().getSubmenuNameList();

        submenu_urls = new ArrayList<>();
        tabTitles = new ArrayList<>();

        sub_menus = (HashMap) submenu_name_list.get(position);
        for (int i = 0; i < sub_menus.size() / 2; i++) {
            tabTitles.add(sub_menus.get("submenu_name" + position + "-" + i).toString());
            submenu_urls.add(sub_menus.get("submenu_url" + position + "-" + i).toString());
        }

        first = true;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = WebPageFragment.newInstance(submenu_urls.get(position));
        mPageReferenceMap.put(position, fragment);
        first = false;
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    public static String getUrl(int position){
        return submenu_urls.get(position);
    }

    public WebPageFragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }
}
