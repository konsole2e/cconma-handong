package handong.cconma.cconmaadmin.board;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;

/**
 * 게시판 목록에서 하나 선택하여 글 내용을 보여주는 화면
 * Created by eundi on 15. 7. 6..
 */
public class BoardViewActivity extends AppCompatActivity{
    private Toolbar toolbar;

    TextView text_board_view_title;
    TextView text_board_view_notice;
    TextView text_board_view_writer;
    TextView text_board_view_date;
    CheckBox check_board_view_complete;

    TextView text_board_view_content;
    Button btn_board_view_modify;
    Button btn_board_view_delete;

    ListView list_board_view_comment;
    BoardCommentAdapter adapter_comment;

    LinearLayout layout_board_view_comment;
    EditText edit_board_view_comment;
    Button btn_board_view_comment;

    InputMethodManager input_manager;

    String boardarticle_no;
    String board_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);

        board_no = this.getIntent().getStringExtra("board_no");
        boardarticle_no = this.getIntent().getStringExtra("boardarticle_no");

        View header = getLayoutInflater().inflate(R.layout.board_list_header, null, false);

        header.setLongClickable(false);
        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        list_board_view_comment = (ListView)findViewById(R.id.list_board_view_comment);
        list_board_view_comment.addHeaderView(header);
        list_board_view_comment.setHeaderDividersEnabled(false);
        adapter_comment = new BoardCommentAdapter(this);
        list_board_view_comment.setAdapter(adapter_comment);
        //list_board_view_comment.setOnScrollListener(scrollListener);
        list_board_view_comment.setOnItemLongClickListener(itemClickListner);

        list_board_view_comment.setFocusable(false);

        layout_board_view_comment = (LinearLayout)findViewById(R.id.layout_board_view_comment);
        edit_board_view_comment = (EditText)findViewById(R.id.edit_board_view_comment);
        edit_board_view_comment.setOnClickListener(clickListener);
        btn_board_view_comment = (Button)findViewById(R.id.btn_board_view_comment);
        btn_board_view_comment.setOnClickListener(clickListener);


        text_board_view_title = (TextView)header.findViewById(R.id.text_board_view_title);
        text_board_view_notice = (TextView)header.findViewById(R.id.text_board_view_notice);
        text_board_view_writer = (TextView)header.findViewById(R.id.text_board_view_writer);
        text_board_view_date = (TextView)header.findViewById(R.id.text_board_view_date);
        check_board_view_complete = (CheckBox)header.findViewById(R.id.check_board_view_complete);
        check_board_view_complete.setOnClickListener(clickListener);

        text_board_view_content = (TextView)header.findViewById(R.id.text_board_view_content);
        btn_board_view_modify = (Button)header.findViewById(R.id.btn_board_view_modify);
        btn_board_view_modify.setOnClickListener(clickListener);
        btn_board_view_delete = (Button)header.findViewById(R.id.btn_board_view_delete);
        btn_board_view_delete.setOnClickListener(clickListener);


        jsonParser();
    }


    AbsListView.OnItemLongClickListener itemClickListner = new AbsListView.OnItemLongClickListener(){

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(position>0)
                dialog(2, position);
            return false;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.check_board_view_complete:
                    if(check_board_view_complete.isChecked())
                        Toast.makeText(getApplicationContext(), "해당 게시글이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "해당 게시글이 완료해제 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_board_view_comment:
                    if(!(edit_board_view_comment.getText().toString()).equals("")) {
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd\nHH:mm:ss");
                        String strNow = sdfNow.format(date);

                        if(edit_board_view_comment.getTag() != null){
                            adapter_comment.updateComment((Integer) edit_board_view_comment.getTag(),
                                    edit_board_view_comment.getText().toString());
                            adapter_comment.notifyDataSetChanged();
                            edit_board_view_comment.setTag(null);
                        }else{
                            adapter_comment.addItem("김은지", strNow, edit_board_view_comment.getText().toString(), null);
                            adapter_comment.notifyDataSetChanged();
                        }
                        edit_board_view_comment.setText("");
                        input_manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        input_manager.hideSoftInputFromWindow(edit_board_view_comment.getWindowToken(), 0);

                    }
                    break;
                case R.id.btn_board_view_modify:
                    dialog(0, 0);
                    break;
                case R.id.btn_board_view_delete:
                    dialog(1, 0);
                    break;
                case R.id.edit_board_view_comment:
                    list_board_view_comment.setSelectionFromTop(adapter_comment.getCount() - 1, 0);
                    break;
            }

        }
    };

    /*AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag)
                layout_board_view_comment.setVisibility(View.GONE);
            else
                layout_board_view_comment.setVisibility(View.VISIBLE);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        }
    };*/

    public void dialog(final int index, final int position){
        String alert_message = "";
        String pos_message = "";
        String neg_message = "";
        AlertDialog.Builder alert_build = new AlertDialog.Builder(this);
        switch(index){
            case 0:
                alert_message = "게시글을 수정하시겠습니까?";
                pos_message = "YES";
                neg_message = "NO";
                break;
            case 1:
                alert_message = "게시글을 삭제하시겠습니까?";
                pos_message = "YES";
                neg_message = "NO";
                break;
            case 2:
                alert_message = "댓글";
                pos_message = "삭제";
                neg_message = "수정";
                break;
        }

        alert_build.setMessage(alert_message).setCancelable(false)
                .setPositiveButton(pos_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //수정하거나 삭제하는 코드.
                        if (index == 0) {
                            Intent intent = new Intent(BoardViewActivity.this, BoardModifyActivity.class);
                            intent.putExtra("number", "7128");
                            startActivity(intent);
                        } else if(index == 1){
                            //게시글 삭제하는 코드.
                        } else{
                            adapter_comment.dialog(1, position - 1);
                        }

                    }
                }).setNegativeButton(neg_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (index == 2)
                    adapter_comment.dialog(0, position - 1);
                else
                    dialog.cancel();
            }
        });

        AlertDialog alert = alert_build.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    public class BoardCommentAdapter extends BaseAdapter {


        public Context context = null;
        public ArrayList<BoardCommentData> board_comment_list_data = new ArrayList<BoardCommentData>();

        public BoardCommentAdapter(Context context){
            super();
            this.context = context;
        }

        @Override
        public int getCount() {
            return board_comment_list_data.size();
        }

        @Override
        public Object getItem(int position) {
            return board_comment_list_data.get(position);
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
                convertView = inflater.inflate(R.layout.board_comment_list_item, null);

                holder.text_board_view_comment_writer = (TextView)convertView.findViewById(R.id.text_board_view_comment_writer);
                holder.text_board_view_comment = (TextView)convertView.findViewById(R.id.text_board_view_comment);
                holder.text_board_view_comment_date = (TextView)convertView.findViewById(R.id.text_board_view_comment_date);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            final BoardCommentData data = board_comment_list_data.get(position);
            holder.text_board_view_comment_writer.setText(data.commnet_writer);
            holder.text_board_view_comment_writer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentComment = edit_board_view_comment.getText().toString();

                    edit_board_view_comment.setText(currentComment + "@" + data.commnet_writer);
                    Editable edt = edit_board_view_comment.getText();
                    Selection.setSelection(edt, edt.length());
                }
            });
            String tag="";
            if(data.comment_hashMap.size() != 0){
                for(int i=0; i < data.comment_hashMap.size()/2; i++)
                {
                    String type;
                    if(data.comment_hashMap.get("hash_tag_type"+i).equals("notice_myteam"))
                        type = "<font color=\"blue\">" + data.comment_hashMap.get("hash_tag"+i) + "</font>";
                    else if(data.comment_hashMap.get("hash_tag_type"+i).equals("notice_team"))
                        type = "<font color=\"red\">" + data.comment_hashMap.get("hash_tag"+i) + "</font>";
                    else if(data.comment_hashMap.get("hash_tag_type"+i).equals("notice_member"))
                        type = "<font color=\"green\">" + data.comment_hashMap.get("hash_tag"+i) + "</font>";
                    else
                        type = data.comment_hashMap.get("hash_tag"+i).toString();

                    tag = tag + type;
                            //Html.fromHtml("<font color=\"red\">"+data.comment_hashMap.get("hash_tag" + i)+"</font>");
                }
            }

            holder.text_board_view_comment.setText(Html.fromHtml(tag + data.comment));
            holder.text_board_view_comment.setMovementMethod(LinkMovementMethod.getInstance());
            holder.text_board_view_comment.setAutoLinkMask(Linkify.WEB_URLS);
            holder.text_board_view_comment_date.setText(data.comment_date);

            convertView.setFocusable(false);
            return convertView;
        }

        public void addItem(String comment_writer, String comment_date, String comment, HashMap commentHashMap){
            BoardCommentData addData = new BoardCommentData();

            addData.comment = comment;
            addData.comment_date = comment_date;
            addData.commnet_writer = comment_writer;
            addData.comment_hashMap = commentHashMap;

            board_comment_list_data.add(addData);
        }

        public void updateComment(int position, String content){
            board_comment_list_data.get(position).setComment(content);
        }

        public class ViewHolder{
            public TextView text_board_view_comment_writer;
            public TextView text_board_view_comment;
            public TextView text_board_view_comment_date;
        }

        public void dialog(final int index, final int position){
            String alert_message = "";
            AlertDialog.Builder alert_build = new AlertDialog.Builder(context);
            switch(index){
                case 0:
                    alert_message = "댓글을 수정하시겠습니까?";
                    break;
                case 1:
                    alert_message = "댓글을 삭제하시겠습니까?";
                    break;
            }

            alert_build.setMessage(alert_message).setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //수정하거나 삭제하는 코드.
                            if(index == 0){
                                edit_board_view_comment.setText((board_comment_list_data.get(position).comment).toString());
                                edit_board_view_comment.setTag(position);
                                //커서 위치 문자열 뒤쪽에 위치하도록.
                                Editable edt = edit_board_view_comment.getText();
                                Selection.setSelection(edt, edt.length());

                                list_board_view_comment.setSelectionFromTop(position, 0);
                            }
                            else {
                                board_comment_list_data.remove(position);
                                adapter_comment.notifyDataSetChanged();
                            }
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = alert_build.create();
            alert.show();
        }
    }



    public void jsonParser(){
        try{

            JSONObject json = new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/board_no/"
                    +Integer.parseInt(board_no)+"/article_no/"
                    +Integer.parseInt(boardarticle_no), "GET", "").execute().get();

            String subject = json.getString("subject");
            String name = json.getString("name");
            String reg_date = json.getString("reg_date");
            String content = json.getString("content");

            JSONArray noticeArr = json.getJSONArray("article_hash_tags");
            String tag = "";
            for(int k=0; k<noticeArr.length(); k++){
                JSONObject noticeObj = noticeArr.getJSONObject(k);

                String type;
                if(noticeObj.getString("hash_tag_type").equals("notice_myteam"))
                    type = "<font color=\"blue\">" + noticeObj.getString("hash_tag") + "</font>";
                else if(noticeObj.getString("hash_tag_type").equals("notice_myteam"))
                    type = "<font color=\"blue\">" + noticeObj.getString("hash_tag") + "</font>";
                else if(noticeObj.getString("hash_tag_type").equals("notice_myteam"))
                    type = "<font color=\"blue\">" + noticeObj.getString("hash_tag") + "</font>";
                else
                    type = noticeObj.getString("hash_tag").toString();

                tag = tag + type;
            }

            text_board_view_notice.setText(Html.fromHtml(tag));

            text_board_view_date.setText(reg_date);
            text_board_view_title.setText(subject);
            text_board_view_writer.setText(name);
            text_board_view_content.setText(Html.fromHtml(content));
            text_board_view_content.setMovementMethod(LinkMovementMethod.getInstance());


            JSONArray jsonArray = json.getJSONArray("comment_list");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject commentObj = jsonArray.getJSONObject(i);
                String comment_name = commentObj.getString("name");
                String comment_reg_date = commentObj.getString("reg_date");
                String comment_content = Html.fromHtml(commentObj.getString("content")).toString();

                JSONArray hashArr = commentObj.getJSONArray("comment_hash_tags");
                HashMap commentHashMap = new HashMap();
                if(hashArr.length()!=0) {
                    for (int j = 0; j < hashArr.length(); j++) {
                        JSONObject hashObj = hashArr.getJSONObject(j);
                        commentHashMap.put("hash_tag"+j, hashObj.getString("hash_tag"));
                        commentHashMap.put("hash_tag_type"+j, hashObj.getString("hash_tag_type"));
                    }
                }
                adapter_comment.addItem(comment_name, comment_reg_date, comment_content, commentHashMap);
            }

            adapter_comment.notifyDataSetChanged();

        }catch(Exception e){
            Log.e("JSON", Log.getStackTraceString(e));
        }

    }
}
