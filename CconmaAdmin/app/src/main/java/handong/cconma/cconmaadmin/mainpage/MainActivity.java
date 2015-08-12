package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.HashMap;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardMarkedActivity;
import handong.cconma.cconmaadmin.board.BoardViewActivity;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.etc.LogoutWebView;
import handong.cconma.cconmaadmin.etc.SettingActivity;
import handong.cconma.cconmaadmin.etc.SwipeToRefresh;
import handong.cconma.cconmaadmin.push.PushView;

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
    private int position = R.id.board;
    private int menu_count;
    private Context context;
    private ImageView arrow;
    private static final String TAG = "debugging";

    private CharSequence mTitle;
    private onKeyBackPressedListener mOnKeyBackPressedListener = null;

    private MenuItem mPreviousMenuItem;

    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

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
        TextView textview = (TextView) view.findViewById(R.id.name);
        RelativeLayout header = (RelativeLayout) view.findViewById(R.id.drawer_header);
        arrow = (ImageView) view.findViewById(R.id.user_arrow);
        textview.setText(BasicData.getInstance().getName());

        header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (status == 1) {
                    drawerDefault();
                } else {
                    drawerUser();
                }
            }
        });

        getDynamicMenu();
        navigationView.addHeaderView(view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setCheckable(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
                if (mPreviousMenuItem == menuItem) {
                    return true;
                } else {
                    menuItem.setChecked(true);
                    if (mPreviousMenuItem != null) {
                        mPreviousMenuItem.setChecked(false);
                    }
                    mDrawerLayout.closeDrawers();

                    position = menuItem.getItemId();
                    Log.d(TAG, String.valueOf(position));

                    if (status == 0) {
                        mPreviousMenuItem = menuItem;
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
                                if (position <= menu_count) {
                                    selectItem(position);
                                }
                        }
                    } else {
                        switch (position) {
                            case R.id.user_settings:
                                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                drawerDefault();
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
            }

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        Random rand = new Random();
        int randnum = (int) rand.nextInt(8);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_content_frame);
        switch (randnum) {
            case 0:
                frameLayout.setBackgroundResource(R.drawable.background03);
                break;

            case 1:
                frameLayout.setBackgroundResource(R.drawable.background04);
                break;

            case 2:
                frameLayout.setBackgroundResource(R.drawable.background07);
                break;

            case 3:
                frameLayout.setBackgroundResource(R.drawable.background09);
                break;

            case 4:
                frameLayout.setBackgroundResource(R.drawable.background11);
                break;
            case 5:
                frameLayout.setBackgroundResource(R.drawable.background3);
                break;
            case 6:
                frameLayout.setBackgroundResource(R.drawable.background12);
                break;
            case 7:
                frameLayout.setBackgroundResource(R.drawable.background14);
                break;
        }


        if (savedInstanceState == null) {
            MenuItem menuItem_board = navigationView.getMenu().findItem(R.id.board);
            mPreviousMenuItem = menuItem_board;
            mPreviousMenuItem.setCheckable(true);
            mPreviousMenuItem.setChecked(true);
            selectItem(-1);
        }

        String board_no = AdminApplication.getInstance().getBoardNo();
        String boardArticle_no = AdminApplication.getInstance().getArticleNo();
        if (board_no != null && boardArticle_no != null) {
            Intent intent = new Intent(this, BoardViewActivity.class);
            intent.putExtra("board_no", board_no);
            intent.putExtra("boardarticle_no", boardArticle_no);
            intent.putExtra("from", "push");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            AdminApplication.getInstance().setBoardNo(null);
            AdminApplication.getInstance().setArticleNo(null);
        }
    }

    public void getDynamicMenu() {
        Menu menu = navigationView.getMenu();
        HashMap temp = BasicData.getInstance().getMenuNameList();
        menu_count = temp.size();

        for (int i = 0; i < menu_count; i++) {
            menu.add(1, i + 1, 0, temp.get("menu_name" + i).toString()).setIcon(R.drawable.ic_web_white_36dp);
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

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
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
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - backPressedTime;

                if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ){
                    finish();
                }else{
                    backPressedTime = tempTime;
                    Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르면 종료", Toast.LENGTH_SHORT).show();
                }
            } else {
                fragmentManager.popBackStack();
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem m = menu.getItem(i);
                    if (m.hasSubMenu()) {
                        for (int j = 0; j < m.getSubMenu().size(); j++) {
                            m.getSubMenu().getItem(j).setChecked(false);
                        }
                    }
                    m.setChecked(false);
                }

                int fPosition = Integer.valueOf(fragmentManager.getFragments().get(count-2).getTag());
                MenuItem prevMenuItem = navigationView.getMenu().findItem(fPosition);
                prevMenuItem.setChecked(true);
                mPreviousMenuItem = prevMenuItem;
            }
        }
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

    private void selectItem(final int mPosition) {
        Fragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(MainFragment.POSITION, mPosition);
        fragment.setArguments(args);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.main_content_frame, fragment, String.valueOf(position));
        ft.addToBackStack(null);
        Log.d(TAG, "fragment stack: " + String.valueOf(getFragmentManager().getBackStackEntryCount()));
        ft.commit();
    }

    private void drawerDefault() {
        navigationView.inflateMenu(R.menu.menu_default);
        Menu menu = navigationView.getMenu();
        menu.removeGroup(R.id.menu_user);
        getDynamicMenu();
        arrow.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
        status = 0;
    }

    private void drawerUser() {
        navigationView.inflateMenu(R.menu.menu_user);
        Menu menu = navigationView.getMenu();
        menu.removeGroup(R.id.menu_default);
        menu.removeGroup(1);
        arrow.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
        status = 1;
    }
}
