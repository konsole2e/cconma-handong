package handong.cconma.cconmaadmin.mainpage;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.etc.MyWebView;
import handong.cconma.cconmaadmin.etc.SettingActivity;
import handong.cconma.cconmaadmin.mainpage.RecyclerViewAdapter;
import handong.cconma.cconmaadmin.statics.StaticsMain;

/**
 * Created by Young Bin Kim on 2015-07-13.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private String TITLES[] = {"게시판","통계","1:1문의","회원정보 조회", "마을지기 홈페이지"};
    private int ICONS[] = {R.drawable.ic_board_selector, R.drawable.ic_chart_selector, R.drawable.ic_question_selector, R.drawable.ic_search_grey600_48dp, R.drawable.ic_home_selector };
    private int returned = 0;
    private int status = 0;

    private String TITLESUSER[] = {"설정", "로그아웃"};
    private int ICONSUSER[] = {R.drawable.ic_setting_selector, R.drawable.ic_logout_selector};

    private String user_name;
    private IntegratedSharedPreferences pref;

    private String TAG = "debugging";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        pref = new IntegratedSharedPreferences(BaseActivity.this);
    }

    public void setUser(String user_name){
        pref.put("user_name", user_name);
    }

    public void setDrawer(){
        user_name = pref.getValue("user_name", "???");

        Log.d(TAG, user_name);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, user_name, 0);

        recyclerView.setAdapter(recyclerAdapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            //Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        final GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            /*@Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "singleTapconfirmed");
                return true;
            }*/
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });

        final View snackbar = findViewById(R.id.snackbarPosition);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(getApplicationContext(), MyWebView.class);
                    switch (recyclerView.getChildPosition(child)) {
                        case 0:
                            if (status == 0) {
                                recyclerAdapter = new RecyclerViewAdapter(TITLESUSER, ICONSUSER, user_name, 1);
                                recyclerView.setAdapter(recyclerAdapter);
                                status = 1;
                            } else if (status == 1) {
                                recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, user_name, 0);
                                recyclerView.setAdapter(recyclerAdapter);
                                status = 0;
                            }
                            break;
                        case 1:
                            if(status == 0) {
                                //go to board
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }else{
                                //settings
                                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                            }
                            mDrawerLayout.closeDrawers();
                            break;
                        case 2:
                            if(status == 0) {
                                //statistics
                                startActivity(new Intent(getApplicationContext(), StaticsMain.class));
                            }else{
                                //logout
                            }
                            mDrawerLayout.closeDrawers();
                            break;
                        case 3:
                            mDrawerLayout.closeDrawers();
                            intent.putExtra("URL", "http://www.cconma.com/CconmaAdmin/member.fmv?cmd=list");
                            startActivity(intent);
                            break;
                        case 4:
                            mDrawerLayout.closeDrawers();
                            intent.putExtra("URL", "http://www.cconma.com/admin/help_board/help_board_list.pmv");
                            startActivity(intent);
                            break;
                        case 5:
                            mDrawerLayout.closeDrawers();
                            intent.putExtra("URL", "http://www.cconma.com/CconmaAdmin/login.fmv?cmd=loginForm&path=%2FCconmaAdmin%2Fmain.fmv");
                            startActivity(intent);
                            break;
                        default:
                            mDrawerLayout.closeDrawers();
                            Snackbar.make(snackbar, recyclerView.getChildPosition(child) + " pressed", Snackbar.LENGTH_SHORT).show();
                    }
                    child.setPressed(false);

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}

