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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.board.BoardData;
import handong.cconma.cconmaadmin.board.BoardRecyclerAdapter;
import handong.cconma.cconmaadmin.board.BoardViewActivity;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;
import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-27.
 */
public class BoardFragment extends Fragment {
    private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
    public static final String ARG_PAGE_NO = "ARG_PAGE_NO";
    private List<BoardData> boardDataList;

    private static String TAG = "debugging";

    private String board_no;
    private int page_no;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private CircularProgressBar circularProgressBar;
    private View view;
    private BoardRecyclerAdapter adapter;

    private Boolean isLoading = false;
    private Boolean isReload = false;
    private int total;
    int isSearch;

    FrameLayout layout_board_search;
    Spinner spinner_board_condition;
    EditText edit_board_search;
    Button btn_board_search;

    String search_keyword="";
    String search_cond = "";

    InputMethodManager input_manager;

    public static BoardFragment newInstance(String page_no) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_NO, page_no);
        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page_no = 1;
        board_no = getArguments().getString(ARG_PAGE_NO);
        //boardDataList = new ArrayList<>();
        Log.d("debugging", "board no: " + board_no);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_layout, container, false);

        //검색창 열기/닫기 버튼
        /*btn_board_search_view = (ToggleButton)view.findViewById(R.id.btn_board_search_view);
        btn_board_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_board_search_view.isChecked()) {
                    layout_board_search.setVisibility(View.VISIBLE);
                } else {
                    layout_board_search.setVisibility(View.GONE);
                }
            }
        });*/
        //검색창 Frame Layout
        layout_board_search = (FrameLayout)view.findViewById(R.id.layout_board_search);
        //검색 조건 spinner
        spinner_board_condition = (Spinner)view.findViewById(R.id.spinner_board_condition);
        String[] cond = getResources().getStringArray(R.array.board_condition);
        final SpinnerAdapter adapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, cond);
        spinner_board_condition.setAdapter(adapter);
        spinner_board_condition.setSelection(0);
        //검색 입력
        edit_board_search = (EditText)view.findViewById(R.id.edit_board_search);
        //검색하기 버튼
        btn_board_search = (Button)view.findViewById(R.id.btn_board_search);
        btn_board_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner_board_condition.getSelectedItem().equals("작성자"))
                    search_cond = "/writers/all?search_field=name&keyword=";
                else if(spinner_board_condition.getSelectedItem().equals("내용"))
                    search_cond = "/writers/all?search_field=content&keyword=";
                else if(spinner_board_condition.getSelectedItem().equals("제목"))
                    search_cond = "/writers/all?search_field=subject&keyword=";

                search_keyword = String.valueOf(edit_board_search.getText());
                try {
                    search_keyword = URLEncoder.encode(search_keyword, "UTF-8");
                }catch(Exception e){

                }
                isSearch = 1;
                new BoardAsyncTask(getActivity().getApplicationContext()).execute("http://www.cconma.com/admin/api/board/v1/boards/"
                        + board_no + "/writers/all?page=" + "1" + "&n=20", "GET", "");
                Log.d("test", search_cond);

                input_manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input_manager.hideSoftInputFromWindow(edit_board_search.getWindowToken(), 0);
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        circularProgressBar = (CircularProgressBar)view.findViewById(R.id.progressbar_circular);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null)
            Log.d("STATE", "onViewCreated");
            new BoardAsyncTask(getActivity().getApplicationContext()).execute("http://www.cconma.com/admin/api/board/v1/boards/"
                    + board_no + "/writers/all?page=" + "1" + "&n=20", "GET", "");
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
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(15);
            return convertView;
        }
    }

    class LoadSearch extends AsyncTask<String, Void, Integer>{
        private String sResult;

        @Override
        protected void onPreExecute(){
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            loadData();
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            circularProgressBar.setVisibility(View.GONE);
            isLoading = false;
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
            Log.d("number", "load data");
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

            /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (isLoading == false) {
                        if (total > 0)
                            if ((total - 1) == lastVisibleItemCount){
                                isLoading = true;
                                Log.d("number", "LAST!!!!!");
                                new LoadMoreData().execute();
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
            });*/

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

    public void loadMoreData(){
        String sResult;
        try{
            page_no += 1;
            HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                    + board_no + "/writers/all?page=" + String.valueOf(page_no) + "&n=20", "GET", "");
            sResult = connection.init();
            Log.d(TAG, sResult);

            JSONObject jason = new JSONObject(sResult);
            JSONArray jsonArray = jason.getJSONArray("articles");
            //boardDataList = new ArrayList<>();

            for(int i=0; i<jsonArray.length(); i++){
                BoardData data = new BoardData();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

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
                isReload = true;
                adapter.notifyDataSetChanged();
            }
        }catch(Exception e){
            Log.d(TAG, "Exception in BoardFragement Line 125: " + e.getMessage());
        }
    }

}
