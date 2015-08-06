package handong.cconma.cconmaadmin.mainpage;

import android.app.Application;

/**
 * Created by Young Bin Kim on 2015-08-05.
 */
public class AdminApplication extends Application {
    private boolean board_refresh;
    private boolean board_search;
    private int tab_position;
    private static AdminApplication adminApplication;

    public static AdminApplication getInstance(){
        if( adminApplication == null ){
            adminApplication = new AdminApplication();
            return adminApplication;
        }
        return adminApplication;
    }

    public void setRefresh(boolean flag){ board_refresh = flag; }

    public boolean getRefresh(){ return board_refresh; }

    public void setTabPosition(int position){ tab_position = position; }

    public int getTabPosition(){ return tab_position; }
}
