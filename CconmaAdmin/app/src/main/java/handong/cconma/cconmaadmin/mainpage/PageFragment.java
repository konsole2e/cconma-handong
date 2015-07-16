package handong.cconma.cconmaadmin.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import handong.cconma.cconmaadmin.board.BoardAdapter;
import handong.cconma.cconmaadmin.board.BoardViewActivity;
import handong.cconma.cconmaadmin.R;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static int layoutName;
    private int mPage;

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
        SpinnerAdapter adapter = new SpinnerAdapter(getActivity().getApplicationContext(),
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

        View footer = ((LayoutInflater)this.getActivity().getBaseContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.board_list_footer, null);

        ListView list_board = (ListView)view.findViewById(R.id.board_list);
        list_board.addFooterView(footer, null, false);
        BoardAdapter adapter_board = new BoardAdapter(view.getContext());
        list_board.setAdapter(adapter_board);
        adapter_board.addItem("1-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, false);
        adapter_board.addItem("2-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, true);
        adapter_board.addItem("3-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, false, true);
        adapter_board.addItem("4-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, false);
        adapter_board.addItem("5-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, false, true);
        adapter_board.addItem("6-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, false);
        adapter_board.addItem("7-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, false);
        adapter_board.addItem("8-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, false, false);
        adapter_board.addItem("9-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, false);
        adapter_board.addItem("10-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, true);
        adapter_board.addItem("11-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, false, true);
        adapter_board.addItem("12-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, false);
        adapter_board.addItem("13-7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, false, true);
        adapter_board.addItem("14-제일 아래거.", "신명재", "전체알림", "2015/07/07\n14:02:36", 3, true, false);

        paging(20542, 20, 1);
        list_board.setFocusable(false);

        list_board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), BoardViewActivity.class);
                startActivity(i);
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
    /* paging(int, int, int) : 전체 글의 수, 한 페이지에 볼 글의 수, 현재 페이지 번호 */
    public void paging(int total_article, int view_article, final int current_page) {

        // 페이지 그룹 단위
        int page_group = 5;

        // 전체 페이지 수 = 전체 글의 수 / 한 페이지에 볼 글의 수
        int total_page = (int) Math.ceil(total_article / view_article);

        // 이전 페이지 = 현재 페이지 - 1
        int prev_page = current_page - 1;
        if (prev_page < 1)   // 이전 페이지가 1보다 작으면 1로 고정
            prev_page = 1;

        // 다음 페이지 = 현재 페이지 + 1
        int next_page = current_page + 1;
        if (next_page > total_page)  // 다음 페이지가 전체 페이지보다 크면 전체 페이지 수로 고정
            next_page = total_page;

        // 현재 그룹의 시작 페이지
        int start_page;
        if (current_page % page_group == 0)
            start_page = current_page - (page_group - 1);
        else
            start_page = current_page - current_page % page_group + 1;

        // 현재 그룹의 마지막 페이지는 시작 페이지 + index_cut
        int end_page = start_page + page_group;

        // 이전 그룹은 시작 페이지 - 1
        int prev_group = start_page - 1;
        if (prev_group < 1)  // 이전 그룹이 1보다 작으면 1로 고정
            prev_group = 1;

        // 다음 그룹은 마지막 페이지
        int next_group = end_page;
        if (next_group > total_page) // 다음 그룹이 전체 페이지보다 크면 전체 페이지 수로 고정
            next_group = total_page;

        // 현재 페이지가 1페이지가 아니면 이전 그룹으로 가는 버튼 출력
        if (current_page != 1){

            TextView prevButton = new TextView(getActivity().getApplicationContext());
            prevButton.setWidth(40);
            prevButton.setHeight(100);
            prevButton.setPadding(0, 20, 0, 0);
            prevButton.setTextSize(18);
            prevButton.setTextColor(Color.BLACK);
            prevButton.setText("<");
            prevButton.setClickable(false);
            ((LinearLayout)view.findViewById(R.id.board_list_footer_layout)).addView(prevButton);
            prevButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //current_page = prev_group;
                }
            });

        }

        // 시작 페이지에서 마지막 페이지 전까지 반복
        for (int i = start_page; i < end_page; i++) {

            if (i > total_page)  // i가 전체 페이지를 초과하면 종료
                break;

            // 페이지 번호 버튼 출력
            if (i == current_page) {   // i가 현재 페이지와 일치

                // 현재 페이지 버튼은 선택할 수 없음
                TextView pageButton = new TextView(getActivity().getApplicationContext());
                pageButton.setWidth(40);
                pageButton.setHeight(100);
                pageButton.setPadding(0, 20, 0, 0);
                pageButton.setTextSize(18);
                pageButton.setTextColor(Color.BLUE);
                pageButton.setText(""+i);
                pageButton.setClickable(false);
                ((LinearLayout)view.findViewById(R.id.board_list_footer_layout)).addView(pageButton);

            } else {

                // 해당 페이지로 이동하는 버튼
                final TextView pageButton = new TextView(getActivity().getApplicationContext());
                pageButton.setWidth(40);
                pageButton.setHeight(100);
                pageButton.setPadding(0, 20, 0, 0);
                pageButton.setTextSize(18);
                pageButton.setTextColor(Color.BLACK);
                pageButton.setText(""+i);
                ((LinearLayout)view.findViewById(R.id.board_list_footer_layout)).addView(pageButton);
                pageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // ((Button)v).getText();   // 해당 페이지로 이동(액티비티 새로고침)
                        Toast.makeText(getActivity().getApplicationContext(), pageButton.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }

        // 현재 페이지가 전체 페이지가 아닐 경우 다음 그룹으로 가는 버튼 출력
        if (current_page != total_page){

            TextView nextButton = new TextView(getActivity().getApplicationContext());
            nextButton.setWidth(40);
            nextButton.setHeight(100);
            nextButton.setPadding(0, 20, 0, 0);
            nextButton.setTextSize(18);
            nextButton.setTextColor(Color.BLACK);
            nextButton.setText(">");
            nextButton.setClickable(false);
            ((LinearLayout) view.findViewById(R.id.board_list_footer_layout)).addView(nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // current_page = next_group;
                }
            });

        }

    }
}
