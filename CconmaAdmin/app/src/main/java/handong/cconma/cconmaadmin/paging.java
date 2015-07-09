package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by �ּ��� on 2015-07-09.
 */
public class paging extends Activity{

    /* paging(int, int, int) : ��ü ���� ��, �� �������� �� ���� ��, ���� ������ ��ȣ */
    public void paging(int total_article, int view_article, int current_page) {

        // ������ �׷� ����
        int page_group = 10;

        // ��ü ������ �� = ��ü ���� �� / �� �������� �� ���� ��
        int total_page = (int) Math.ceil(total_article / view_article);

        // ���� ������ = ���� ������ - 1
        int prev_page = current_page - 1;
        if (prev_page < 1)   // ���� �������� 1���� ������ 1�� ����
            prev_page = 1;

        // ���� ������ = ���� ������ + 1
        int next_page = current_page + 1;
        if (next_page > total_page)  // ���� �������� ��ü ���������� ũ�� ��ü ������ ���� ����
            next_page = total_page;

        // ���� �׷��� ���� ������
        int start_page;
        if (current_page % page_group == 0)
            start_page = current_page - (page_group - 1);
        else
            start_page = current_page - current_page % page_group + 1;

        // ���� �׷��� ������ �������� ���� ������ + index_cut
        int end_page = start_page + page_group;

        // ���� �׷��� ���� ������ - 1
        int prev_group = start_page - 1;
        if (prev_group < 1)  // ���� �׷��� 1���� ������ 1�� ����
            prev_group = 1;

        // ���� �׷��� ������ ������
        int next_group = end_page;
        if (next_group > total_page) // ���� �׷��� ��ü ���������� ũ�� ��ü ������ ���� ����
            next_group = total_page;

        // ���� �������� 1�������� �ƴϸ� ���� �׷����� ���� ��ư ���
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

            // ���� ���������� ������ ������ ������ �ݺ�
            for (int i = start_page; i < end_page; i++) {

                if (i > total_page)  // i�� ��ü �������� �ʰ��ϸ� ����
                    break;

            // ������ ��ȣ ��ư ���
            if (i == current_page) {   // i�� ���� �������� ��ġ

                // ���� ������ ��ư�� ������ �� ����
                Button pageButton = new Button(this);
                pageButton.setWidth(20);
                pageButton.setText(""+i);
                pageButton.setClickable(false);
                ((LinearLayout) findViewById(R.id.paging)).addView(pageButton);

            } else {

                // �ش� �������� �̵��ϴ� ��ư
                Button pageButton = new Button(this);
                pageButton.setWidth(20);
                pageButton.setText(""+i);
                ((LinearLayout) findViewById(R.id.paging)).addView(pageButton);
                pageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // ((Button)v).getText();   // �ش� �������� �̵�(��Ƽ��Ƽ ���ΰ�ħ)
                    }
                });

            }

        }

        // ���� �������� ��ü �������� �ƴ� ��� ���� �׷����� ���� ��ư ���
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