package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;

/**
 * Created by Young Bin Kim on 2015-07-27.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder> {
    private List<BoardData> dataItemList;
    private Context context;
    private double width_notice;
    int sum_of_width_notice;
    public View Snackbar;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_board_title;
        public TextView text_board_comment_num;
        public TextView text_board_date;
        public TextView text_board_writer;
        public ToggleButton btn_board_mark;
        public ImageView img_board_file;

        public LinearLayout layout_notice;


        public ViewHolder(View itemView) {
            super(itemView);
            text_board_title = (TextView)itemView.findViewById(R.id.text_board_title);
            text_board_comment_num = (TextView)itemView.findViewById(R.id.text_board_comment_num);
            text_board_date = (TextView)itemView.findViewById(R.id.text_board_date);
            text_board_writer = (TextView)itemView.findViewById(R.id.text_board_writer);
            btn_board_mark = (ToggleButton)itemView.findViewById(R.id.btn_board_mark);
            btn_board_mark.setFocusable(false);
            layout_notice = (LinearLayout)itemView.findViewById(R.id.layout_notice);
        }
    }

    public BoardRecyclerAdapter(List<BoardData> dataItemList, Context context) {
        this.dataItemList = dataItemList;
        this.context = context;
        Display dis = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width_notice = dis.getWidth()*0.6;
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
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        if( dataItemList == null ){

        }
        else {
            final BoardData dataItem = dataItemList.get(i);

            if(dataItem.boardAll) {
                viewHolder.text_board_title.setText(Html.fromHtml("<b>"+"[" + dataItem.board_short_name + "]</b> " + dataItem.subject));
                Log.d("board", dataItem.board_short_name);
            }
            else {
                viewHolder.text_board_title.setText(dataItem.subject);
                Log.d("board", dataItem.board_short_name + "else");
            }

            viewHolder.text_board_title.setAlpha(1.0f);
            viewHolder.text_board_title.setTextColor(Color.BLACK);
            viewHolder.text_board_writer.setAlpha(1.0f);

            viewHolder.text_board_title.setTextColor(Color.BLACK);
            Pattern pattern = Pattern.compile("\\[완료\\]");
            Matcher matcher = pattern.matcher(dataItem.subject);
            if (matcher.find()) {
                viewHolder.text_board_title.setTextColor(Color.LTGRAY);
            }
            viewHolder.text_board_writer.setText(dataItem.name);

            viewHolder.text_board_comment_num.setVisibility(View.GONE);

            if (dataItem.comment_count != 0) {
                viewHolder.text_board_comment_num.setVisibility(View.VISIBLE);
                viewHolder.text_board_comment_num.setText(dataItem.comment_nicknames);
            }

            StringTokenizer st = new StringTokenizer(dataItem.reg_date, "-:");
            int count = 0;
            String date = "";
            while (st.hasMoreTokens()) {
                String stDate = st.nextToken();
                if (count == 1) {
                    date = date + stDate;
                } else if (count == 2) {
                    date = date + "/" + stDate;
                } else if (count == 3) {
                    date = date + ":" + stDate;
                    break;
                }
                count++;
            }
            viewHolder.text_board_date.setText(date);
            viewHolder.text_board_date.setAlpha(1.0f);

            viewHolder.layout_notice.removeAllViews();
            if (dataItem.hashMap.size() != 0) {
                sum_of_width_notice = 0;
                int addingCount = 0;
                int layout_num = 0;
                LinearLayout l1 = new LinearLayout(context);
                LinearLayout l2 = new LinearLayout(context);
                LinearLayout l3 = new LinearLayout(context);
                for (int j = 0; j < dataItem.hashMap.size() / 2; j++) {
                    TextView textView = new TextView(context);
                    color(textView, dataItem, j);

                    textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    sum_of_width_notice = sum_of_width_notice + textView.getMeasuredWidth();

                    if (sum_of_width_notice > width_notice) {
                        addingCount = 0;
                        layout_num++;
                        sum_of_width_notice = textView.getMeasuredWidth() + 5;
                    }

                    if (layout_num == 0 && addingCount == 0) {
                        viewHolder.layout_notice.addView(l1);
                    } else if (layout_num == 1 && addingCount == 0) {
                        viewHolder.layout_notice.addView(l2);
                    } else if (layout_num == 2 && addingCount == 0) {
                        viewHolder.layout_notice.addView(l3);
                    }

                    switch (layout_num) {
                        case 0:
                            l1.addView(textView);
                            break;
                        case 1:
                            l2.addView(textView);
                            break;
                        case 2:
                            l3.addView(textView);
                            break;
                    }
                    addingCount++;
                }

          /*  Log.d("whywhywhy", dataItem.hashMap.toString());
            TextView[] notice = new TextView[dataItem.hashMap.size()];
            for(int j = 0; j < dataItem.hashMap.size(); j++) {
                notice[j] = new TextView(context);
                notice[j].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                notice[j].setText("마을지기" + j);
                viewHolder.layout_notice.addView(notice[j]);
            }*/
            }

            viewHolder.btn_board_mark.setChecked(dataItem.board_marked);
            viewHolder.btn_board_mark.setTag(i);
            viewHolder.btn_board_mark.setClickable(false);
            viewHolder.btn_board_mark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BasicData basicData = BasicData.getInstance();
                    try {
                        JSONObject json = new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                                + dataItem.board_no
                                + "/articles/" + dataItem.boardarticle_no
                                + "/scraped_members/" + basicData.getMem_no()
                                , "PUT", "").execute().get();
                        Log.d("scrap", json.get("status").toString());
                    } catch (Exception e) {

                    }

                    if (viewHolder.btn_board_mark.isChecked()) {
                        Toast.makeText(context, "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                        dataItemList.get((Integer) v.getTag()).board_marked = true;
                    } else {

                        Toast.makeText(context, "즐겨찾기 해제", Toast.LENGTH_SHORT).show();
                        dataItemList.get((Integer) v.getTag()).board_marked = false;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
//        Log.d("debugging", "dataItemList size: " + String.valueOf(dataItemList.size()));
        return dataItemList.size();
        //return (null != dataItemList ? dataItemList.size() : 0);
    }

    public void color(TextView textView, BoardData boardData, int n){
        String hash_tag_type = boardData.hashMap.get("hash_tag_type"+n).toString();
        textView.setText(" "+boardData.hashMap.get("hash_tag" + n).toString()+" ");

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 5, 2); // llp.setMargins(left, top, right, bottom);
        textView.setLayoutParams(llp);
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
