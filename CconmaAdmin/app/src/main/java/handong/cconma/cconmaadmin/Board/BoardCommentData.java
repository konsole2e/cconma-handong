package handong.cconma.cconmaadmin.board;

import java.util.HashMap;

/**
 * Created by eundi on 15. 7. 6..
 */
public class BoardCommentData {

    public String comment_no;
    public String comment_mem_no;
    public String commnet_writer;
    public String comment;
    public String comment_ip;
    public String comment_date;


    public HashMap comment_hashMap;


    public void setComment(String comment){
        this.comment = comment;
    }

}
