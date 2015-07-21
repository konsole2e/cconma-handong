package handong.cconma.cconmaadmin.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eundi on 15. 7. 6..
 */
public class BoardData {

    public boolean board_file = false;      //파일 여부 ??

    public boolean board_marked = false;    //즐겨찾기 **


    public int comment_count=0;

    /**************************************************/
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

    public HashMap article_hash_tags;   //알림 종류 / hash_tag, hash_tag_type
    public String comment_nicknames;    //댓글



    public int hash_count;

}
