package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by JS on 2015-08-10.
 */
public class BoardSearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<BoardData> boardDataList;
    private String board_title;
    private String board_no;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private CircularProgressBar circularProgressBar;
    private BoardRecyclerAdapter adapter;
    private TextView searchTv;
    private GestureDetector mGestureDetector;
    private View actionView;

    //FrameLayout layout_board_search;
    Spinner spinner_board_condition;
    EditText edit_board_search;
    Button btn_board_search;

    String search_keyword = "";
    String search_cond = "";

    InputMethodManager input_manager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        //검색 조건 spinner
        actionView = menu.getItem(0).getActionView();
        spinner_board_condition = (Spinner) actionView.findViewById(R.id.spinner_board_condition);
        String[] cond = getResources().getStringArray(R.array.board_condition);
        final SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, cond);
        spinner_board_condition.setAdapter(adapter);
        spinner_board_condition.setSelection(0);
        //검색 입력
        edit_board_search = (EditText) actionView.findViewById(R.id.edit_board_search);
        btn_board_search = (Button) actionView.findViewById(R.id.btn_board_search);

        EditText edittext = edit_board_search;
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    btn_board_search.performClick();
                    return true;
                }
                return false;
            }
        });
        //검색하기 버튼
        btn_board_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.removeAllViewsInLayout();
                recyclerView.invalidate();
                if(edit_board_search.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (spinner_board_condition.getSelectedItem().equals("작성자"))
                    search_cond = "/writers/all?search_field=name&keyword=";
                else if (spinner_board_condition.getSelectedItem().equals("내용"))
                    search_cond = "/writers/all?search_field=content&keyword=";
                else if (spinner_board_condition.getSelectedItem().equals("제목"))
                    search_cond = "/writers/all?search_field=subject&keyword=";

                search_keyword = String.valueOf(edit_board_search.getText());
                try {
                    search_keyword = URLEncoder.encode(search_keyword, "UTF-8");
                } catch (Exception e) {

                }

                input_manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                input_manager.hideSoftInputFromWindow(edit_board_search.getWindowToken(), 0);
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_search);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("검색");

            board_no = getIntent().getStringExtra("board_no");
        board_title = getIntent().getStringExtra("board_title");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.progressbar_circular);
        searchTv = (TextView) findViewById(R.id.board_search_tv);
        searchTv.setText("\"" + board_title + "\" 게시판");

        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                mGestureDetector.onTouchEvent(e);
                return false;
            }
        });

        mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (e.getX() > 100) {
                    View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    int position = recyclerView.getChildAdapterPosition(view);
                    Log.d("debugging", "POSITION: " + String.valueOf(position));
                    // handle single tap
                    if (view != null && position != -1) {
                        Intent i = new Intent(BoardSearchActivity.this, BoardViewActivity.class);

                        Log.d("보드서치", boardDataList.get(position).board_no.toString() + " " + boardDataList.get(position).boardarticle_no.toString() + " " +
                                boardDataList.get(position).boardarticle_no);
                        i.putExtra("board_no", boardDataList.get(position).board_no);
                        i.putExtra("boardarticle_no", boardDataList.get(position).boardarticle_no);
                        i.putExtra("number", boardDataList.get(position).boardarticle_no);
                        i.putExtra("marked", boardDataList.get(position).board_marked);
                        i.putExtra("from", "search");

                        startActivity(i);
                    }
                }
                return super.onSingleTapConfirmed(e);
            }
        });
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.footer);
        recyclerView.removeView(rl);
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
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(15);
            return convertView;
        }
    }

    class BoardAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            searchTv.setText("\"" + board_title + "\" 게시판에서 \"" + edit_board_search.getText() + "\" 검색 중...");
            searchTv.setSelected(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("number", "load data");
            return loadData();
        }

        @Override
        protected void onPostExecute(Boolean empty) {
            adapter = new BoardRecyclerAdapter(boardDataList, getApplicationContext());
            recyclerView.setAdapter(adapter);
            searchTv.setText("\"" + board_title + "\" 게시판에서 \"" + edit_board_search.getText() + "\" 검색 완료");
            searchTv.setSelected(true);

            if (empty) {
                findViewById(R.id.search_none_tv).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.search_none_tv).setVisibility(View.GONE);
            }

            circularProgressBar.setVisibility(View.GONE);

            adapter.notifyDataSetChanged();
        }
    }

    public boolean loadData() {
        String sResult;
        boolean empty = true;
        try {
            String url = "http://www.cconma.com/admin/api/board/v1/boards/"
                    + board_no + search_cond + search_keyword;

            HttpConnection connection = new HttpConnection(url, "GET", "");
            sResult = connection.init();
            Log.d("보드서치 로드데이타", sResult);

            JSONObject jason = new JSONObject(sResult);
            JSONArray jsonArray = jason.getJSONArray("articles");
            boardDataList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                empty = false;
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

                if (hashArr.length() != 0) {
                    for (int j = 0; j < hashArr.length(); j++) {
                        JSONObject hashObj = hashArr.getJSONObject(j);
                        data.hashMap.put("hash_tag" + j, hashObj.getString("hash_tag"));
                        data.hashMap.put("hash_tag_type" + j, hashObj.getString("hash_tag_type"));
                    }
                }
                data.comment_nicknames = jsonObject.getString("comment_nicknames");

                Pattern pattern = Pattern.compile("\\(");
                Matcher matcher = pattern.matcher(data.comment_nicknames);
                while (matcher.find()) {
                    data.comment_count++;
                }

                JSONObject scrap = jsonObject.getJSONObject("scrap_info");
                String scrap_on = scrap.getString("scraped");

                if (scrap_on.equals("on"))
                    data.board_marked = true;
                else
                    data.board_marked = false;

                boardDataList.add(data);
            }
        } catch (Exception e) {
            Log.d("보드서치 로드데이타", "Exception in BoardFragement Line 125: " + e.getMessage());
        }
        return empty;
    }
}
