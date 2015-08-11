package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardMarkedActivity;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.etc.LogoutWebView;
import handong.cconma.cconmaadmin.etc.SettingActivity;
import handong.cconma.cconmaadmin.etc.SwipeToRefresh;

/**
 * Created by YoungBinKim on 2015-07-06.
 */

public class MainActivity extends AppCompatActivity {
    public Menu mMenu;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;
    private FragmentManager fragmentManager;
    private Configuration config;
    private int status = 0;
    private int position;
    private int menu_count;
    private Context context;

    private static final String TAG = "debugging";

    private CharSequence mTitle;
    private onKeyBackPressedListener mOnKeyBackPressedListener = null;

    private MenuItem mPreviousMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(null);
        config = getResources().getConfiguration();
        mTitle = getTitle();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        LayoutInflater inflater = (LayoutInflater) MainActivity.this.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.drawer_header, navigationView, false);
        TextView textview = (TextView)view.findViewById(R.id.name);
        RelativeLayout header = (RelativeLayout)view.findViewById(R.id.drawer_header);
        final ImageView arrow = (ImageView)view.findViewById(R.id.user_arrow);
        textview.setText(BasicData.getInstance().getName());

        header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (status == 1) {
                    Menu menu = navigationView.getMenu();
                    menu.removeGroup(R.id.menu_user);
                    navigationView.inflateMenu(R.menu.menu_default);
                    getDynamicMenu();
                    arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                    status = 0;
                } else {
                    Menu menu = navigationView.getMenu();
                    menu.removeGroup(R.id.menu_default);
                    menu.removeGroup(1);
                    navigationView.inflateMenu(R.menu.menu_user);
                    arrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                    status = 1;
                }
            }
        });

        getDynamicMenu();
        navigationView.addHeaderView(view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
                if( mPreviousMenuItem == menuItem ){
                    return true;
                }
                else{
                    menuItem.setChecked(true);
                    if (mPreviousMenuItem != null) {
                        mPreviousMenuItem.setChecked(false);
                    }
                    mPreviousMenuItem = menuItem;
                    mDrawerLayout.closeDrawers();

                    position = menuItem.getItemId();
                    Log.d(TAG, String.valueOf(position));

                    if (status == 0) {
                        switch (position) {
                            case R.id.board:
                                selectItem(-1);
                                break;
                            case R.id.chart:
                                selectItem(-2);
                                break;
                            case R.id.text_chart:
                                selectItem(-3);
                                break;
                            default:
                                if( position <= menu_count ){
                                    selectItem(position);
                                }
                        }
                    } else {
                        switch (position) {
                            case R.id.user_settings:
                                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                break;
                            case R.id.logout:
                                startActivity(new Intent(MainActivity.this, LogoutWebView.class));
                                finish();
                        }
                    }
                }
                return true;
            }
        });

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

        if (savedInstanceState == null) {
            MenuItem menuItem_board = navigationView.getMenu().findItem(R.id.board);
            mPreviousMenuItem = menuItem_board;
            mPreviousMenuItem.setCheckable(true);
            mPreviousMenuItem.setChecked(true);
            selectItem(-1);
        }
    }

    public void getDynamicMenu(){
        Menu menu = navigationView.getMenu();
        HashMap temp = BasicData.getInstance().getMenuNameList();
        menu_count = temp.size();

        for (int i = 0; i < menu_count; i++) {
            menu.add(1, i, 0, temp.get("menu_name" + i).toString()).setIcon(R.drawable.ic_web_white_36dp);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
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
            //Intent intent = new Intent(this, StartPage.class);
            //startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public interface onKeyBackPressedListener {
        void onBack();
    }

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        if (position == 0 || position == R.id.board || position == R.id.chart || position == R.id.text_chart) {
            mOnKeyBackPressedListener = null;
        } else {
            mOnKeyBackPressedListener = listener;
        }
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
            return;
        }

        if (position == R.id.chart && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {// 가로
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
            return;
        }
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBack();
        } else {
            if (count == 1) {
                finish();
            } else {
                navigationView.getMenu().findItem(position).setChecked(false);
                fragmentManager.popBackStack();
                navigationView.getMenu().getItem(Integer.valueOf(fragmentManager.getFragments().get(count-2).getTag()) - 1).setChecked(true);
            }
        }
    }

    /*@Override
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
    }*/

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

    private void selectItem(final int position) {
            /*if (position == 1) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content_frame);
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fragment).commit();
                }

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
                tabLayout.setVisibility(View.VISIBLE);

                floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, BoardWriteActivity.class);
                        startActivity(intent);
                    }
                });

                getSupportActionBar().setTitle(TITLES[position - 1]);
            } else {*/
        // tabLayout.setVisibility(View.GONE);
        // floatingActionButton.setVisibility(View.GONE);
        Fragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(MainFragment.POSITION, position);
        fragment.setArguments(args);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_content_frame, fragment, String.valueOf(position));
        ft.addToBackStack(null);
        Log.d(TAG, "fragment stack: " + String.valueOf(getFragmentManager().getBackStackEntryCount()));
        ft.commit();
        //}
    }

    private void setSwipeToRefresh() {
        //set SwipeToRefresh on the activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SwipeToRefresh swipe = new SwipeToRefresh();
        transaction.add(R.id.board_container, swipe);
        transaction.commit();
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {

        Context context;
        String items[];

        public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_dropdown_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(15);
            return convertView;
        }

        /**
         * 기본 스피너 View 정의
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(15);
            return convertView;
        }
    }
}
