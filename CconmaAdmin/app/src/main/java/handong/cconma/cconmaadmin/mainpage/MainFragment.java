package handong.cconma.cconmaadmin.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardWriteActivity;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.statics.StaticsViewPagerAdapter;

/**
 * Created by Young Bin Kim on 2015-07-14.
 */
public class MainFragment extends Fragment implements MainActivity.onKeyBackPressedListener {
    public static final String POSITION = "0";
    private CharSequence TITLES[] = {"게시판","통계","1:1문의","회원정보 조회", "주문조회", "마을지기 홈페이지"};

    public static WebView webview;
    private SmoothProgressBar progressBar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

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
        Log.d("fragment", "RESUME");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("fragment", "onStart");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("fragment", "onStop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("fragment", "onCreateView");
        View rootView;

        Log.d("fragment", "savedInstanceState: " + String.valueOf(savedInstanceState));
        int position = getArguments().getInt(POSITION);

        if(position == 1){
            rootView = inflater.inflate(R.layout.board_viewpager, container, false);

            viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
            //viewPager.setOffscreenPageLimit(2);
            final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext());
            viewPager.setAdapter(viewPagerAdapter);

            tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager);

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


                }
            });

        }
        else if(position == 2){
            rootView = inflater.inflate(R.layout.statics_main, container, false);

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.statics_vp);
            StaticsViewPagerAdapter svp = new StaticsViewPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext());
            viewPager.setOffscreenPageLimit(0);
            viewPager.setAdapter(svp);

            tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager);

            fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            FloatingActionButton fab_up = (FloatingActionButton)getActivity().findViewById(R.id.fab_up);
            fab_up.setVisibility(View.GONE);
        }
        else {
            rootView = inflater.inflate(R.layout.navi_webview, container, false);
            progressBar = (SmoothProgressBar) rootView.findViewById(R.id.progressbar);
            String location = getResources().getString(R.string.www);

            switch(position){
                case 3:
                    hideOtherViews();
                    openWebView(rootView, location + "/admin/help_board/help_board_list.pmv");
                    break;
                case 4:
                    hideOtherViews();
                    openWebView(rootView, location + "/CconmaAdmin/member.fmv?cmd=list");
                    break;
                case 5:
                    hideOtherViews();
                    openWebView(rootView, location + "/CconmaAdmin/orderList.fmv?cmd=list");
                    break;
                case 6:
                    hideOtherViews();
                    openWebView(rootView, location + "/CconmaAdmin/main.fmv");
                    break;
            }
        }
        getActivity().setTitle(TITLES[position - 1]);

        return rootView;
    }

//////////////////back key control in fragment
    @Override
    public void onBack() {
        if (webview.canGoBack()) {
            webview.goBack();
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

    public void openWebView(View view, String url){
        webview = (WebView) view.findViewById(R.id.navi_webView);

        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        webview.getSettings().setBuiltInZoomControls(true);
        String userAgent = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(userAgent + ";com.admincconma.app");
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.canGoBackOrForward(3);
        webview.loadUrl(url);
        webview.setWebViewClient(new WebClient());
    }

    public void hideOtherViews(){
        tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.GONE);

        fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }
 /*   @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_hourly_btn :
                startActivity(new Intent(getActivity(), StaticsOrderH.class));
                break;
            case R.id.order_recent_btn:
                startActivity(new Intent(getActivity(), StaticsOrderRecent.class));
                break;
            case R.id.trade_btn :
                startActivity(new Intent(getActivity(), StaticsTrade.class));
                break;
            case R.id.like_btn:
                startActivity(new Intent(getActivity(), StaticsLike.class));
                break;
            case R.id.member_btn :
                startActivity(new Intent(getActivity(), StaticsMember.class));
                break;
            case R.id.member_recent_btn:
                startActivity(new Intent(getActivity(), StaticsMemberRecent.class));
                break;
            case R.id.test_btn :
                startActivity(new Intent(getActivity(), StaticsTest.class));
                break;
        }
    }*/

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            progressBar.progressiveStart();
            view.loadUrl(url);
            return true;
        }
        public void onPageFinished(WebView view, String url){
            progressBar.progressiveStop();
            if( getActivity().getApplicationContext() != null )
                Cookies.getInstance(getActivity().getApplicationContext()).updateCookies(url);
        }
    }
}