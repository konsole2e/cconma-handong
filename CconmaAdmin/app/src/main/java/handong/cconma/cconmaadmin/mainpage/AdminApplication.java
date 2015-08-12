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
    private String gcm_board_no;
    private String gcm_article_no;

    public static AdminApplication getInstance(){
        if( adminApplication == null ){
            adminApplication = new AdminApplication();
            return adminApplication;
        }
        return adminApplication;
    }

    public void setRefresh(boolean flag){ board_refresh = flag; }

    public void setBoardNo(String boardNo){ gcm_board_no = boardNo; }

    public void setArticleNo(String ArticleNo){ gcm_article_no = ArticleNo; }

    public boolean getRefresh(){ return board_refresh; }

    public void setTabPosition(int position){ tab_position = position; }

    public int getTabPosition(){ return tab_position; }

    public String getBoardNo(){ return gcm_board_no; }

    public String getArticleNo(){ return gcm_article_no; }
}
