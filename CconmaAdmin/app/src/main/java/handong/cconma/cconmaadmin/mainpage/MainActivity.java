package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.drive.internal.SetDrivePreferencesRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.Handler;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardMarkedActivity;
import handong.cconma.cconmaadmin.board.BoardWriteActivity;
import handong.cconma.cconmaadmin.push.PushView;
import handong.cconma.cconmaadmin.statics.StaticsMain;
import handong.cconma.cconmaadmin.etc.MyWebView;
import handong.cconma.cconmaadmin.etc.SwipeToRefresh;
import handong.cconma.cconmaadmin.gcm.RegistrationIntentService;

/**
 * Created by YoungBinKim on 2015-07-06.
 */

public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout drawerHeader;
    private handong.cconma.cconmaadmin.data.IntegratedSharedPreferences pref;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout mSwipeRefresh;
    private FragmentManager fragmentManager;

    private String TITLESUSER[] = {"설정", "로그아웃"};
    private int ICONSUSER[] = {R.drawable.ic_setting_selector,
            R.drawable.ic_logout_selector};

    private CharSequence TITLES[] = {"게시판","통계","1:1문의","회원정보 조회", "주문조회", "마을지기 홈페이지"};
    private int ICONS[] = {R.drawable.ic_board_selector, R.drawable.ic_chart_selector, R.drawable.ic_question_selector, R.drawable.ic_search_grey600_48dp, R.drawable.ic_shopping_cart_grey600_48dp, R.drawable.ic_home_selector };
    private int status = 0;
    private String user_name = "개발";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "1";
    private static final String TAG = "debugging";
    private int position;

    Context context;

    /*
    temp
     */
    private CharSequence mTitle;

    @Override
    protected void onResume() {
        super.onResume();
        //checkPlayServices();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();
        context = getApplicationContext();

        getInstanceIdToken(); //get regId for GCM

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, user_name, 0);

        recyclerView.setAdapter(recyclerAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        final View snackbar = findViewById(R.id.snackbarPosition);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    position = recyclerView.getChildPosition(child);
                    if (position == 0) {
                        if (status == 0) {
                            recyclerAdapter = new RecyclerViewAdapter(TITLESUSER, ICONSUSER, user_name, 1);
                            recyclerView.setAdapter(recyclerAdapter);
                            status = 1;
                        } else if (status == 1) {
                            recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, user_name, 0);
                            recyclerView.setAdapter(recyclerAdapter);
                            status = 0;
                        }
                    } else {
                        mDrawerLayout.closeDrawers();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setTitle(TITLES[position - 1]);
                                selectItem(position);
                            }
                        }, 350);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            //Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
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

        if (savedInstanceState == null) {
            selectItem(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.my_favorite) {
            Intent intent = new Intent(this, BoardMarkedActivity.class);
            startActivity(intent);
        } else if (id == R.id.notification) {
            Intent intent = new Intent(this, PushView.class);
            startActivity(intent);
    }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MENU:
                    Toast.makeText(getApplicationContext(), "menu button clicked", Toast.LENGTH_SHORT).show();
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    Fragment webView = getSupportFragmentManager().findFragmentById(R.id.webView);

                    if (webView instanceof MainFragment) {
                        boolean goback = ((MainFragment)webView).canGoBack();
                        if (!goback)
                            super.onBackPressed();
                    }
                    else if(position == 1){
                        finish();
                    }
                    else {
                        position = 1;
                        selectItem(1);
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {
        if(position == 1){
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content_frame);
            if(fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(fragment).commit();
            }

            // View Page Adapter
            viewPager = (ViewPager)findViewById(R.id.viewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                    this);
            viewPager.setAdapter(viewPagerAdapter);

            // ViewPager setting
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setVisibility(tabLayout.VISIBLE);

            floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
            floatingActionButton.setVisibility(floatingActionButton.VISIBLE);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BoardWriteActivity.class);
                    startActivity(intent);
                }
            });
            getSupportActionBar().setTitle(TITLES[position - 1]);
        }
        else {
            tabLayout.setVisibility(findViewById(R.id.tabLayout).GONE);
            floatingActionButton.setVisibility(findViewById(R.id.fab).GONE);
            Fragment fragment = new MainFragment();
            Bundle args = new Bundle();
            args.putInt(MainFragment.POSITION, position);
            fragment.setArguments(args);

            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.main_content_frame, fragment, "fragment");
            ft.commit();
        }
    }

    private void setSwipeToRefresh(){
        //set SwipeToRefresh on the activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SwipeToRefresh swipe = new SwipeToRefresh();
        transaction.add(R.id.board_container, swipe);
        transaction.commit();
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            Log.d(TAG, "GCM INTENT");
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}

