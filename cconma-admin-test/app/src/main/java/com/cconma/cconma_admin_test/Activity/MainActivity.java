package com.cconma.cconma_admin_test.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cconma.cconma_admin_test.Adapter.RecyclerViewAdapter;
import com.cconma.cconma_admin_test.data.IntegratedSharedPreferences;
import com.cconma.cconma_admin_test.view.SwipeToRefresh;
import com.cconma.cconma_admin_test.Adapter.ViewPagerAdapter;
import com.cconma.cconma_test_actionbar.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;



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
    private IntegratedSharedPreferences pref;

    private String TITLES[] = {"게시판","통계","1:1문의","마을지기 홈페이지"};
    private int ICONS[] = {R.drawable.ic_board_selector, R.drawable.ic_chart_selector, R.drawable.ic_question_selector, R.drawable.ic_home_selector };
    private int returned = 0;
    private int status = 0;

    private String TITLESUSER[] = {"설정", "로그아웃"};
    private int ICONSUSER[] = {R.drawable.ic_setting_selector, R.drawable.ic_logout_selector};

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "1";
    private static final String TAG = "debugging";

    GoogleCloudMessaging gcm;
    Context context;
    String regid;

    @Override
    protected void onResume(){
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        pref = new IntegratedSharedPreferences(context);

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {

            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        addSwipeToRefresh();

        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //setTabLayout();

        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, "IT개발팀", 0);

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "singleTapconfirmed");
                return true;
            }
        });

        final View snackbar = findViewById(R.id.snackbarPosition);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                Log.d(TAG, String.valueOf(mGestureDetector.onTouchEvent(motionEvent)));
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    if (recyclerView.getChildPosition(child) == 0) {
                        if (status == 0) {
                            recyclerAdapter = new RecyclerViewAdapter(TITLESUSER, ICONSUSER, "IT개발팀", 1);
                            recyclerView.setAdapter(recyclerAdapter);
                            status = 1;
                        } else if (status == 1) {
                            recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, "IT개발팀", 0);
                            recyclerView.setAdapter(recyclerAdapter);
                            status = 0;
                        }
                    } else if (recyclerView.getChildPosition(child) == 4) {
                        mDrawerLayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, LoginWebView.class);
                        startActivity(intent);
                    } else {
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

        recyclerView.setAdapter(recyclerAdapter);

        // View Page Adapter
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);
        viewPager.setAdapter(viewPagerAdapter);

        // ViewPager setting
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

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

        /*
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show();
                Snackbar.make(snackbar, menuItem.getTitle() + String.valueOf(menuItem.getItemId()) + " pressed", Snackbar.LENGTH_SHORT).show();

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                if(menuItem.getItemId() == R.id.homepage) {
                    //Toast.makeText(getApplicationContext(), "홈페이지 이동", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginWebView.class);
                    startActivity(intent);
                }
                return true;
            }
        });*/
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
        if(id == R.id.my_favorite){
            Toast.makeText(getApplicationContext(), "my favorite", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.notification){
            Toast.makeText(getApplicationContext(), "notification", Toast.LENGTH_SHORT).show();
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
                    if(returned == 0) {
                        Toast.makeText(getApplicationContext(), "종료하려면 한번 더 탭하세요", Toast.LENGTH_SHORT).show();
                        returned = 1;
                    }
                    else{
                        finish();
                        returned = 0;
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void addSwipeToRefresh() {
        //set SwipeToRefresh on the activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SwipeToRefresh swipe = new SwipeToRefresh();
        transaction.add(R.id.container, swipe);
        transaction.commit();
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

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
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

    private void scaleImage(ImageView view, int intoscale ) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        if (drawing == null) {
            return; // Checking for null & return, as suggested in comments
        }
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(intoscale);

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;


        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(getResources(), scaledBitmap);

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}

