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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_fragment, container, false);

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
        spinner_board_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
                input_manager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input_manager.hideSoftInputFromWindow(edit_board_search.getWindowToken(), 0);
                layout_board_search.setVisibility(View.GONE);
                btn_board_search_view.setChecked(false);
            }
        });


        ListView list_board = (ListView)view.findViewById(R.id.board_list);
        BoardAdapter adapter_board = new BoardAdapter(view.getContext());
        list_board.setAdapter(adapter_board);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, true);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, false, true);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, false, true);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, false, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, true);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, false, true);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, false, true);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, true, false);
        adapter_board.addItem("7월 3주 공동구매 예고페이지 작업 요청", "신명재", "전체알림", "2015/07/07", 3, false, false);

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
}
