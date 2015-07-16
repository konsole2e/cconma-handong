package handong.cconma.cconmaadmin.board;

/**
 * Created by eundi on 15. 7. 6..
 */
public class BoardData {

    public int board_num;           //게시판 번호
    public int article_num;         //게시글 번호

    public String board_date;       //게시글 날짜
    public String board_title;      //게시글 제목
    public String board_notice;     //알림 종류
    public String board_writer;     //작성자
    public int board_comment_num;   //댓글 수
    public boolean board_file;      //파일 여부

    public boolean board_marked;    //즐겨찾기 여부
}
