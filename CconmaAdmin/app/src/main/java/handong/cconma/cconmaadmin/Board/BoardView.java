package handong.cconma.cconmaadmin.Board;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import handong.cconma.cconmaadmin.R;

/**
 * 게시판 목록에서 하나 선택하여 글 내용을 보여주는 화면
 * Created by eundi on 15. 7. 6..
 */
public class BoardView extends Activity{

    TextView text_board_view_title;
    TextView text_board_view_notice;
    TextView text_board_view_writer;
    TextView text_board_view_date;
    CheckBox check_board_view_complete;

    TextView text_board_view_content;
    Button btn_board_view_modify;
    Button btn_board_view_delete;

    ListView list_board_view_comment;
    //boolean lastitemVisibleFlag = false;
    BoardCommentAdapter adapter_comment;

    LinearLayout layout_board_view_comment;
    EditText edit_board_view_comment;
    Button btn_board_view_comment;

    InputMethodManager input_manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);

        View header =getLayoutInflater().inflate(R.layout.board_list_header, null, false);

        list_board_view_comment = (ListView)findViewById(R.id.list_board_view_comment);
        list_board_view_comment.addHeaderView(header);
        adapter_comment = new BoardCommentAdapter(this);
        list_board_view_comment.setAdapter(adapter_comment);
        //list_board_view_comment.setOnScrollListener(scrollListener);

        layout_board_view_comment = (LinearLayout)findViewById(R.id.layout_board_view_comment);
        edit_board_view_comment = (EditText)findViewById(R.id.edit_board_view_comment);
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

    }

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
                            adapter_comment.addItem("김은지", strNow, edit_board_view_comment.getText().toString());
                        }
                        edit_board_view_comment.setText("");
                        input_manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        input_manager.hideSoftInputFromWindow(edit_board_view_comment.getWindowToken(), 0);

                    }
                    break;
                case R.id.btn_board_view_modify:
                    dialog(0);
                    break;
                case R.id.btn_board_view_delete:
                    dialog(1);
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

    public void dialog(final int index){
        String alert_message = "";
        AlertDialog.Builder alert_build = new AlertDialog.Builder(this);
        switch(index){
            case 0:
                alert_message = "게시글을 수정하시겠습니까?";
                break;
            case 1:
                alert_message = "게시글을 삭제하시겠습니까?";
                break;
        }

        alert_build.setMessage(alert_message).setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //수정하거나 삭제하는 코드.
                        if(index == 0){
                            Intent intent = new Intent(BoardView.this, BoardModify.class);
                            intent.putExtra("number", "7128");
                            startActivity(intent);
                        }else{
                            //게시글 삭제하는 코드.
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
                holder.btn_board_view_comment_menu = (ImageButton)convertView.findViewById(R.id.btn_board_view_comment_menu);

                holder.layout_board_view_comment_menu = (LinearLayout)convertView.findViewById(R.id.layout_board_view_comment_menu);
                holder.btn_board_view_comment_modify = (Button)convertView.findViewById(R.id.btn_board_view_comment_modify);
                holder.btn_board_view_comment_delete = (Button)convertView.findViewById(R.id.btn_board_view_comment_delete);

                holder.layout_board_view_comment_modify = (LinearLayout)convertView.findViewById(R.id.layout_board_view_comment_modify);
                holder.btn_board_view_comment_complete = (Button)convertView.findViewById(R.id.btn_board_view_comment_complete);
                holder.edit_board_view_comment_modify = (EditText)convertView.findViewById(R.id.edit_board_view_comment_modify);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            BoardCommentData data = board_comment_list_data.get(position);
            holder.text_board_view_comment_writer.setText(data.commnet_writer);
            holder.text_board_view_comment.setText(data.comment);
            holder.text_board_view_comment_date.setText(data.comment_date);
            holder.btn_board_view_comment_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layout_board_view_comment_menu.setVisibility(View.VISIBLE);
                }
            });
            holder.btn_board_view_comment_modify.setTag(position);
            holder.btn_board_view_comment_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layout_board_view_comment_menu.setVisibility(View.GONE);
                    dialog(0, (Integer) v.getTag(), holder);
                }
            });
            holder.btn_board_view_comment_delete.setTag(position);
            holder.btn_board_view_comment_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layout_board_view_comment_menu.setVisibility(View.GONE);
                    dialog(1, (Integer) v.getTag(), holder);
                }
            });
            holder.btn_board_view_comment_complete.setTag(position);
            holder.btn_board_view_comment_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    board_comment_list_data.get((Integer) v.getTag()).setComment(edit_board_view_comment.getText().toString());
                    holder.text_board_view_comment.setText(board_comment_list_data.get((Integer) v.getTag()).comment);
                    adapter_comment.notifyDataSetChanged();
                    edit_board_view_comment.setText("");
                    //holder.layout_board_view_comment_modify.setVisibility(View.GONE);
                    //input_manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //input_manager.hideSoftInputFromWindow(holder.edit_board_view_comment_modify.getWindowToken(), 0);
                    //layout_board_view_comment.setVisibility(View.VISIBLE);
                }
            });

            return convertView;
        }

        public void addItem(String comment_writer, String comment_date, String comment){
            BoardCommentData addData = new BoardCommentData();

            addData.comment = comment;
            addData.comment_date = comment_date;
            addData.commnet_writer = comment_writer;

            board_comment_list_data.add(addData);
        }

        public void updateComment(int position, String content){
            board_comment_list_data.get(position).setComment(content);
        }

        public class ViewHolder{
            public TextView text_board_view_comment_writer;
            public TextView text_board_view_comment;
            public TextView text_board_view_comment_date;
            public ImageButton btn_board_view_comment_menu;

            public Button btn_board_view_comment_modify;
            public Button btn_board_view_comment_delete;
            public LinearLayout layout_board_view_comment_menu;

            public Button btn_board_view_comment_complete;
            public EditText edit_board_view_comment_modify;
            public LinearLayout layout_board_view_comment_modify;
        }

        public void dialog(final int index, final int position, final ViewHolder holder){
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
                                //holder.layout_board_view_comment_modify.setVisibility(View.VISIBLE);
                                //holder.edit_board_view_comment_modify.setText((board_comment_list_data.get(position).comment).toString());
                                //layout_board_view_comment.setVisibility(View.GONE);
                                edit_board_view_comment.setText((board_comment_list_data.get(position).comment).toString());
                                edit_board_view_comment.setTag(position);
                                //커서 위치 문자열 뒤쪽에 위치하도록.
                                Editable edt = edit_board_view_comment.getText();
                                Selection.setSelection(edt, edt.length());

                                /*input_manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                input_manager.showSoftInput(edit_board_view_comment, 0);
                                list_board_view_comment.setSelection(position);*/
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


}
