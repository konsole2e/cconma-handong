package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;
import java.util.StringTokenizer;

import handong.cconma.cconmaadmin.R;

/**
 * Created by Young Bin Kim on 2015-07-27.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder> {
    private List<BoardData> dataItemList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_board_title;
        public TextView text_board_comment_num;
        public TextView text_board_date;
        public TextView text_board_writer;
        public ToggleButton btn_board_mark;
        public ImageView img_board_file;

        public TextView text_notice1;
        public TextView text_notice2;
        public TextView text_notice3;
        public TextView text_notice4;
        public TextView text_notice5;
        public TextView text_notice6;

        public ViewHolder(View itemView) {
            super(itemView);
            text_board_title = (TextView)itemView.findViewById(R.id.text_board_title);
            text_board_comment_num = (TextView)itemView.findViewById(R.id.text_board_comment_num);
            text_board_date = (TextView)itemView.findViewById(R.id.text_board_date);
            text_board_writer = (TextView)itemView.findViewById(R.id.text_board_writer);
            btn_board_mark = (ToggleButton)itemView.findViewById(R.id.btn_board_mark);
            img_board_file = (ImageView)itemView.findViewById(R.id.img_board_file);

            text_notice1 = (TextView)itemView.findViewById(R.id.text_notice1);
            text_notice2 = (TextView)itemView.findViewById(R.id.text_notice2);
            text_notice3 = (TextView)itemView.findViewById(R.id.text_notice3);
            text_notice4 = (TextView)itemView.findViewById(R.id.text_notice4);
            text_notice5 = (TextView)itemView.findViewById(R.id.text_notice5);
            text_notice6 = (TextView)itemView.findViewById(R.id.text_notice6);
        }
    }

    public BoardRecyclerAdapter(List<BoardData> dataItemList, Context context) {
        this.dataItemList = dataItemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //View view = inflater.inflate(R.layout.board_item, viewGroup, false);

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.board_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.d("debugging", "onBindViewHolder int i: " + String.valueOf(i));
        BoardData dataItem = dataItemList.get(i);

        viewHolder.text_board_title.setText(dataItem.subject);
        viewHolder.text_board_writer.setText(dataItem.name);

        if(dataItem.comment_count != 0) {
            viewHolder.text_board_comment_num.setVisibility(View.VISIBLE);
            viewHolder.text_board_comment_num.setText(dataItem.comment_nicknames);
        }

        StringTokenizer st = new StringTokenizer(dataItem.reg_date, "-:");
        int count = 0;
        String date = "";
        while(st.hasMoreTokens()){
            String stDate = st.nextToken();
            if(count == 1){
                date = date + stDate;
            }else if(count ==2){
                date = date + "/" +stDate;
            }else if(count == 3){
                date = date+ ":" + stDate;
                break;
            }
            count++;
        }
        viewHolder.text_board_date.setText(date);

        if(dataItem.hashMap.size() != 0) {
            setNotice(viewHolder, dataItem);
        }

        viewHolder.btn_board_mark.setChecked(dataItem.board_marked);
        //holder.btn_board_mark.setTag(position);
        viewHolder.btn_board_mark.setClickable(false);
        /*holder.btn_board_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_board_mark.isChecked()) {
                    Toast.makeText(context, "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                    board_list_data.get((Integer) v.getTag()).board_marked = true;
                } else {

                    Toast.makeText(context, "즐겨찾기 해제", Toast.LENGTH_SHORT).show();
                    board_list_data.get((Integer) v.getTag()).board_marked = false;
                }
            }
        });*/

        if(dataItem.board_file)
            viewHolder.img_board_file.setVisibility(View.VISIBLE);
        else
            viewHolder.img_board_file.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        Log.d("debugging", "dataItemList size: " + String.valueOf(dataItemList.size()));
        return dataItemList.size();
        //return (null != dataItemList ? dataItemList.size() : 0);
    }

    public void setNotice(ViewHolder viewHolder, BoardData dataItem){
        for(int j=0; j<dataItem.hashMap.size()/2; j++){
            switch (j){
                case 0:
                    color(viewHolder.text_notice1, dataItem, j);
                    break;
                case 1:
                    color(viewHolder.text_notice2, dataItem, j);
                    break;
                case 2:
                    color(viewHolder.text_notice3, dataItem, j);
                    break;
                case 3:
                    color(viewHolder.text_notice4, dataItem, j);
                    break;
                case 4:
                    color(viewHolder.text_notice5, dataItem, j);
                    break;
                case 5:
                    color(viewHolder.text_notice6, dataItem, j);
                    break;
            }
        }
    }

    public void color(TextView textView, BoardData boardData, int n){
        String hash_tag_type = boardData.hashMap.get("hash_tag_type"+n).toString();
        textView.setText(" "+boardData.hashMap.get("hash_tag" + n).toString()+" ");
        textView.setVisibility(View.VISIBLE);

        Resources res = context.getResources();
        if(hash_tag_type.equals("notice_myteam")) {
            Drawable d = res.getDrawable(R.drawable.notice_myteam);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(255, 255, 255));
        }
        else if(hash_tag_type.equals("notice_team")) {
            Drawable d = res.getDrawable(R.drawable.notice_team);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(42, 117, 0));
        }
        else if(hash_tag_type.equals("notice_member")) {
            Drawable d = res.getDrawable(R.drawable.notice_member);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(59, 89, 152));
        }else if(hash_tag_type.equals("notice_me")){
            Drawable d = res.getDrawable(R.drawable.notice_me);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(255, 255, 255));
        }
    }
}
