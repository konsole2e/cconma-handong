package handong.cconma.cconmaadmin.push;

import handong.cconma.cconmaadmin.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.push_list_item, parent, false);
        holder.text_push_message = (TextView)convertView.findViewById(R.id.text_push_message);
        holder.text_push_date = (TextView)convertView.findViewById(R.id.text_push_date);

        convertView.setTag(holder);


        String[] typeStr = push_list_data.get(position).board_title[0].split("]");

        String pushStr = "<b>"+ typeStr[0] + "]</b>" + typeStr[1]
                + "'<font color=#dd6600>" + push_list_data.get(position).board_title[1] + "</font>'"
                + push_list_data.get(position).board_title[2]
                + "<font color=#0055dd> " + push_list_data.get(position).push_content + "</font>";
        holder.text_push_message.setText(Html.fromHtml(pushStr));
        holder.text_push_date.setText(push_list_data.get(position).pushed_datetime);

        return convertView;
    }


    public void addItem(String id, String event_name, String receiver_mem_no, String title,
                        String message, String url, String pushed_datetime){
        PushData addData = new PushData();

        addData.id = id;
        addData.event_name = event_name;
        addData.receiver_mem_no = receiver_mem_no;
        addData.title = title;
        addData.message = message;
        addData.url = url;
        addData.pushed_datetime = pushed_datetime;

        String[] messageSplit = message.split("\n\n");
        addData.push_title = messageSplit[0];
        addData.push_content = messageSplit[1];

        String[] titleSplit = addData.push_title.split("'");
        addData.board_title = titleSplit;

        int point = url.indexOf('&');
        String board_info  = url.substring(point+1);
        String[] boardSplit = board_info.split("&");

        addData.board_no = boardSplit[0].split("=")[1];
        addData.boardarticle_no = boardSplit[1].split("=")[1];

        push_list_data.add(addData);
    }

    public class ViewHolder{

        TextView text_push_message;
        TextView text_push_date;
    }


}
