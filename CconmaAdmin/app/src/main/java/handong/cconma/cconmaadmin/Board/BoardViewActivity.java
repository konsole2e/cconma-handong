package handong.cconma.cconmaadmin.board;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;

/**
 * 게시판 목록에서 하나 선택하여 글 내용을 보여주는 화면
 * Created by eundi on 15. 7. 6..
 */
public class BoardViewActivity extends AppCompatActivity implements Html.ImageGetter{
    private Toolbar toolbar;
    boolean firstTime=true;
    int width_notice=0;
    TextView text_board_view_title;
    LinearLayout layout_view_notice;
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

    JSONObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);

        board_no = this.getIntent().getStringExtra("board_no");
        boardarticle_no = this.getIntent().getStringExtra("boardarticle_no");


        Display dis = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width_notice = dis.getWidth()*9/14;
        View header = getLayoutInflater().inflate(R.layout.board_list_header, null, false);

        header.setLongClickable(false);
        
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        list_board_view_comment = (ListView)findViewById(R.id.list_board_view_comment);
        list_board_view_comment.addHeaderView(header);
        list_board_view_comment.setHeaderDividersEnabled(false);
        adapter_comment = new BoardCommentAdapter(this);
        list_board_view_comment.setAdapter(adapter_comment);
        list_board_view_comment.setOnItemLongClickListener(itemClickListner);

        list_board_view_comment.setFocusable(false);

        layout_view_notice = (LinearLayout)findViewById(R.id.layout_view_notice);
        edit_board_view_comment = (EditText)findViewById(R.id.edit_board_view_comment);
        edit_board_view_comment.setOnClickListener(clickListener);
        btn_board_view_comment = (Button)findViewById(R.id.btn_board_view_comment);
        btn_board_view_comment.setOnClickListener(clickListener);

        text_board_view_title = (TextView)header.findViewById(R.id.text_board_view_title);
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
                        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd\nHH:mm");
                        String strNow = sdfNow.format(date);

                        if(edit_board_view_comment.getTag() != null){

                            try{
                                String requestBody = "_METHOD=" + "PUT"
                                        + "&board_no=" + board_no
                                        + "&boardarticle_no=" + boardarticle_no
                                        + "&comment_no=" + edit_board_view_comment.getTag()
                                        + "&content=" + edit_board_view_comment.getText().toString();
                                new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                                        +board_no+"/articles/" + boardarticle_no + "/comments/"
                                        + edit_board_view_comment.getTag(), "POST", requestBody).execute().get();
                                Log.d("test", requestBody);
                            }catch(Exception e){

                            }

                            //adapter_comment.updateComment((Integer) edit_board_view_comment.getTag(),
                                    //edit_board_view_comment.getText().toString());
                            //adapter_comment.notifyDataSetChanged();
                            edit_board_view_comment.setTag(null);
                            adapter_comment.board_comment_list_data.clear();
                            jsonParser();

                        }else{
                            //HashMap hash = new HashMap();
                            //adapter_comment.addItem("김은지", strNow, edit_board_view_comment.getText().toString(), hash);

                            String comment_no = "";

                            String requestBody = "board_no=" + board_no
                                    + "&boardarticle_no=" + boardarticle_no
                                    +"&content=" + edit_board_view_comment.getText().toString();
                            try{
                                new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                                        +board_no+"/articles/" + boardarticle_no + "/comments", "POST", requestBody).execute().get();
                            }catch(Exception e){

                            }

                            /*****************     일단은 다 지우고 다시 쓰도록 했는데  고쳐야함.*****************/
                            adapter_comment.board_comment_list_data.clear();
                            jsonParser();
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

                            try{
                                new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                                        +board_no+"/articles/" + boardarticle_no, "DELETE", "").execute().get();
                            }catch(Exception e){

                            }

                            //뒤로 돌아갔을때 변한 사항이 바로바로 반영되도록 해야한다.

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

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.ic_star_outline_grey600_48dp);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;
    }
    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Display dis;

            dis = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            try {

                if(source.substring(0, 4).equals("data")){
                    return null;
                }else{
                    Log.d("list", "WS.CCONMA");
                    InputStream is = new URL(source).openStream();
                    int scale=1;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(is, null, options);
                    if (options.outHeight > (dis.getHeight()) || options.outWidth > (dis.getWidth()*5/6)) {
                        scale = (int)Math.pow(2, (int)Math.round(Math.log((dis.getWidth()*5/6)/(double)Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
                    }
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale;
                    Log.d("list", ""+scale);
                    is.close();

                    is = new URL(source).openStream();
                    Bitmap resize = BitmapFactory.decodeStream(is, null, options);

                    return resize;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d("list", "onPostExecute drawable " + mDrawable);
            Log.d("list", "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = text_board_view_content.getText();
                text_board_view_content.setText(t);

            }else{
                Log.d("list", "bitmap is null!!!!!!!!!!!!!!!");
            }
        }
    }
    public class BoardCommentAdapter extends BaseAdapter {


        public Context context = null;
        public ArrayList<BoardCommentData> board_comment_list_data = new ArrayList<BoardCommentData>();
        int width_comment=0;
        public BoardCommentAdapter(Context context){
            super();
            this.context = context;

            Display dis = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            width_comment = dis.getWidth()*2/3;
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
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_comment_list_item, null);

            holder.text_board_view_comment_writer = (TextView)convertView.findViewById(R.id.text_board_view_comment_writer);
            holder.text_board_view_comment = (TextView)convertView.findViewById(R.id.text_board_view_comment);
            holder.text_board_view_comment_date = (TextView)convertView.findViewById(R.id.text_board_view_comment_date);
            holder.layout_comment_notice = (LinearLayout)convertView.findViewById(R.id.layout_comment_notice);

            convertView.setTag(holder);


            final BoardCommentData data = board_comment_list_data.get(position);
            holder.text_board_view_comment_writer.setText(data.commnet_writer);
            holder.text_board_view_comment_writer.setPaintFlags(holder.text_board_view_comment_writer.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);


            holder.text_board_view_comment_writer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentComment = edit_board_view_comment.getText().toString();

                    boolean isValid = true;
                    Pattern pattern = Pattern.compile("@" + holder.text_board_view_comment_writer.getText() + " ");
                    Matcher matcher = pattern.matcher(currentComment);
                    while (matcher.find()) {
                        isValid = false;
                    }
                    if (isValid) {
                        edit_board_view_comment.setText(currentComment + "@" + data.commnet_writer + " ");
                        Editable edt = edit_board_view_comment.getText();
                        Selection.setSelection(edt, edt.length());
                    }
                }
            });

            if(data.comment_hashMap.size() != 0){
                int sum_of_width_notice = 0;
                int addingCount = 0;
                int layout_num = 0;
                LinearLayout l1 = new LinearLayout(convertView.getContext());
                LinearLayout l2 = new LinearLayout(convertView.getContext());
                LinearLayout l3 = new LinearLayout(convertView.getContext());
                for(int i=0; i < data.comment_hashMap.size()/2; i++)
                {
                    String hash_tag = data.comment_hashMap.get("hash_tag"+i).toString();
                    String hash_tag_type = data.comment_hashMap.get("hash_tag_type"+i).toString();

                    TextView textView = new TextView(convertView.getContext());
                    color(textView, hash_tag, hash_tag_type);

                    textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    sum_of_width_notice = sum_of_width_notice + textView.getMeasuredWidth() + 5;

                    if(sum_of_width_notice > width_comment){
                        addingCount = 0;
                        layout_num++;
                        sum_of_width_notice = textView.getMeasuredWidth() + 5;
                    }

                    if(layout_num == 0 && addingCount == 0){
                        holder.layout_comment_notice.addView(l1);
                    }else if(layout_num == 1 && addingCount == 0){
                        holder.layout_comment_notice.addView(l2);
                    }else if(layout_num == 2 && addingCount == 0){
                        holder.layout_comment_notice.addView(l3);
                    }

                    switch(layout_num){
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
            holder.text_board_view_comment.setText(Html.fromHtml(data.comment));
            holder.text_board_view_comment.setMovementMethod(LinkMovementMethod.getInstance());
            holder.text_board_view_comment.setAutoLinkMask(Linkify.WEB_URLS);
            holder.text_board_view_comment_date.setText(data.comment_date);

            convertView.setFocusable(false);
            return convertView;
        }

        public void addItem(String boardcomment_no, String comment_writer, String comment_date, String comment, HashMap commentHashMap){
            BoardCommentData addData = new BoardCommentData();

            addData.boardcomment_no = boardcomment_no;
            addData.comment = comment;
            addData.comment_date = comment_date;
            addData.commnet_writer = comment_writer;
            addData.comment_hashMap = commentHashMap;

            board_comment_list_data.add(addData);
        }

        public class ViewHolder{
            public TextView text_board_view_comment_writer;
            public TextView text_board_view_comment;
            public TextView text_board_view_comment_date;

            public LinearLayout layout_comment_notice;
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
                                String tag="";
                                for(int i=0; i<board_comment_list_data.get(position).comment_hashMap.size()/2; i++){
                                    tag = tag + "@" + board_comment_list_data.get(position).comment_hashMap.get("hash_tag"+i) + " ";
                                }
                                edit_board_view_comment.setText(tag + (board_comment_list_data.get(position).comment).toString());
                                edit_board_view_comment.setTag(board_comment_list_data.get(position).boardcomment_no);
                                //커서 위치 문자열 뒤쪽에 위치하도록.
                                Editable edt = edit_board_view_comment.getText();
                                Selection.setSelection(edt, edt.length());

                                list_board_view_comment.setSelectionFromTop(position, 0);
                            }
                            else {

                                try{
                                    new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                                            +board_no+"/articles/" + boardarticle_no + "/comments/"
                                            + board_comment_list_data.get(position).boardcomment_no, "DELETE", "").execute().get();
                                }catch(Exception e){

                                }

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

            JSONObject json = new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                    +Integer.parseInt(board_no)+"/articles/"
                    +Integer.parseInt(boardarticle_no), "GET", "").execute().get();

            if(firstTime) {
                String subject = json.getString("subject");
                String name = json.getString("name");
                String reg_date = json.getString("reg_date");
                String content = json.getString("content");

                JSONArray noticeArr = json.getJSONArray("article_hash_tags");
                if (noticeArr.length() != 0) {
                    int sum_of_width_notice = 0;
                    int addingCount = 0;
                    int layout_num = 0;
                    LinearLayout l1 = new LinearLayout(getApplicationContext());
                    LinearLayout l2 = new LinearLayout(getApplicationContext());
                    LinearLayout l3 = new LinearLayout(getApplicationContext());
                    for (int k = 0; k < noticeArr.length(); k++) {
                        JSONObject noticeObj = noticeArr.getJSONObject(k);
                        String notice_tag = noticeObj.getString("hash_tag");
                        String notice_tag_type = noticeObj.getString("hash_tag_type");

                        TextView textView = new TextView(getApplicationContext());
                        color(textView, notice_tag, notice_tag_type);

                        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                        sum_of_width_notice = sum_of_width_notice + textView.getMeasuredWidth() + 5;

                        if (sum_of_width_notice > width_notice) {
                            addingCount = 0;
                            layout_num++;
                            sum_of_width_notice = textView.getMeasuredWidth() + 5;
                        }

                        if (layout_num == 0 && addingCount == 0) {
                            layout_view_notice.addView(l1);
                        } else if (layout_num == 1 && addingCount == 0) {
                            layout_view_notice.addView(l2);
                        } else if (layout_num == 2 && addingCount == 0) {
                            layout_view_notice.addView(l3);
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
                    firstTime = false;
                }

                text_board_view_date.setText(reg_date);
                text_board_view_title.setText(subject);
                text_board_view_title.setPaintFlags(text_board_view_title.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                text_board_view_writer.setText(name);
                text_board_view_writer.setPaintFlags(text_board_view_writer.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

                Spanned spanned = Html.fromHtml(content, this, null);
                text_board_view_content.setText(spanned);
                text_board_view_content.setMovementMethod(LinkMovementMethod.getInstance());
            }

            JSONArray jsonArray = json.getJSONArray("comments");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject commentObj = jsonArray.getJSONObject(i);
                String comment_name = commentObj.getString("name");


                String comment_reg_date = commentObj.getString("reg_date");
                StringTokenizer st = new StringTokenizer(comment_reg_date, "- :");
                int count=0;
                String date="";
                while(st.hasMoreTokens()){
                    String stDate = st.nextToken();
                    if(count == 1){
                        date = date + stDate;
                    }else if(count ==2){
                        date = date + "/" +stDate;
                    }else if(count == 3){
                        date = date+ "\n" + stDate;
                    }else if(count == 4){
                        date = date + ":" +stDate;
                        break;
                    }
                    count++;
                }
                comment_reg_date = date;

                String comment_content = Html.fromHtml(commentObj.getString("content")).toString();
                String boardcomment_no = commentObj.getString("boardcomment_no");
                JSONArray hashArr = commentObj.getJSONArray("comment_hash_tags");
                HashMap commentHashMap = new HashMap();
                if(hashArr.length()!=0) {
                    for (int j = 0; j < hashArr.length(); j++) {
                        JSONObject hashObj = hashArr.getJSONObject(j);
                        commentHashMap.put("hash_tag"+j, hashObj.getString("hash_tag"));
                        commentHashMap.put("hash_tag_type"+j, hashObj.getString("hash_tag_type"));
                    }
                }
                adapter_comment.addItem(boardcomment_no, comment_name, comment_reg_date, comment_content, commentHashMap);
            }
            adapter_comment.notifyDataSetChanged();

        }catch(Exception e){
            Log.e("JSON", Log.getStackTraceString(e));
        }

    }

    public void color(final TextView textView, String tag, String type){
        textView.setText(" " + tag + " ");

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 5, 2); // llp.setMargins(left, top, right, bottom);
        textView.setLayoutParams(llp);
        textView.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentComment = edit_board_view_comment.getText().toString();
                boolean isValid = true;
                Pattern pattern = Pattern.compile("@"+textView.getText().toString().substring(1));
                Matcher matcher = pattern.matcher(currentComment);
                while(matcher.find()){
                    isValid = false;
                }
                if(isValid){
                    edit_board_view_comment.setText(currentComment + "@" + textView.getText().toString().substring(1));
                    Editable edt = edit_board_view_comment.getText();
                    Selection.setSelection(edt, edt.length());
                }
            }
        });

        Resources res = getApplicationContext().getResources();
        if(type.equals("notice_myteam")) {
            Drawable d = res.getDrawable(R.drawable.notice_myteam);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(255, 255, 255));
        }
        else if(type.equals("notice_team")) {
            Drawable d = res.getDrawable(R.drawable.notice_team);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(42, 117, 0));
        }
        else if(type.equals("notice_member")) {
            Drawable d = res.getDrawable(R.drawable.notice_member);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(59, 89, 152));
        }else if(type.equals("notice_me")){
            Drawable d = res.getDrawable(R.drawable.notice_me);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
