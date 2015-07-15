package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * Created by eundi on 15. 7. 6..
 */
public class BoardCconma extends Activity{

    Button btn_board_cconma;
    Button btn_board_develop;
    Button btn_board_cm;
    Button btn_board_cs;
    Button btn_board_marketing;
    Button btn_board_happyrice;
    Button btn_board_design;
    Button btn_board_distribution;

    ToggleButton btn_board_search_view;
    FrameLayout layout_board_search;
    Spinner spinner_board_condition;
    EditText edit_board_search;
    Button btn_board_search;

    ListView list_board;
    BoardAdapter adapter_board;

    ImageButton btn_board_write;

    Button btn_board_list_prev;
    Button btn_board_list_next;

    InputMethodManager input_manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_main);

        //게시판 바로가기 버튼 [꽃마보드][개발팀][CM][CS][마케팅][행밥팀][디자인][물류팀]
        btn_board_cconma = (Button)findViewById(R.id.btn_board_cconma);
        btn_board_cconma.setOnClickListener(buttonListener);
        btn_board_develop = (Button)findViewById(R.id.btn_board_develop);
        btn_board_develop.setOnClickListener(buttonListener);
        btn_board_cm = (Button)findViewById(R.id.btn_board_cm);
        btn_board_cm.setOnClickListener(buttonListener);
        btn_board_cs = (Button)findViewById(R.id.btn_board_cs);
        btn_board_cs.setOnClickListener(buttonListener);
        btn_board_marketing = (Button)findViewById(R.id.btn_board_marketing);
        btn_board_marketing.setOnClickListener(buttonListener);
        btn_board_happyrice = (Button)findViewById(R.id.btn_board_happyrice);
        btn_board_happyrice.setOnClickListener(buttonListener);
        btn_board_design = (Button)findViewById(R.id.btn_board_design);
        btn_board_design.setOnClickListener(buttonListener);
        btn_board_distribution = (Button)findViewById(R.id.btn_board_distribution);
        btn_board_distribution.setOnClickListener(buttonListener);


        //검색창 열기/닫기 버튼
        btn_board_search_view = (ToggleButton)findViewById(R.id.btn_board_search_view);
        btn_board_search_view.setOnClickListener(buttonListener);
        //검색창 Frame Layout
        layout_board_search = (FrameLayout)findViewById(R.id.layout_board_search);
        //검색 조건 spinner
        spinner_board_condition = (Spinner)findViewById(R.id.spinner_board_condition);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.board_condition,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_board_condition.setAdapter(spinnerAdapter);
        spinner_board_condition.setSelection(0);
        //검색 입력
        edit_board_search = (EditText)findViewById(R.id.edit_board_search);
        //검색하기 버튼
        btn_board_search = (Button)findViewById(R.id.btn_board_search);
        btn_board_search.setOnClickListener(buttonListener);


        //게시판 목록
        View footer = getLayoutInflater().inflate(R.layout.board_list_footer, null, false);
        list_board = (ListView)findViewById(R.id.list_board);
        list_board.addFooterView(footer);
        adapter_board = new BoardAdapter(this);
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
        list_board.setOnItemClickListener(itemclick);

        //page button
        //btn_board_list_next = (Button)findViewById(R.id.btn_board_list_next);
        //btn_board_list_prev = (Button)findViewById(R.id.btn_board_list_prev);
        paging(20542, 20, 1);

        //글쓰기 버튼
        btn_board_write = (ImageButton)findViewById(R.id.btn_board_write);
        btn_board_write.setOnClickListener(buttonListener);

    }

    AdapterView.OnItemClickListener itemclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent(BoardCconma.this, BoardView.class);
            startActivity(i);
        }
    };
    //버튼 클릭 리스너
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                //게시판 바로가기 버튼
                case R.id.btn_board_cconma:
                    Toast.makeText(getApplicationContext(), "cconma board", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_develop:
                    Toast.makeText(getApplicationContext(), "develop board", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_cm:
                    Toast.makeText(getApplicationContext(), "cm board", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_cs:
                    Toast.makeText(getApplicationContext(), "cs board", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_marketing:
                    Toast.makeText(getApplicationContext(), "marketing board", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_happyrice:
                    Toast.makeText(getApplicationContext(), "happyrice board", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_design:
                    Toast.makeText(getApplicationContext(), "design board", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_distribution:
                    Toast.makeText(getApplicationContext(), "distritution board", Toast.LENGTH_SHORT).show();
                    break;
                //게시판 검색 관련 버튼
                case R.id.btn_board_search_view:
                    if(btn_board_search_view.isChecked()){
                        layout_board_search.setVisibility(View.VISIBLE);
                    }else{
                        layout_board_search.setVisibility(View.GONE);
                    }
                    break;
                case R.id.btn_board_search:
                    Toast.makeText(getApplicationContext(), spinner_board_condition.getSelectedItem().toString()
                            + " " + edit_board_search.getText(), Toast.LENGTH_SHORT).show();
                    edit_board_search.setText("");
                    input_manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    input_manager.hideSoftInputFromWindow(edit_board_search.getWindowToken(), 0);
                    layout_board_search.setVisibility(View.GONE);
                    btn_board_search_view.setChecked(false);
                    break;
                case R.id.btn_board_write:
                    Intent i = new Intent(BoardCconma.this, BoardWrite.class);
                    startActivity(i);
                    break;
            }
        }
    };

    /* paging(int, int, int) : 전체 글의 수, 한 페이지에 볼 글의 수, 현재 페이지 번호 */
    public void paging(int total_article, int view_article, int current_page) {

        // 페이지 그룹 단위
        int page_group = 10;

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

            TextView prevButton = new TextView(this);
            prevButton.setWidth(20);
            prevButton.setText("<");
            prevButton.setClickable(false);
            ((LinearLayout) findViewById(R.id.board_list_footer_layout)).addView(prevButton);
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
                TextView pageButton = new TextView(this);
                pageButton.setWidth(20);
                pageButton.setText(""+i);
                pageButton.setClickable(false);
                ((LinearLayout) findViewById(R.id.board_list_footer_layout)).addView(pageButton);

            } else {

                // 해당 페이지로 이동하는 버튼
                TextView pageButton = new TextView(this);
                pageButton.setWidth(20);
                pageButton.setText(""+i);
                ((LinearLayout) findViewById(R.id.board_list_footer_layout)).addView(pageButton);
                pageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // ((Button)v).getText();   // 해당 페이지로 이동(액티비티 새로고침)
                    }
                });

            }

        }

        // 현재 페이지가 전체 페이지가 아닐 경우 다음 그룹으로 가는 버튼 출력
        if (current_page != total_page){

            TextView nextButton = new TextView(this);
            nextButton.setWidth(20);
            nextButton.setText(">");
            nextButton.setClickable(false);
            ((LinearLayout) findViewById(R.id.board_list_footer_layout)).addView(nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // current_page = next_group;
                }
            });

        }

    }

}
