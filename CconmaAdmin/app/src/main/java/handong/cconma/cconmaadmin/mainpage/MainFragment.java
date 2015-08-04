package handong.cconma.cconmaadmin.mainpage;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardWriteActivity;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.statics.StaticsLike;
import handong.cconma.cconmaadmin.statics.StaticsMember;
import handong.cconma.cconmaadmin.statics.StaticsMemberRecent;
import handong.cconma.cconmaadmin.statics.StaticsOrderH;
import handong.cconma.cconmaadmin.statics.StaticsOrderRecent;
import handong.cconma.cconmaadmin.statics.StaticsTest;
import handong.cconma.cconmaadmin.statics.StaticsTrade;
import handong.cconma.cconmaadmin.statics.StaticsViewPagerAdapter;

/**
 * Created by Young Bin Kim on 2015-07-14.
 */
public class MainFragment extends Fragment {
    public static final String POSITION = "0";
    private CharSequence TITLES[] = {"게시판","통계","1:1문의","회원정보 조회", "주문조회", "마을지기 홈페이지"};
    private ViewPager vp;
    private WebView webview;
    private CircularProgressBar circularProgressBar;

    public MainFragment(){
    }

    @Override
    public void onResume(){
        super.onResume();
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

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
            viewPager.setOffscreenPageLimit(2);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext());
            viewPager.setAdapter(viewPagerAdapter);

            TabLayout tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager);

            FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BoardWriteActivity.class);
                    startActivity(intent);
                }
            });
        }
        else if(position == 2){
            rootView = inflater.inflate(R.layout.statics_main, container, false);

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.statics_vp);
            StaticsViewPagerAdapter svp = new StaticsViewPagerAdapter(getChildFragmentManager(), getActivity());
            viewPager.setOffscreenPageLimit(0);
            viewPager.setAdapter(svp);
            vp = viewPager;

            TabLayout tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager);

            FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
        }
        else {
            rootView = inflater.inflate(R.layout.webview, container, false);
            circularProgressBar = (CircularProgressBar)rootView.findViewById(R.id.progressbar_circular);

            switch(position){
                case 3:
                    openWebView(rootView, "http://www.cconma.com/admin/help_board/help_board_list.pmv");
                    break;
                case 4:
                    openWebView(rootView, "http://www.cconma.com/CconmaAdmin/member.fmv?cmd=list");
                    break;
                case 5:
                    openWebView(rootView, "http://www.cconma.com/CconmaAdmin/orderList.fmv?cmd=list");
                    break;
                case 6:
                    openWebView(rootView, "http://www.cconma.com/CconmaAdmin/main.fmv");
                    break;
            }
        }
        getActivity().setTitle(TITLES[position - 1]);

        return rootView;
    }

    public boolean canGoBack() {
        return webview.canGoBack();
    }

    public void openWebView(View view, String url){
        webview = (WebView) view.findViewById(R.id.webView);

        webview.getSettings().setJavaScriptEnabled(true); //Enable when javascript is needed
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setUserAgentString("User-agent");
        webview.canGoBackOrForward(5);
        webview.loadUrl(url);
        webview.setWebViewClient(new WebClient());
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
            view.loadUrl(url);
            return true;
        }
        public void onPageFinished(WebView view, String url){
            ((CircularProgressDrawable)circularProgressBar.getIndeterminateDrawable()).progressiveStop();
            Cookies.getInstance(getActivity().getApplicationContext()).updateCookies(url);
        }
    }

    public ViewPager getStaticsViewPager(){
        return vp;
    }
}