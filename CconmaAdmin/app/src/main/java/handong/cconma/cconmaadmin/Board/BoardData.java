package handong.cconma.cconmaadmin.board;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eundi on 15. 7. 6..
 */
public class BoardData {

    public boolean check = false;
    public boolean boardAll;
    public boolean board_marked = false;    //즐겨찾기 **
    public int comment_count=0;

    public String notice_type;          //공지사항 여부
    public String board_no;             //게시판 번호
    public String boardarticle_no;      //게시글 번호
    public String name;                 //작성자 **
    public String subject;              //게시글 제목 **
    public String mem_no;               //회원 번호
    public String reg_date;             //게시글 날짜 **
    public String ip;                   //ip
    public String hit;                  //조회수
    public String board_short_name;     //게시판 이름

    public String comment_nicknames;    //댓글

    public HashMap hashMap;

}
