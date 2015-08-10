package handong.cconma.cconmaadmin.etc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ResourceBundle;

import handong.cconma.cconmaadmin.R;

/**
 * Created by Young Bin Kim on 2015-07-03.
 */
public class SwipeToRefresh extends Fragment {
    private SwipeRefreshLayout mSwipeRefresh;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.swipe_refresh, container, false);

        mSwipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );

        float density = getResources().getDisplayMetrics().density;
        mSwipeRefresh.setProgressViewOffset(true, Math.round((float)0 * density), Math.round((float)76 * density));

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }
}
