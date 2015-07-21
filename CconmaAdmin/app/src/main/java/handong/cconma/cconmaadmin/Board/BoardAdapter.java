package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

import handong.cconma.cconmaadmin.R;


/**
 * Created by eundi on 15. 7. 6..
 */
public class BoardAdapter extends BaseAdapter{

    public Context context = null;
    public ArrayList<BoardData> board_list_data = new ArrayList<BoardData>();

    public BoardAdapter(Context context){
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return board_list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return board_list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_list_item, null);

            holder.text_board_title = (TextView)convertView.findViewById(R.id.text_board_title);
            holder.text_board_comment_num = (TextView)convertView.findViewById(R.id.text_board_comment_num);
            holder.text_board_date = (TextView)convertView.findViewById(R.id.text_board_date);
            holder.text_board_notice = (TextView)convertView.findViewById(R.id.text_board_notice);
            holder.text_board_writer = (TextView)convertView.findViewById(R.id.text_board_writer);
            holder.btn_board_mark = (ToggleButton)convertView.findViewById(R.id.btn_board_mark);
            holder.btn_board_mark.setFocusable(false);
            holder.img_board_file = (ImageView)convertView.findViewById(R.id.img_board_file);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        BoardData data = board_list_data.get(position);
        holder.text_board_title.setText(data.subject);
        holder.text_board_comment_num.setText("+" + data.comment_count);
        holder.text_board_date.setText(data.reg_data);
        for(int i=0; i<data.hash_count; i++){
            holder.text_board_notice.setText((data.article_hash_tags).get("hash_tag"+i).toString());
        }
        holder.text_board_writer.setText(data.name);

        holder.btn_board_mark.setChecked(data.board_marked);
        holder.btn_board_mark.setTag(position);
        holder.btn_board_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_board_mark.isChecked()){
                    Toast.makeText(context, "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                    board_list_data.get((Integer)v.getTag()).board_marked = true;
                }else{
                    Toast.makeText(context, "즐겨찾기 해제", Toast.LENGTH_SHORT).show();
                    board_list_data.get((Integer)v.getTag()).board_marked = false;
                }
            }
        });
        if(data.board_file)
            holder.img_board_file.setVisibility(View.VISIBLE);
        else
            holder.img_board_file.setVisibility(View.GONE);

        return convertView;
    }

    //파싱한 데이터 저장하기
    //  댓글 개수, 게시판 번호, 게시글 번호, 공지사항여부, 조회수,
    //  작성 날짜, 게시글 제목, 게시판 이름, 작성자, 알림 종류
    public void addItem(String notice_type, String board_no, String boardarticle_no, String name,
                        String subject, String mem_no, String reg_data, String ip, String hit,
                        String board_short_name, HashMap article_hash_tags, String comment_nicknames){

        BoardData addData = new BoardData();

        addData.notice_type = notice_type;
        addData.board_no = board_no;
        addData.boardarticle_no = boardarticle_no;
        addData.name = name;

        addData.subject = subject;
        addData.mem_no = mem_no;
        addData.reg_data = reg_data;
        addData.ip = ip;
        addData.hit = hit;

        addData.board_short_name = board_short_name;
        addData.article_hash_tags = article_hash_tags;
        addData.comment_nicknames = comment_nicknames;

        addData.hash_count = article_hash_tags.size()/2;

        board_list_data.add(addData);
    }


    /*public void addItem(String title, String writer, String notice, String date, int comment, boolean marked, boolean file){
        BoardData addData = new BoardData();

        addData.board_title = title;
        addData.board_writer = writer;
        addData.board_notice = notice;
        addData.board_date = date;
        addData.board_comment_num = comment;
        addData.board_marked = marked;
        addData.board_file = file;

        board_list_data.add(addData);
    }*/

    public class ViewHolder{
        public TextView text_board_title;
        public TextView text_board_comment_num;
        public TextView text_board_date;
        public TextView text_board_notice;
        public TextView text_board_writer;
        public ToggleButton btn_board_mark;
        public ImageView img_board_file;
    }
}

