package com.cconma.cconma_admin_test.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cconma.cconma_test_actionbar.R;

/**
 * Created by Young Bin Kim on 2015-07-08.
 */
public class BoardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.board_cconma, container, false);
    }
}
