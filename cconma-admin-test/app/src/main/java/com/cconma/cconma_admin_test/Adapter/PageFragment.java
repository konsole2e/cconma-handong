package com.cconma.cconma_admin_test.Adapter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cconma.cconma_admin_test.Activity.BoardViewActivity;
import com.cconma.cconma_admin_test.Activity.MainActivity;
import com.cconma.cconma_test_actionbar.R;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static int layoutName;
    private int mPage;

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
}
