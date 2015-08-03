package handong.cconma.cconmaadmin.statics;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by JS on 2015-08-03.
 */
public class StaticsFragement extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_PAGE_NO = "ARG_PAGE_NO";

    public static StaticsFragement newInstance(int page, int page_no) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_PAGE_NO, page_no);
        StaticsFragement fragment = new StaticsFragement();
        fragment.setArguments(args);

        return fragment;
    }
}
