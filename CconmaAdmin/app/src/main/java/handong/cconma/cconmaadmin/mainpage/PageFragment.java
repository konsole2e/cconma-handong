package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import handong.cconma.cconmaadmin.board.BoardAdapter;
import handong.cconma.cconmaadmin.board.BoardData;
import handong.cconma.cconmaadmin.board.BoardViewActivity;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static int layoutName;
    private int mPage;
    JSONObject result;
    LinearLayout layout_list_update;
    ProgressBar progress;

    boolean lastitemVisibleFlag;
    boolean lock = false;
    ListView list_board;
    BoardAdapter adapter_board;

    ToggleButton btn_board_search_view;
    FrameLayout layout_board_search;
    Spinner spinner_board_condition;
    EditText edit_board_search;
    Button btn_board_search;

    InputMethodManager input_manager;
    View view;
    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onResume(){
        super.onResume();

        btn_board_search_view.setChecked(false);
        layout_board_search.setVisibility(View.GONE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_fragment, container, false);

        layout_list_update = (LinearLayout)view.findViewById(R.id.layout_list_update);
        progress = (ProgressBar)view.findViewById(R.id.progress_list_update);

        //검색창 열기/닫기 버튼
        btn_board_search_view = (ToggleButton)view.findViewById(R.id.btn_board_search_view);
        btn_board_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_board_search_view.isChecked()) {
                    layout_board_search.setVisibility(View.VISIBLE);
                } else {
                    layout_board_search.setVisibility(View.GONE);
                }
            }
        });
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
                Toast.makeText(getActivity().getApplicationContext(), spinner_board_condition.getSelectedItem().toString()
                        + " " + edit_board_search.getText(), Toast.LENGTH_SHORT).show();
                edit_board_search.setText("");
                input_manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input_manager.hideSoftInputFromWindow(edit_board_search.getWindowToken(), 0);
                layout_board_search.setVisibility(View.GONE);
                btn_board_search_view.setChecked(false);
            }
        });

        /*View footer = ((LayoutInflater)this.getActivity().getBaseContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.board_list_footer, null);*/

        list_board = (ListView)view.findViewById(R.id.board_list);
        //list_board.addFooterView(footer, null, false);
        adapter_board = new BoardAdapter(view.getContext());
        list_board.setAdapter(adapter_board);
        list_board.setOnTouchListener(touchListener);

        MainAsyncTask aysnc = new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/board_no/12", "GET", "");
        try{
            result = aysnc.execute().get();
        }catch(Exception e){
            Log.d("debugging", e.getMessage());
        }

        jsonParser(result);



        list_board.setFocusable(false);

        list_board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), BoardViewActivity.class);
                BoardData data = (BoardData)adapter_board.getItem(position);
                i.putExtra("number", data.boardarticle_no);
                startActivity(i);
            }
        });
        list_board.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag && (lock == false)) {

                    /**        데이터 넣기       **/
                    layout_list_update.setVisibility(View.VISIBLE);

                    //progress.setVisibility(View.VISIBLE);

                    new getMoreItems().execute();

                } else {

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });


        return view;
    }

    public class SpinnerAdapter extends ArrayAdapter<String>{

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


    public void jsonParser(JSONObject obj){

        try{
            int page = obj.getInt("page");
            int n = obj.getInt("n");
            JSONArray jsonArray = obj.getJSONArray("article_list");

            for(int i=0; i<jsonArray.length(); i++){
                Log.d("list", "testtest");
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //JSONArray commentArr = jsonObject.getJSONArray("comment");

                //int comment_count = commentArr.length();
                Log.d("list", "1111");

                String notice_type = jsonObject.getString("notice_type");
                Log.d("list", "22222");
                String board_no = jsonObject.getString("board_no");
                Log.d("list", "33333");
                String boardarticle_no = jsonObject.getString("boardarticle_no");
                Log.d("list", "44444");
                String name = jsonObject.getString("name");
                Log.d("list", "5555");
                String subject = jsonObject.getString("subject");
                Log.d("list", "6666");
                String mem_no = jsonObject.getString("mem_no");
                Log.d("list", "7777");
                String reg_date = jsonObject.getString("reg_date");
                Log.d("list", "8888");
                String ip = jsonObject.getString("ip");
                Log.d("list", "9999");
                String hit = jsonObject.getString("hit");
                Log.d("list", "10");
                String board_short_name = jsonObject.getString("board_short_name");
                JSONArray hashArr = jsonObject.getJSONArray("article_hash_tags");

                HashMap article_hash_tags = new HashMap();

                if(hashArr.length() != 0) {
                    for (int j = 0; j < hashArr.length(); j++) {
                        JSONObject hashObj = hashArr.getJSONObject(j);
                        String hash_tag = hashObj.getString("hash_tag");
                        String hash_tag_type = hashObj.getString("hash_tag_type");

                        article_hash_tags.put("hash_tag"+j, hash_tag);
                        article_hash_tags.put("hash_tag_type"+j, hash_tag_type);
                    }
                }
                Log.d("list", "33333333333");

                String comment_nicknames = jsonObject.getString("comment_nicknames");

                Log.d("list", "다 잘 받았을까");
                adapter_board.addItem(notice_type, board_no, boardarticle_no, name,
                        subject, mem_no, reg_date, ip, hit,
                        board_short_name, article_hash_tags, comment_nicknames);
            }
            //Log.d("list", "1222testtest");

        }catch(JSONException e){
            Log.e("JSON", Log.getStackTraceString(e));
        }

        //adapter_board.addItem(3, "12", "1223", "0", "96", "07/16 14:24", "11글 제목입니다.", "[꽃마]", "김은지", "전체알림");
        adapter_board.notifyDataSetChanged();
    }

    private class getMoreItems extends AsyncTask<ArrayList<String>, Integer, Long>{

        @Override
        protected Long doInBackground(ArrayList<String>... params) {

            lock = true;
            //데이터 넣기.

            try{
                Thread.sleep(3000);
            }catch(Exception e){

            }

            return null;
        }

        protected void onPostExecute(Long result){
            adapter_board.notifyDataSetChanged();
            layout_list_update.setVisibility(View.GONE);
            lock = false;
            //progress.setVisibility(View.GONE);
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
                        ((MainActivity)getActivity()).getSupportActionBar().show();
                    }
                }else{
                    dy = prevY -curY;
                    if(dy > 150){
                        ((MainActivity)getActivity()).getSupportActionBar().hide();
                    }
                }

            }

            return false;
        }
    };
}
