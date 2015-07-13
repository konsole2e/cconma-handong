package handong.cconma.cconmaadmin.statics;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;

import handong.cconma.cconmaadmin.R;
<<<<<<< Updated upstream
import handong.cconma.cconmaadmin.mainpage.BaseActivity;

public class StaticsMain extends BaseActivity implements View.OnClickListener{
=======
import handong.cconma.cconmaadmin.etc.MyWebView;
import handong.cconma.cconmaadmin.mainpage.RecyclerViewAdapter;

public class StaticsMain extends AppCompatActivity implements View.OnClickListener{
>>>>>>> Stashed changes
    private Button orderH;
    private Button orderRcnt;
    private Button trade;
    private Button like;
    private Button member;
    private Button memberRcnt;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private String TITLES[] = {"게시판","통계","1:1문의","회원정보 조회", "마을지기 홈페이지"};
    private int ICONS[] = {R.drawable.ic_board_selector, R.drawable.ic_chart_selector, R.drawable.ic_question_selector, R.drawable.ic_search_grey600_48dp, R.drawable.ic_home_selector };
    private int returned = 0;
    private int status = 0;

    private String TITLESUSER[] = {"설정", "로그아웃"};
    private int ICONSUSER[] = {R.drawable.ic_setting_selector, R.drawable.ic_logout_selector};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_main);

<<<<<<< Updated upstream
        setDrawer();
=======
        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, "IT개발팀", 0);

        final GestureDetector mGestureDetector = new GestureDetector(StaticsMain.this, new GestureDetector.SimpleOnGestureListener() {
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
                    Intent intent = new Intent(StaticsMain.this, MyWebView.class);
                    switch(recyclerView.getChildPosition(child)){
                        case 0:
                            if (status == 0) {
                                recyclerAdapter = new RecyclerViewAdapter(TITLESUSER, ICONSUSER, "IT개발팀", 1);
                                recyclerView.setAdapter(recyclerAdapter);
                                status = 1;
                            } else if (status == 1) {
                                recyclerAdapter = new RecyclerViewAdapter(TITLES, ICONS, "IT개발팀", 0);
                                recyclerView.setAdapter(recyclerAdapter);
                                status = 0;
                            }
                            break;
                        case 1:
                            //go to board
                            break;
                        case 2:
                            //statistics
                            startActivity(new Intent(getApplicationContext(), StaticsMain.class));
                            break;
                        case 3:
                            intent.putExtra("URL", "http://www.cconma.com/CconmaAdmin/member.fmv?cmd=list");
                            startActivity(intent);
                            break;
                        case 4:
                            intent.putExtra("URL", "http://www.cconma.com/admin/help_board/help_board_list.pmv");
                            startActivity(intent);
                            break;
                        case 5:
                            intent.putExtra("URL", "http://www.cconma.com/CconmaAdmin/login.fmv?cmd=loginForm&path=%2FCconmaAdmin%2Fmain.fmv");
                            startActivity(intent);
                            break;
                        default:
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
>>>>>>> Stashed changes

        orderH = (Button)findViewById(R.id.order_hourly_btn);
        orderH.setOnClickListener(this);
        orderRcnt = (Button)findViewById(R.id.order_recent_btn);
        orderRcnt.setOnClickListener(this);
        trade = (Button)findViewById(R.id.trade_btn);
        trade.setOnClickListener(this);
        like = (Button)findViewById(R.id.like_btn);
        like.setOnClickListener(this);
        member = (Button)findViewById(R.id.member_btn);
        member.setOnClickListener(this);
        memberRcnt = (Button)findViewById(R.id.member_recent_btn);
        memberRcnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_hourly_btn :
                startActivity(new Intent(this, StaticsOrderH.class));
                break;
            case R.id.order_recent_btn:
                startActivity(new Intent(this, StaticsOrderRecent.class));
                break;
            case R.id.trade_btn :
                startActivity(new Intent(this, StaticsTrade.class));
                break;
            case R.id.like_btn:
                startActivity(new Intent(this, StaticsLike.class));
                break;
            case R.id.member_btn :
                startActivity(new Intent(this, StaticsMember.class));
                break;
            case R.id.member_recent_btn:
                startActivity(new Intent(this, StaticsMemberRecent.class));
                break;
        }
    }

}
