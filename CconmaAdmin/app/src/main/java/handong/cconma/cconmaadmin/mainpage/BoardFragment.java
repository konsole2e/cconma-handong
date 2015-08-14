package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardData;
import handong.cconma.cconmaadmin.board.BoardRecyclerAdapter;
import handong.cconma.cconmaadmin.board.BoardSearchActivity;
import handong.cconma.cconmaadmin.board.BoardViewActivity;
import handong.cconma.cconmaadmin.board.MyLinearLayoutManager;
import handong.cconma.cconmaadmin.customview.SwipeRefreshLayoutBottom;
import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-27.
 */
public class BoardFragment extends Fragment {
    private static BoardFragment boardFragment;
    private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
    public static final String ARG_PAGE_NO = "ARG_PAGE_NO";
    public static final String ARG_TITLE = "ARG_TITLE";
    private List<BoardData> boardDataList;

    private static String TAG = "debugging";
    private String board_title;
    private String board_no;
    private int page_no;
    private int overallYScroll = 0;
    private int overallXScroll = 0;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private CircularProgressBar circularProgressBar;
    private BoardRecyclerAdapter adapter;
    private SwipeRefreshLayoutBottom refresh_bottom;
    private FloatingActionButton fab_up;

    private Boolean isLoading = false;
    private Boolean isReload = false;
    private int total = 0;
    private int firstVisibleItemCount;
    private int lastVisibleItemCount;
    private GestureDetector mGestureDetector;

    public static BoardFragment newInstance(String page_no, String title) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_NO, page_no);
        args.putString(ARG_TITLE, title);
        boardFragment = new BoardFragment();
        boardFragment.setArguments(args);
        return boardFragment;
    }

    public static BoardFragment getInstance(){
        if (boardFragment == null) {
            boardFragment = new BoardFragment();
            return boardFragment;
        }else{
            return boardFragment;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page_no = 1;
        setHasOptionsMenu(true);
        board_no = getArguments().getString(ARG_PAGE_NO);
        board_title = getArguments().getString(ARG_TITLE);
        Log.d("debugging", "board no: " + board_no + "board title: " + board_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.board_layout, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        linearLayoutManager = new MyLinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false, 500);

        recyclerView.setLayoutManager(linearLayoutManager);
        circularProgressBar = (CircularProgressBar)view.findViewById(R.id.progressbar_circular);
        refresh_bottom = (SwipeRefreshLayoutBottom)view.findViewById(R.id.refresh_bottom);
        fab_up = (FloatingActionButton)getActivity().findViewById(R.id.fab_up);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            Intent intent = new Intent(getActivity(), BoardSearchActivity.class);
            intent.putExtra("board_no", board_no);
            intent.putExtra("board_title", board_title);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh(Context context){
        new BoardAsyncTask_test(context, 0).execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        //if(savedInstanceState == null)
            new BoardAsyncTask_test(getActivity().getApplicationContext(), 0).execute();
        mGestureDetector = new GestureDetector(
                getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (e.getX() > 100) {
                    View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    int position = recyclerView.getChildAdapterPosition(view);
                    Log.d("debugging", "POSITION: " + String.valueOf(position));
                    // handle single tap
                    if (view != null && position != -1) {
                        Intent i = new Intent(getActivity(), BoardViewActivity.class);

                        i.putExtra("board_no", boardDataList.get(position).board_no);
                        i.putExtra("boardarticle_no", boardDataList.get(position).boardarticle_no);
                        i.putExtra("number", boardDataList.get(position).boardarticle_no);
                        i.putExtra("marked", boardDataList.get(position).board_marked);
                        i.putExtra("from", "fragment");

                        startActivity(i);
                    }
                }
                return super.onSingleTapConfirmed(e);
            }

            public void onLongPress(MotionEvent e) {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerView.getChildAdapterPosition(view);
                // handle long press

                super.onLongPress(e);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                mGestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }
        });

        refresh_bottom.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );
        refresh_bottom.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_bottom.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new BoardAsyncTask_test(getActivity().getApplicationContext(), -1).execute();
                    }
                }, 300);
            }
        });
    }

    class BoardAsyncTask_test extends AsyncTask<Integer, Void, Void>{
        private Context context;
        private int page;

        public BoardAsyncTask_test(Context context, int page){
            this.context = context;
            this.page = page;
        }

        @Override
        protected void onPreExecute(){
            if( page == 0 )
                circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            FetchData(page);
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            if( page == 0 ) {
                adapter = new BoardRecyclerAdapter(boardDataList, context);
                recyclerView.setAdapter(adapter);
                //recyclerView.setItemAnimator(null);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        overallXScroll = overallXScroll + dx;
                        overallYScroll = overallYScroll + dy;
                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                        Log.d("debugging", " " + overallYScroll + " " + displaymetrics.heightPixels);
                        total = linearLayoutManager.getItemCount();
                        //firstVisibleItemCount = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        firstVisibleItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                        lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();

                        View view = recyclerView.getChildAt(firstVisibleItemCount);
                        int itemHeight = 148;
                        if (view != null) {
                            itemHeight = recyclerView.getChildAt(firstVisibleItemCount).getHeight();
                        }
                        if (!isLoading) {
                            if (total > 0)
                                if ((total - 1) == lastVisibleItemCount) {
                                }
                        }
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                    }
                });
            }
            refresh_bottom.setRefreshing(false);
            circularProgressBar.setVisibility(View.GONE);
            adapter.notifyItemInserted(boardDataList.size() - 1);

            //recyclerView.scrollBy(overallXScroll, overallYScroll);
            //linearLayoutManager.scrollToPositionWithOffset(0, overallXScroll);
            // recyclerView.smoothScrollBy(0, overallXScroll);
            //overallYScroll = 0;
        }
    }

    protected void FetchData(int pages){
        /*
        * "flags" is to determine whether it needs data for search or not (default is 0)
        * "pages" is to initialize the data, 0 to initialize and -1 to continue
        * */

        String sResult;

        if( pages == 0 ){
            page_no = pages;
            boardDataList = new ArrayList<>();
        }

        try{
            page_no += 1;
            HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                    + board_no + "/writers/all?page=" + page_no + "&n=20", "GET", "");
            sResult = connection.init();
            Log.d(TAG, "sResult is " + sResult);

            JSONObject jason = new JSONObject(sResult);
            JSONArray jsonArray = jason.getJSONArray("articles");

            for(int i=0; i<jsonArray.length(); i++){
                BoardData data = new BoardData();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if(board_no.equals("all"))
                    data.boardAll = true;
                else
                    data.boardAll = false;
                data.notice_type = jsonObject.getString("notice_type");
                data.board_no = jsonObject.getString("board_no");
                data.boardarticle_no = jsonObject.getString("boardarticle_no");
                data.name = jsonObject.getString("name");
                data.subject = jsonObject.getString("subject");
                data.mem_no = jsonObject.getString("mem_no");
                data.reg_date = jsonObject.getString("reg_date");
                data.ip = jsonObject.getString("ip");
                data.hit = jsonObject.getString("hit");
                data.board_short_name = jsonObject.getString("board_short_name");

                JSONArray hashArr = jsonObject.getJSONArray("article_hash_tags");
                data.hashMap = new HashMap();

                if(hashArr.length()!=0) {
                    for (int j = 0; j < hashArr.length(); j++) {
                        JSONObject hashObj = hashArr.getJSONObject(j);
                        data.hashMap.put("hash_tag"+j, hashObj.getString("hash_tag"));
                        data.hashMap.put("hash_tag_type"+j, hashObj.getString("hash_tag_type"));
                    }
                }
                data.comment_nicknames = jsonObject.getString("comment_nicknames");

                Pattern pattern = Pattern.compile("\\(");
                Matcher matcher = pattern.matcher(data.comment_nicknames);
                while(matcher.find()){
                    data.comment_count++;
                }

                JSONObject scrap = jsonObject.getJSONObject("scrap_info");
                String scrap_on = scrap.getString("scraped");

                if(scrap_on.equals("on"))
                    data.board_marked = true;
                else
                    data.board_marked = false;

                boardDataList.add(data);
                isLoading = false;
            }
        }catch(Exception e){
            Log.d(TAG, "Exception in BoardFragement Line 125: " + e.getMessage());
        }
    }
}
