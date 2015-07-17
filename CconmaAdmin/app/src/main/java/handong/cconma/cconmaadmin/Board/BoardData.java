package handong.cconma.cconmaadmin.board;

/**
 * Created by eundi on 15. 7. 6..
 */
public class BoardData {

    public boolean board_file = false;      //파일 여부 ??

    public boolean board_marked = false;    //즐겨찾기 **


    public int comment_count;

    /**************************************************/
    public String board_no;             //게시판 번호
    public String boardarticle_no;      //게시글 번호

    public String notice_type;          //공지사항 여부
    public String hit;                  //조회수
    public String reg_data;             //게시글 날짜 **
    public String subject;              //게시글 제목 **
    public String board_short_name;     //게시판 이름
    public String name;                 //작성자 **
    public String article_hashtag;      //알림 종류 **

}
