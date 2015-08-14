package handong.cconma.cconmaadmin.board;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-27.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder> {
    private static final int VIEW_MAIN  = 0;
    private static final int VIEW_FOOTER = 1;

    private List<BoardData> dataItemList;
    private Context context;
    private double width_notice;
    int sum_of_width_notice;
    int markpos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_board_title;
        public TextView text_board_comment_num;
        public TextView text_board_date;
        public TextView text_board_writer;
        public ToggleButton btn_board_mark;

        public LinearLayout layout_notice;
        public int holderId;

        public ViewHolder(View itemView, int i) {
            super(itemView);
            if( i == VIEW_MAIN ) {
                text_board_title = (TextView) itemView.findViewById(R.id.text_board_title);
                text_board_comment_num = (TextView) itemView.findViewById(R.id.text_board_comment_num);
                text_board_date = (TextView) itemView.findViewById(R.id.text_board_date);
                text_board_writer = (TextView) itemView.findViewById(R.id.text_board_writer);
                btn_board_mark = (ToggleButton) itemView.findViewById(R.id.btn_board_mark);
                btn_board_mark.setFocusable(false);
                layout_notice = (LinearLayout) itemView.findViewById(R.id.layout_notice);
                holderId = 0;
            }
            else{
                holderId = 1;
            }
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
        if( i == VIEW_MAIN ) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.board_item, viewGroup, false);

            return new ViewHolder(view, i);
        }
        else{
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.board_footer, viewGroup, false);

            return new ViewHolder(view, i);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
<<<<<<< HEAD
        if( dataItemList != null ){
            final BoardData dataItem = dataItemList.get(i);

            if(dataItem.boardAll) {
                viewHolder.text_board_title.setText(Html.fromHtml("<b>"+"[" + dataItem.board_short_name + "]</b> " + dataItem.subject));
            }
            else {
                viewHolder.text_board_title.setText(dataItem.subject);
            }
=======
        if( viewHolder.holderId == 0 ) {
            if (dataItemList != null) {
                final BoardData dataItem = dataItemList.get(i);

                if (dataItem.boardAll) {
                    viewHolder.text_board_title.setText(Html.fromHtml("<b>" + "[" + dataItem.board_short_name + "]</b> " + dataItem.subject));
                    Log.d("board", dataItem.board_short_name);
                } else {
                    viewHolder.text_board_title.setText(dataItem.subject);
                    Log.d("board", dataItem.board_short_name + "else");
                }
>>>>>>> ae326c7cefb44263212f99e285bfdeae74033261

                viewHolder.text_board_title.setAlpha(1.0f);
                viewHolder.text_board_title.setTextColor(Color.BLACK);
                viewHolder.text_board_writer.setAlpha(1.0f);

                viewHolder.text_board_title.setTextColor(Color.BLACK);
                Pattern pattern = Pattern.compile("\\[완료\\]");
                Matcher matcher = pattern.matcher(dataItem.subject);
                if (matcher.find()) {
                    viewHolder.text_board_title.setTextColor(context.getResources().getColor(R.color.completed));
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
                }

                viewHolder.btn_board_mark.setChecked(dataItem.board_marked);
                viewHolder.btn_board_mark.setTag(i);
                viewHolder.btn_board_mark.setClickable(false);
                viewHolder.btn_board_mark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markpos = i;
                        new MarkAsync().execute();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
//        Log.d("debugging", "dataItemList size: " + String.valueOf(dataItemList.size()));
        return dataItemList.size();
        //return (null != dataItemList ? dataItemList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if ( position == dataItemList.size() - 1 )
            return VIEW_FOOTER;

        return VIEW_MAIN;
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

    class MarkAsync extends AsyncTask<String, Void, String>{

        BasicData basicData = BasicData.getInstance();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(dataItemList.get(markpos).board_marked){
                Toast.makeText(context, "즐겨찾기 해제", Toast.LENGTH_SHORT).show();
                dataItemList.get(markpos).board_marked = false;
            }else{
                Toast.makeText(context, "즐겨찾기 추가", Toast.LENGTH_SHORT).show();
                dataItemList.get(markpos).board_marked = true;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                    + dataItemList.get(markpos).board_no
                    + "/articles/" + dataItemList.get(markpos).boardarticle_no
                    + "/scraped_members/" + basicData.getMem_no()
                    , "PUT", "");
            connection.init();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
