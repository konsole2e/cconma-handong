package handong.cconma.cconmaadmin.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.HashMap;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardWriteActivity;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.statics.StaticsViewPagerAdapter;
import handong.cconma.cconmaadmin.textStatics.TextStaticsViewPagerAdapter;
import handong.cconma.cconmaadmin.webpage.WebPagesViewPagerAdapter;

/**
 * Created by Young Bin Kim on 2015-07-14.
 */
public class MainFragment extends Fragment implements MainActivity.onKeyBackPressedListener {
    public static final String POSITION = "0";

    public static WebView webview;
    private ViewPager viewPager;
    private ViewPager etc_viewPager;
    private TabLayout tabLayout;
    private TabLayout etc_tabLayout;
    private FloatingActionButton fab;
    public WebPagesViewPagerAdapter viewPagerAdapter;
    public WebView pageWebView;
    private String url;
    private int fragment_pos = 0;
    private int position = 0;
    private boolean reSelect = true;

    int pos;
    public MainFragment(){
    }

    @Override
    public void onResume() {
        super.onResume();
        if( AdminApplication.getInstance().getRefresh() ){
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),
                    getActivity().getApplicationContext());
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(AdminApplication.getInstance().getTabPosition());
            AdminApplication.getInstance().setRefresh(false);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        position = getArguments().getInt(POSITION);

        if(position == -1){
            rootView = inflater.inflate(R.layout.board_viewpager, container, false);

            viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
            final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager()
                    , getActivity().getApplicationContext());
            viewPager.setAdapter(viewPagerAdapter);

            tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            resetTabSelectListener(tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(viewPager.getCurrentItem() != tab.getPosition()){
                        reSelect = false;
                    }
                    super.onTabSelected(tab);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    if(!reSelect){
                        reSelect = true;
                        return;
                    }
                    fragment_pos = tab.getPosition();
                    Fragment fragment = viewPagerAdapter.getFragment(fragment_pos);
                    if( fragment != null ) {
                        BoardFragment bf = (BoardFragment) fragment;
                        bf.refresh(getActivity().getApplicationContext());
                    }
                }
            });

            fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int board = viewPager.getCurrentItem();
                    AdminApplication.getInstance().setTabPosition(board);

                    if (board == 0) {
                        board = 12;
                    } else {
                        HashMap board_list = BasicData.getInstance().getBoardList();
                        board = Integer.parseInt(board_list.get("board_no" + board).toString());
                    }

                    Intent intent = new Intent(getActivity(), BoardWriteActivity.class);
                    intent.putExtra("board", board);
                    Log.d("debugging", String.valueOf(board));
                    startActivity(intent);
                }
            });

            //리스트 최상단으로 가는 버튼
            FloatingActionButton fab_up = (FloatingActionButton)getActivity().findViewById(R.id.fab_up);
            fab_up.setVisibility(View.VISIBLE);
            fab_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = viewPagerAdapter.getFragment(fragment_pos);
                    if( fragment != null ) {
                        RecyclerView rv = (RecyclerView) fragment.getView().findViewById(R.id.recycler_view);
                        rv.smoothScrollToPosition(0);
                    }
                }
            });

        }
        else if(position == -2){
            rootView = inflater.inflate(R.layout.statics_main, container, false);

            viewPager = (ViewPager) rootView.findViewById(R.id.statics_vp);
            StaticsViewPagerAdapter svpa = new StaticsViewPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext());
            viewPager.setOffscreenPageLimit(0);
            viewPager.setAdapter(svpa);

            tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            resetTabSelectListener(tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            FloatingActionButton fab_up = (FloatingActionButton)getActivity().findViewById(R.id.fab_up);
            fab_up.setVisibility(View.GONE);
        }
        else if(position == -3) {
            rootView = inflater.inflate(R.layout.text_statics_main, container, false);

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.text_statics_vp);
            TextStaticsViewPagerAdapter tsvpa = new TextStaticsViewPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext());
            viewPager.setOffscreenPageLimit(0);
            viewPager.setAdapter(tsvpa);

            TabLayout tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager);

            FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            FloatingActionButton fab_up = (FloatingActionButton)getActivity().findViewById(R.id.fab_up);
            fab_up.setVisibility(View.GONE);
        }else{
            rootView = inflater.inflate(R.layout.etc_viewpager, container, false);

            etc_viewPager = (ViewPager) rootView.findViewById(R.id.etc_viewpager);
            viewPagerAdapter = new WebPagesViewPagerAdapter(
                    getChildFragmentManager(), getActivity().getApplicationContext(), position - 1);
            etc_viewPager.setAdapter(viewPagerAdapter);

            etc_tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            etc_tabLayout.setVisibility(View.VISIBLE);
            etc_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            etc_tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            etc_tabLayout.setupWithViewPager(etc_viewPager);
            //tabLayout.setTabsFromPagerAdapter(viewPagerAdapter);
            //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            etc_tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(etc_viewPager) {
                ViewPager vp = etc_viewPager;
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    pos = tab.getPosition();
                    vp.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    fragment_pos = tab.getPosition();
                    Fragment fragment = viewPagerAdapter.getFragment(fragment_pos);
                    //Fragment fragment = viewPagerAdapter.getPrimaryFragment();
                    if (fragment != null && !viewPagerAdapter.first) {
                        pageWebView = (WebView) fragment.getView().findViewById(R.id.navi_webView);
                        CircularProgressBar cpb = (CircularProgressBar) fragment.getView().findViewById(R.id.progressbar_circular);

                        if (pageWebView != null) {
                            cpb.setVisibility(View.VISIBLE);
                            Log.d("debugging", "tab position: " + tab.getPosition() + " " + tab.getText());
                            url = WebPagesViewPagerAdapter.getUrl(tab.getPosition());
                            String location = getResources().getString(R.string.www);
                            pageWebView.loadUrl(location + url);
                        }
                    }
                }
            });

            fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            FloatingActionButton fab_up = (FloatingActionButton)getActivity().findViewById(R.id.fab_up);
            fab_up.setVisibility(View.GONE);
        }
        getActivity().setTitle("마을지기");

        return rootView;
    }

//////////////////back key control in fragment
    @Override
    public void onBack() {
        WebView webView = (WebView) viewPagerAdapter.getFragment(pos).getView().
                findViewById(R.id.navi_webView);
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            MainActivity activity = (MainActivity) getActivity();
            activity.setOnKeyBackPressedListener(null);
            activity.onBackPressed();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setOnKeyBackPressedListener(this);
    }
//////////////////
    public void resetTabSelectListener(TabLayout tabLayout){
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}