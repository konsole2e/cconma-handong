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
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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

    TextView text_board_view_title;
    TextView text_board_view_notice1;
    TextView text_board_view_notice2;
    TextView text_board_view_notice3;
    TextView text_board_view_notice4;
    TextView text_board_view_notice5;
    TextView text_board_view_notice6;
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
        //list_board_view_comment.setOnScrollListener(scrollListener);
        list_board_view_comment.setOnItemLongClickListener(itemClickListner);

        list_board_view_comment.setFocusable(false);

        layout_board_view_comment = (LinearLayout)findViewById(R.id.layout_board_view_comment);
        edit_board_view_comment = (EditText)findViewById(R.id.edit_board_view_comment);
        edit_board_view_comment.setOnClickListener(clickListener);
        btn_board_view_comment = (Button)findViewById(R.id.btn_board_view_comment);
        btn_board_view_comment.setOnClickListener(clickListener);

        text_board_view_title = (TextView)header.findViewById(R.id.text_board_view_title);
        text_board_view_notice1 = (TextView)header.findViewById(R.id.text_board_view_notice1);
        text_board_view_notice2 = (TextView)header.findViewById(R.id.text_board_view_notice2);
        text_board_view_notice3 = (TextView)header.findViewById(R.id.text_board_view_notice3);
        text_board_view_notice4 = (TextView)header.findViewById(R.id.text_board_view_notice4);
        text_board_view_notice5 = (TextView)header.findViewById(R.id.text_board_view_notice5);
        text_board_view_notice6 = (TextView)header.findViewById(R.id.text_board_view_notice6);
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

        String boardarticle_no = getIntent().getExtras().getString("number");

        MainAsyncTask aysnc = new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/board_no/12/article_no/"+boardarticle_no, "GET", "");

        try{

            result = aysnc.execute().get();

        }catch(Exception e){}


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
            //Log.d("list", "doInBackground " + source);
            Display dis;

            dis = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            try {

                if(source.substring(0, 4).equals("data")){

                    /*Log.d("list", "base64 Image");

                    String bytes64bytes = Base64.encodeToString(source.substring(4, source.length()).getBytes(), 0);
                    String txtPlainOrg = "";
                    byte[] bytePlainOrg = Base64.decode(bytes64bytes, 0);
                    Log.d("list", bytePlainOrg.toString());

                    //byte[] 데이터  stream 데이터로 변환 후 bitmapFactory로 이미지 생성
                    ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
                    Bitmap bm = BitmapFactory.decodeStream(inStream);

                    if(bm == null)
                        Log.d("list", "bm is null");
*/
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


                //BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inSampleSize = 7;






                //데이터 base64 형식으로 Decode



                /*if (options.outHeight > (dis.getHeight()) || options.outWidth > (dis.getWidth()*5/6)) {
                    scale = (int)Math.pow(2, (int)Math.round(Math.log((dis.getWidth()*5/6)/(double)Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;
                Log.d("list", ""+scale);
                is.close();

                is = new URL(source).openStream();
                Bitmap resize = BitmapFactory.decodeStream(is, null, options);*/



                /*Bitmap bit = BitmapFactory.decodeStream(is);
                Bitmap resize = bit;
                if(bit.getWidth() > dis.getWidth() || bit.getHeight() > (dis.getHeight()*3/4)) {
                    resize = Bitmap.createScaledBitmap(bit, (int) dis.getWidth()/2, (int) (dis.getHeight()/ 4), true);
                    Log.d("list", "RESIZE");
                }*/
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
            String tag="";
            if(data.comment_hashMap.size() != 0){
                for(int i=0; i < data.comment_hashMap.size()/2; i++)
                {
                    String type;
                    if(data.comment_hashMap.get("hash_tag_type"+i).equals("notice_myteam"))
                        type = "<font color=\"#22741C\">" + data.comment_hashMap.get("hash_tag"+i) + " " + "</font>";
                    else if(data.comment_hashMap.get("hash_tag_type"+i).equals("notice_team"))
                        type = "<font color=\"#6BA300\">" + data.comment_hashMap.get("hash_tag"+i) + " " + "</font>";
                    else if(data.comment_hashMap.get("hash_tag_type"+i).equals("notice_member"))
                        type = "<font color=\"#4641D9\">" + data.comment_hashMap.get("hash_tag"+i) + " " + "</font>";
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
            for(int k=0; k<noticeArr.length(); k++){
                JSONObject noticeObj = noticeArr.getJSONObject(k);
                String notice_tag = noticeObj.getString("hash_tag");
                String notice_tag_type = noticeObj.getString("hash_tag_type");
                switch(k){
                    case 0:
                        color(text_board_view_notice1, notice_tag, notice_tag_type);
                        break;
                    case 1:
                        color(text_board_view_notice2, notice_tag, notice_tag_type);
                        break;
                    case 2:
                        color(text_board_view_notice3, notice_tag, notice_tag_type);
                        break;
                    case 3:
                        color(text_board_view_notice4, notice_tag, notice_tag_type);
                        break;
                    case 4:
                        color(text_board_view_notice5, notice_tag, notice_tag_type);
                        break;
                    case 5:
                        color(text_board_view_notice6, notice_tag, notice_tag_type);
                        break;
                }
            }

            text_board_view_date.setText(reg_date);
            text_board_view_title.setText(subject);
            text_board_view_title.setPaintFlags(text_board_view_title.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
            text_board_view_writer.setText(name);
            text_board_view_writer.setPaintFlags(text_board_view_writer.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

            Spanned spanned = Html.fromHtml(content, this, null);
            text_board_view_content.setText(spanned);
            text_board_view_content.setMovementMethod(LinkMovementMethod.getInstance());


            JSONArray jsonArray = json.getJSONArray("comment_list");
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

    public void color(final TextView textView, String tag, String type){
        textView.setText(" " + tag + " ");
        textView.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentComment = edit_board_view_comment.getText().toString();
                boolean isValid = true;
                Pattern pattern = Pattern.compile("@"+textView.getText()+" ");
                Matcher matcher = pattern.matcher(currentComment);
                while(matcher.find()){
                    isValid = false;
                }
                if(isValid){
                    edit_board_view_comment.setText(currentComment + "@" + textView.getText() + " ");
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
}
