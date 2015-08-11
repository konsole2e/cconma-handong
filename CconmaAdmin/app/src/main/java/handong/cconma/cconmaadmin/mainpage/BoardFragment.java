package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private int overallXScroll = 0;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private CircularProgressBar circularProgressBar;
    private BoardRecyclerAdapter adapter;

    private Boolean isLoading = false;
    private Boolean isReload = false;
    private int total = 0;
    private int firstVisibleItemCount;
    private int lastVisibleItemCount;
    int isSearch;

    private View actionView;

    //FrameLayout layout_board_search;
    Spinner spinner_board_condition;
    EditText edit_board_search;
    Button btn_board_search;

    String search_keyword="";
    String search_cond = "";

    InputMethodManager input_manager;

    public static BoardFragment newInstance(String page_no, String title) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_NO, page_no);
        args.putString(ARG_TITLE, title);
        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(args);
        return fragment;
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
        Log.d(TAG, "onRESUME in BoardFragment");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page_no = 1;
        setHasOptionsMenu(true);
        board_no = getArguments().getString(ARG_PAGE_NO);
        board_title = getArguments().getString(ARG_TITLE);
        //boardDataList = new ArrayList<>();
        Log.d("debugging", "board no: " + board_no + "board title: " + board_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.board_layout, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setOnTouchListener(touchListener);
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        circularProgressBar = (CircularProgressBar)view.findViewById(R.id.progressbar_circular);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent intent = new Intent(getActivity(), BoardSearchActivity.class);
            intent.putExtra("board_no", board_no);
            intent.putExtra("board_title", board_title);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null)
            Log.d("STATE", "onViewCreated");
            new BoardAsyncTask_test(getActivity().getApplicationContext(), 0, 0).execute();
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {

        Context context;
        String items[];
        public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects){
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

            TextView tv = (TextView)convertView
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
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(15);
            return convertView;
        }
    }

    class BoardAsyncTask_test extends AsyncTask<Integer, Void, Void>{
        private Context context;
        private int flags;
        private int page;

        public BoardAsyncTask_test(Context context, int flags, int page){
            this.context = context;
            this.flags = flags;
            this.page = page;
        }

        @Override
        protected void onPreExecute(){
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            FetchData(flags, page);
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            if( page == 0 ) {
                adapter = new BoardRecyclerAdapter(boardDataList, context);
                recyclerView.setAdapter(adapter);
                recyclerView.setItemAnimator(null);

                final GestureDetector mGestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (e.getX() > 100) {
                            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            int position = recyclerView.getChildAdapterPosition(view);
                            Log.d("debugging", "POSITION: " + String.valueOf(position));
                            // handle single tap
                            if (view != null && position != -1) {
                                Intent i = new Intent(getActivity(), BoardViewActivity.class);

                                Log.d(TAG, boardDataList.get(position).board_no.toString() + " " + boardDataList.get(position).boardarticle_no.toString() + " " +
                                        boardDataList.get(position).boardarticle_no);
                                i.putExtra("board_no", boardDataList.get(position).board_no);
                                i.putExtra("boardarticle_no", boardDataList.get(position).boardarticle_no);
                                i.putExtra("number", boardDataList.get(position).boardarticle_no);
                                i.putExtra("marked", boardDataList.get(position).board_marked);

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

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        Log.d("debugging", "DX:  " + dy);
                        overallXScroll = overallXScroll + dy;
                        Log.d("debugging", "OVERALL DX:  " + overallXScroll);

                        total = linearLayoutManager.getItemCount();
                        //firstVisibleItemCount = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        firstVisibleItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                        lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                        Log.d("number", String.valueOf(total) + " " + String.valueOf(firstVisibleItemCount) + " "
                                + String.valueOf(lastVisibleItemCount));
                        Log.d("number", String.valueOf(isLoading));

                        View view = recyclerView.getChildAt(firstVisibleItemCount);
                        int itemHeight = 148;
                        if( view != null ) {
                            itemHeight = recyclerView.getChildAt(firstVisibleItemCount).getHeight();
                        }
                        Log.d("number", String.valueOf(itemHeight));
                        if (!isLoading) {
                            if (total > 0)
                                if ((total - 1) == lastVisibleItemCount) {
                                    //isLoading = true;
                                    //new BoardAsyncTask_test(context, 0, -1).execute();
                                    Log.d("number", "LAST!!!!!");
                                } else {
                                    Log.d("number", "NO NO NO NO NO");
                                }
                        } else {
                            Log.d("number", "is LOADING");
                        }
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

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
            }

            circularProgressBar.setVisibility(View.GONE);
            adapter.notifyItemInserted(boardDataList.size() - 1);

           // recyclerView.smoothScrollBy(0, overallXScroll);
           // overallXScroll = 0;

        }
    }

    class BoardAsyncTask extends AsyncTask<String, Void, Integer> {
        private Context context;

        public BoardAsyncTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute(){
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            adapter = new BoardRecyclerAdapter(boardDataList, context);
            recyclerView.setAdapter(adapter);

            final GestureDetector mGestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if(e.getX()>100) {

                        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        int position = recyclerView.getChildAdapterPosition(view);
                        Log.d("debugging", "POSITION: " + String.valueOf(position));
                        // handle single tap
                        if (view != null && position != -1) {
                            Intent i = new Intent(getActivity(), BoardViewActivity.class);

                            Log.d(TAG, boardDataList.get(position).board_no.toString() + " " + boardDataList.get(position).boardarticle_no.toString() + " " +
                                    boardDataList.get(position).boardarticle_no);
                            i.putExtra("board_no", boardDataList.get(position).board_no);
                            i.putExtra("boardarticle_no", boardDataList.get(position).boardarticle_no);
                            i.putExtra("number", boardDataList.get(position).boardarticle_no);
                            i.putExtra("marked", boardDataList.get(position).board_marked);

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

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    total = linearLayoutManager.getItemCount();
                    int firstVisibleItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                    Log.d("number", String.valueOf(total) + " "  + String.valueOf(firstVisibleItemCount) + " "
                            + String.valueOf(lastVisibleItemCount));
                    Log.d("number", String.valueOf(isLoading));
                    //to avoid multiple calls to loadMore() method
                    //maintain a boolean value (isLoading). if loadMore() task started set to true and completes set to false
                    if (!isLoading) {
                        if (total > 0)
                            if ((total - 1) == lastVisibleItemCount){
                                isLoading = true;
                                Log.d("number", "LAST!!!!!");
                            }else{
                                Log.d("number", "NO NO NO NO NO");
                            }
                    }else{
                        Log.d("number", "is LOADING");
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

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
            circularProgressBar.setVisibility(View.GONE);

            adapter.notifyDataSetChanged();
            //((CircularProgressDrawable)circularProgressBar.getIndeterminateDrawable()).progressiveStop();
        }
    }

    public void loadData(){
        String sResult;
        try {
            String url = "";
            if (isSearch == 0) {
                url = "http://www.cconma.com/admin/api/board/v1/boards/"
                        + board_no + "/writers/all?page=" + String.valueOf(page_no) + "&n=50";
            } else{
               url = "http://www.cconma.com/admin/api/board/v1/boards/"
                       + board_no + search_cond + search_keyword;
            }

            HttpConnection connection = new HttpConnection(url, "GET", "");
            sResult = connection.init();
            Log.d(TAG, sResult);

            JSONObject jason = new JSONObject(sResult);
            JSONArray jsonArray = jason.getJSONArray("articles");
            boardDataList = new ArrayList<>();

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
                isReload = false;

            }
        }catch(Exception e){
            Log.d(TAG, "Exception in BoardFragement Line 125: " + e.getMessage());
        }
    }

    protected void FetchData(int flags, int pages){
        /*
        * "flags" is to determine whether it needs data for search or not (default is 0)
        * "pages" is to initialize the data, 0 to initialize and -1 to continue
        * */

        String sResult;
        String url;

        if( pages == 0 ){
            page_no = pages;
            boardDataList = new ArrayList<>();
        }

        if( flags == 0 ){

        }
        else if( flags == 1 ){

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

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        int prevY, curY, dy;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                prevY = (int)event.getY();
            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                curY = (int)event.getY();

                if(prevY < curY){
                    dy = curY-prevY;
                    if(dy > 150){

                    }
                }else{
                    dy = prevY -curY;
                    if(dy > 150){
                    }
                }

            }

            return false;
        }
    };
}
