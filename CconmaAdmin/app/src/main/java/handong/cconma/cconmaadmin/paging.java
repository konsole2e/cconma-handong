package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by 최서율 on 2015-07-09.
 */
public class paging extends Activity{

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

            Button prevButton = new Button(this);
            prevButton.setWidth(20);
            prevButton.setText("<");
            prevButton.setClickable(false);
            ((LinearLayout) findViewById(R.id.paging)).addView(prevButton);
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
                Button pageButton = new Button(this);
                pageButton.setWidth(20);
                pageButton.setText(""+i);
                pageButton.setClickable(false);
                ((LinearLayout) findViewById(R.id.paging)).addView(pageButton);

            } else {

                // 해당 페이지로 이동하는 버튼
                Button pageButton = new Button(this);
                pageButton.setWidth(20);
                pageButton.setText(""+i);
                ((LinearLayout) findViewById(R.id.paging)).addView(pageButton);
                pageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // ((Button)v).getText();   // 해당 페이지로 이동(액티비티 새로고침)
                    }
                });

            }

        }

        // 현재 페이지가 전체 페이지가 아닐 경우 다음 그룹으로 가는 버튼 출력
        if (current_page != total_page){

            Button nextButton = new Button(this);
            nextButton.setWidth(20);
            nextButton.setText(">");
            nextButton.setClickable(false);
            ((LinearLayout) findViewById(R.id.paging)).addView(nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // current_page = next_group;
                }
            });

        }

    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.paging);

        paging(20542, 20, 1);

    }

}