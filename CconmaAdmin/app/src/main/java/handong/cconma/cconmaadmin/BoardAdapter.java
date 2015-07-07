package handong.cconma.cconmaadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.util.ArrayList;

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
        ViewHolder holder;
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

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        BoardData data = board_list_data.get(position);
        holder.text_board_title.setText(data.board_title);
        holder.text_board_comment_num.setText("+" + data.board_comment_num);
        holder.text_board_date.setText(data.board_date);
        holder.text_board_notice.setText(data.board_notice);
        holder.text_board_writer.setText(data.board_writer);
        holder.btn_board_mark.setChecked(data.board_marked);

        return convertView;
    }

    public void addItem(String title, String writer, String notice, String date, int comment, boolean marked){
        BoardData addData = new BoardData();

        addData.board_title = title;
        addData.board_writer = writer;
        addData.board_notice = notice;
        addData.board_date = date;
        addData.board_comment_num = comment;
        addData.board_marked = marked;

        board_list_data.add(addData);
    }

    public class ViewHolder{
        public TextView text_board_title;
        public TextView text_board_comment_num;
        public TextView text_board_date;
        public TextView text_board_notice;
        public TextView text_board_writer;
        public ToggleButton btn_board_mark;

    }


}
