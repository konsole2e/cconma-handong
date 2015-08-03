package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static final String ARG_PAGE_NO = "ARG_PAGE_NO";
    private List<BoardData> boardDataList;

    private static String TAG = "debugging";

    private String mPage_no;
    private RecyclerView recyclerView;
    private CircularProgressBar circularProgressBar;
    private View view;
    private BoardRecyclerAdapter adapter;

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
        mPage_no = getArguments().getString(ARG_PAGE_NO);
        Log.d("debugging", "page no: " + mPage_no);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_layout, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        circularProgressBar = (CircularProgressBar)view.findViewById(R.id.progressbar_circular);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null)
            new BoardAsyncTask(getActivity().getApplicationContext()).execute("http://www.cconma.com/admin/api/board/v1/boards/"
                    + mPage_no + "/writers/all?page=" + "1" + "&n=20", "GET", "");
    }

    class BoardAsyncTask extends AsyncTask<String, Void, Integer> {
        private String sResult;
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
            try {
                URL url = new URL(params[0]);
                HttpConnection connection = new HttpConnection(params[0], params[1], params[2]);
                sResult = connection.init();
                Log.d(TAG, sResult);
            }catch(Exception e){
                Log.d(TAG, "Exception error in BoardAsyncTask" + e.getMessage());
            }
            jsonParser(sResult);
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            adapter = new BoardRecyclerAdapter(boardDataList, context);
            recyclerView.setAdapter(adapter);

            final GestureDetector mGestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    int position = recyclerView.getChildAdapterPosition(view);
                    // handle single tap
                    Intent i = new Intent(getActivity(), BoardViewActivity.class);

                    i.putExtra("board_no", boardDataList.get(position).board_no);
                    i.putExtra("boardarticle_no", boardDataList.get(position).boardarticle_no);
                    i.putExtra("number",  boardDataList.get(position).boardarticle_no);

                    startActivity(i);

                    return super.onSingleTapConfirmed(e);
                }

                public void onLongPress(MotionEvent e) {
                    View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    int position = recyclerView.getChildAdapterPosition(view);
                    // handle long press

                    super.onLongPress(e);
                }
            });

            /*recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(
                    mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    // do somthing...

                    loadMoreData(current_page);

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

    public void jsonParser(String result){
        try{
            JSONObject jason = new JSONObject(result);
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

                boardDataList.add(data);
            }
        }catch(Exception e){
            Log.d(TAG, "Exception in BoardFragement Line 125: " + e.getMessage());
        }
    }
}
