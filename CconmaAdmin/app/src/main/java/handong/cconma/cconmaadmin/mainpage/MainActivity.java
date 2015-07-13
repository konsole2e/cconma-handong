package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.drive.internal.SetDrivePreferencesRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import handong.cconma.cconmaadmin.board.BoardMarkedActivity;
import handong.cconma.cconmaadmin.board.BoardWriteActivity;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.push.PushView;
import handong.cconma.cconmaadmin.statics.StaticsMain;
import handong.cconma.cconmaadmin.etc.MyWebView;
import handong.cconma.cconmaadmin.etc.SwipeToRefresh;
import handong.cconma.cconmaadmin.gcm.RegistrationIntentService;

/**
 * Created by YoungBinKim on 2015-07-06.
 */

public class MainActivity extends BaseActivity{
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
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout mSwipeRefresh;

    private int returned = 0;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "1";
    private static final String TAG = "debugging";

    GoogleCloudMessaging gcm;
    Context context;
    String regid;

    @Override
    protected void onResume() {
        super.onResume();
        //checkPlayServices();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUser("IT개발");
        setDrawer();
        context = getApplicationContext();

        getInstanceIdToken(); //get regId for GCM

        // View Page Adapter
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);
        viewPager.setAdapter(viewPagerAdapter);

        // ViewPager setting
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardWriteActivity.class);
                startActivity(intent);
            }
        });
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
                    if (returned == 0) {
                        Toast.makeText(getApplicationContext(), "종료하려면 한번 더 탭하세요", Toast.LENGTH_SHORT).show();
                        returned = 1;
                    } else {
                        finish();
                        returned = 0;
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
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

