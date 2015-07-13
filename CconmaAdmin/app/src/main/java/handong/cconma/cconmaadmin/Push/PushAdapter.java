package handong.cconma.cconmaadmin.push;

import handong.cconma.cconmaadmin.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by eundi on 15. 7. 9..
 */
public class PushAdapter extends BaseAdapter {
    public Context context = null;
    public ArrayList<PushData> push_list_data = new ArrayList<PushData>();

    public PushAdapter(Context context){
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return push_list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return push_list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position){
        return push_list_data.get(position).type;
    }

    public int getViewTypeCount(){
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int res = 0;
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.push_list_item, parent, false);
            holder.img_push_category = (ImageView)convertView.findViewById(R.id.img_push_category);
            holder.text_push_board = (TextView)convertView.findViewById(R.id.text_push_board);
            holder.text_push_board_date = (TextView)convertView.findViewById(R.id.text_push_board_date);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        PushData data = push_list_data.get(position);
        res = getItemViewType(position);
        switch (res){
            case 0:
                holder.img_push_category.setBackgroundColor(Color.RED);
                holder.text_push_board.setText(data.writer + "님이 "
                + data.notice + "'" + data.title + "' 게시글에 '"
                + data.content + "' 댓글을 달았습니다.");
                holder.text_push_board_date.setText(data.date);
                break;
            case 1:
                holder.img_push_category.setBackgroundColor(Color.GREEN);
                holder.text_push_board.setText(data.product + "의 남은 수량이 " + data.stock + "개 입니다.");
                break;
            case 2:
                holder.img_push_category.setBackgroundColor(Color.BLUE);
                holder.text_push_board.setText(""+data.type);
                break;
        }

        return convertView;
    }

    public void addBoardItem(int type, String writer, String notice, String title, String content, String date){
        PushData addData = new PushData();

        addData.type = type;
        addData.writer = writer;
        addData.notice = notice;
        addData.title = title;
        addData.content = content;
        addData.date = date;

        push_list_data.add(addData);
    }

    public void addStockItem(int type, String product, int stock){
        PushData addData = new PushData();

        addData.type = type;
        addData.product = product;
        addData.stock = stock;

        push_list_data.add(addData);
    }

    public void addItem(int type){
        PushData addData = new PushData();

        addData.type = type;

        push_list_data.add(addData);
    }

    public class ViewHolder{
        public ImageView img_push_category;
        public TextView text_push_board;
        public TextView text_push_board_date;
    }


}
