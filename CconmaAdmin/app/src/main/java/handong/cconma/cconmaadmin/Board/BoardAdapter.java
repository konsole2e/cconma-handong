package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //Context convertContext = null;
        //if(convertView == null){
        holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.board_list_item, null);
        //convertContext = convertView.getContext();
        //holder.layout_board_list_item_notice = (LinearLayout)convertView.findViewById(R.id.layout_board_list_item_notice);
        holder.text_board_title = (TextView)convertView.findViewById(R.id.text_board_title);
        holder.text_board_comment_num = (TextView)convertView.findViewById(R.id.text_board_comment_num);
        holder.text_board_date = (TextView)convertView.findViewById(R.id.text_board_date);
        holder.text_board_writer = (TextView)convertView.findViewById(R.id.text_board_writer);
        holder.btn_board_mark = (ToggleButton)convertView.findViewById(R.id.btn_board_mark);
        holder.btn_board_mark.setFocusable(false);
        holder.img_board_file = (ImageView)convertView.findViewById(R.id.img_board_file);

        holder.text_notice1 = (TextView)convertView.findViewById(R.id.text_notice1);
        holder.text_notice2 = (TextView)convertView.findViewById(R.id.text_notice2);
        holder.text_notice3 = (TextView)convertView.findViewById(R.id.text_notice3);
        holder.text_notice4 = (TextView)convertView.findViewById(R.id.text_notice4);
        holder.text_notice5 = (TextView)convertView.findViewById(R.id.text_notice5);
        holder.text_notice6 = (TextView)convertView.findViewById(R.id.text_notice6);

        holder.layout_for_mark = (LinearLayout)convertView.findViewById(R.id.layout_for_mark);

        convertView.setTag(holder);
        //}else{
        //    holder = (ViewHolder)convertView.getTag();
        //}

        BoardData data = board_list_data.get(position);

        holder.text_board_title.setText(data.subject);
        String date = "";
        if(data.comment_count != 0) {
            holder.text_board_comment_num.setVisibility(View.VISIBLE);
            holder.text_board_comment_num.setText(data.comment_nicknames);
        }
            //holder.text_board_comment_num.setText("+" + data.comment_count);

        StringTokenizer st = new StringTokenizer(data.reg_date, "-:");
        int count=0;
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
        holder.text_board_date.setText(date);



        //Log.d("list", "position = " + position + "    ::    notice = "+data.notice);
        if(data.hashMap.size() != 0) {
            for(int i=0; i<data.hashMap.size()/2; i++){
                switch (i){
                    case 0:
                        color(holder.text_notice1, data, i);
                        break;
                    case 1:
                        color(holder.text_notice2, data, i);
                        break;
                    case 2:
                        color(holder.text_notice3, data, i);
                        break;
                    case 3:
                        color(holder.text_notice4, data, i);
                        break;
                    case 4:
                        color(holder.text_notice5, data, i);
                        break;
                    case 5:
                        color(holder.text_notice6, data, i);
                        break;
                }
            }
        }


        holder.text_board_writer.setText(data.name);

        holder.btn_board_mark.setChecked(data.board_marked);
        //holder.btn_board_mark.setTag(position);
        holder.btn_board_mark.setClickable(false);
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


        holder.layout_for_mark.setTag(position);
        holder.layout_for_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_board_mark.isChecked()){
                    Toast.makeText(context, "즐겨찾기 해제", Toast.LENGTH_SHORT).show();
                    holder.btn_board_mark.setChecked(false);
                    board_list_data.get((Integer)v.getTag()).board_marked = false;
                }else{
                    Toast.makeText(context, "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                    holder.btn_board_mark.setChecked(true);
                    board_list_data.get((Integer)v.getTag()).board_marked = true;

                }
            }
        });




        if(data.board_file)
            holder.img_board_file.setVisibility(View.VISIBLE);
        else
            holder.img_board_file.setVisibility(View.GONE);


        Pattern pattern = Pattern.compile("[완료]");
        Matcher matcher = pattern.matcher(data.subject);
        if(matcher.find()){
            holder.text_board_title.setTextColor(Color.LTGRAY);
        }

        return convertView;
    }

    //파싱한 데이터 저장하기
    public void addItem(String notice_type, String board_no, String boardarticle_no, String name,
                        String subject, String mem_no, String reg_date, String ip, String hit,
                        String board_short_name, HashMap hashMap, String comment_nicknames){

        BoardData addData = new BoardData();

        addData.notice_type = notice_type;
        addData.board_no = board_no;
        addData.boardarticle_no = boardarticle_no;
        addData.name = name;

        addData.subject = subject;
        addData.mem_no = mem_no;
        addData.reg_date = reg_date;
        addData.ip = ip;
        addData.hit = hit;

        addData.board_short_name = board_short_name;
        addData.hashMap = hashMap;


        addData.comment_nicknames = comment_nicknames;

        Pattern pattern = Pattern.compile("\\(");
        Matcher matcher = pattern.matcher(comment_nicknames);
        while(matcher.find()){
            addData.comment_count++;
        }

        board_list_data.add(addData);
    }

    public class ViewHolder{
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

        public LinearLayout layout_for_mark;

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

