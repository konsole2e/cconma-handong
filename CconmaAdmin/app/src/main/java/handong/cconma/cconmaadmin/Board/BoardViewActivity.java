package handong.cconma.cconmaadmin.board;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.data.BasicData;
import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.etc.MainAsyncTask;
import handong.cconma.cconmaadmin.mainpage.AdminApplication;
import handong.cconma.cconmaadmin.http.HttpConnection;
import handong.cconma.cconmaadmin.webpage.WebViewForOthers;


/**
 * 게시판 목록에서 하나 선택하여 글 내용을 보여주는 화면
 * Created by eundi on 15. 7. 6..
 */

public class BoardViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    boolean firstTime = true;
    boolean marked = false;
    int width_notice = 0;
    int insert_mode;
    int modify_position;
    private CircularProgressBar circularProgressBar;

    TextView text_board_view_title;
    LinearLayout layout_view_notice;
    TextView text_board_view_writer;
    TextView text_board_view_date;

    WebView webView_content;

    ListView list_board_view_comment;
    BoardCommentAdapter adapter_comment;

    EditText edit_board_view_comment;
    Button btn_board_view_comment;

    InputMethodManager input_manager;

    String boardarticle_no;
    String board_no;

    JSONObject result;


    String view_content = "";
    HashMap hashtag;

    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);

        board_no = this.getIntent().getStringExtra("board_no");
        boardarticle_no = this.getIntent().getStringExtra("boardarticle_no");
        marked = this.getIntent().getBooleanExtra("marked", false);
        from = this.getIntent().getStringExtra("from");


        Display dis = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width_notice = dis.getWidth() * 9 / 14;


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.progressbar_circular);
        new ViewAsyncTask().execute();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_board_view_comment:
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd\nHH:mm");
                    String strNow = sdfNow.format(date);
                    if (!(edit_board_view_comment.getText().toString()).equals("")) {
                        /*****************************     댓글 수정       ********************************/
                        if (edit_board_view_comment.getTag() != null) {
                            insert_mode = 1;
                            new InsertAsyncTask().execute();
                            edit_board_view_comment.setText("");
                        } else {
                            /*************************        댓글 입력         ****************************/
                            String html_comment = edit_board_view_comment.getText().toString().replace("\n", "<br>");
                            insert_mode = 0;
                            HashMap hash = new HashMap();
                            BasicData basicData = BasicData.getInstance();
                            adapter_comment.addItem("123", basicData.getName(), strNow, html_comment, hash);
                            adapter_comment.notifyDataSetChanged();

                            new InsertAsyncTask().execute();

                            edit_board_view_comment.setText("");
                        }

                        input_manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        input_manager.hideSoftInputFromWindow(edit_board_view_comment.getWindowToken(), 0);
                    }
                    break;
                case R.id.edit_board_view_comment:
                    list_board_view_comment.setSelectionFromTop(adapter_comment.getCount() - 1, 0);
                    break;
            }

        }
    };

    public void dialog(final int index, final int position) {
        String alert_message = "";
        String pos_message = "";
        String neg_message = "";
        AlertDialog.Builder alert_build = new AlertDialog.Builder(this);
        switch (index) {
            case 0:
                alert_message = "게시글을 수정하시겠습니까?\n※ 사진이나 표가 포함된 글은 수정 시 " +
                        "제대로 수정되지 않습니다";
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

                            String tag = "";
                            for (int i = 0; i < hashtag.size(); i++) {
                                tag = tag + "@" + hashtag.get("hash_tag" + i) + " ";
                            }
                            intent.putExtra("board_no", board_no);
                            intent.putExtra("boardarticle_no", boardarticle_no);
                            intent.putExtra("title", text_board_view_title.getText());
                            intent.putExtra("content", view_content);
                            intent.putExtra("tag", tag);


                            startActivity(intent);
                        } else if (index == 1) {
                            //게시글 삭제하는 코드.
                            try {
                                new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                                        + board_no + "/articles/" + boardarticle_no, "DELETE", "").execute().get();
                            } catch (Exception e) {

                            }

                            ////////////
                            HashMap board_list = BasicData.getInstance().getBoardList();
                            ArrayList<String> tabTitles = new ArrayList<>();
                            int index;
                            String board_no;

                            for (int i = 0; i < board_list.size() / 2; i++) {
                                tabTitles.add(board_list.get("board_title" + i).toString());
                            }

                            index = tabTitles.indexOf(getIntent().getStringExtra("board_no"));
                            if (index != -1) {
                                board_no = tabTitles.get(index).split("e")[1];
                                AdminApplication.getInstance().
                                        setTabPosition(Integer.parseInt(board_no));
                            }
                            AdminApplication.getInstance().setRefresh(true);
                            ////////////

                            finish();
                        } else {
                            adapter_comment.dialogComment(1, position);
                        }

                    }
                }).setNegativeButton(neg_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (index == 2) {

                    String tag = "";
                    for (int i = 0; i < adapter_comment.board_comment_list_data.get(position).comment_hashMap.size() / 2; i++) {
                        tag = tag + "@" + adapter_comment.board_comment_list_data.get(position).comment_hashMap.get("hash_tag" + i) + " ";
                    }
                    edit_board_view_comment.setText(tag + Html.fromHtml(adapter_comment.board_comment_list_data.get(position).comment).toString());
                    edit_board_view_comment.setTag(adapter_comment.board_comment_list_data.get(position).boardcomment_no);
                    modify_position = position;
                    //커서 위치 문자열 뒤쪽에 위치하도록.
                    Editable edt = edit_board_view_comment.getText();
                    Selection.setSelection(edt, edt.length());

                    list_board_view_comment.setSelectionFromTop(position, 0);
                } else
                    dialog.cancel();
            }
        });

        AlertDialog alert = alert_build.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    class InsertAsyncTask extends AsyncTask<String, Void, JSONObject> {
        String requestBody;
        String commentNO;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String html_comment = edit_board_view_comment.getText().toString().replace("\n", "<br>");
            if (insert_mode == 0) {
                requestBody = "board_no=" + board_no
                        + "&boardarticle_no=" + boardarticle_no
                        + "&content=" + html_comment;
            } else if (insert_mode == 1) {
                requestBody = "_METHOD=" + "PUT"
                        + "&board_no=" + board_no
                        + "&boardarticle_no=" + boardarticle_no
                        + "&comment_no=" + edit_board_view_comment.getTag()
                        + "&content=" + html_comment;

                commentNO = edit_board_view_comment.getTag().toString();
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                if (insert_mode == 0) {
                    HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                            + board_no + "/articles/" + boardarticle_no + "/comments", "POST", requestBody);
                    String sResult = connection.init();

                    JSONObject json = new JSONObject(sResult);
                    String commentN = json.getString("boardcomment_no");

                    HttpConnection conn = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                            + Integer.parseInt(board_no) + "/articles/"
                            + Integer.parseInt(boardarticle_no), "GET", "");
                    String sResults = conn.init();

                    JSONObject jsonObject = new JSONObject(sResults);

                    JSONArray jsonArray = jsonObject.getJSONArray("comments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject commentObj = jsonArray.getJSONObject(i);
                        String commentN2 = commentObj.getString("boardcomment_no");

                        if (commentN.equals(commentN2)) {

                            String comment_name = commentObj.getString("name");
                            String comment_reg_date = commentObj.getString("reg_date");
                            StringTokenizer st = new StringTokenizer(comment_reg_date, "- :");
                            int count = 0;
                            String date = "";
                            while (st.hasMoreTokens()) {
                                String stDate = st.nextToken();
                                if (count == 1) {
                                    date = date + stDate;
                                } else if (count == 2) {
                                    date = date + "/" + stDate;
                                } else if (count == 3) {
                                    date = date + "\n" + stDate;
                                } else if (count == 4) {
                                    date = date + ":" + stDate;
                                    break;
                                }
                                count++;
                            }
                            comment_reg_date = date;

                            String comment_content = commentObj.getString("content").toString();
                            String boardcomment_no = commentObj.getString("boardcomment_no");
                            JSONArray hashArr = commentObj.getJSONArray("comment_hash_tags");
                            HashMap commentHashMap = new HashMap();
                            if (hashArr.length() != 0) {
                                for (int j = 0; j < hashArr.length(); j++) {
                                    JSONObject hashObj = hashArr.getJSONObject(j);
                                    commentHashMap.put("hash_tag" + j, hashObj.getString("hash_tag"));
                                    commentHashMap.put("hash_tag_type" + j, hashObj.getString("hash_tag_type"));
                                }
                            }
                            adapter_comment.board_comment_list_data.remove(adapter_comment.getCount() - 1);
                            //list_board_view_comment.clearChoices();
                            adapter_comment.addItem(boardcomment_no, comment_name, comment_reg_date, comment_content, commentHashMap);
                        }
                    }
                } else if (insert_mode == 1) {
                    //modify
                    HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                            + board_no + "/articles/" + boardarticle_no + "/comments/"
                            + commentNO, "POST", requestBody);

                    String sResult = connection.init();

                    JSONObject json = new JSONObject(sResult);
                    String commentN = json.getString("boardcomment_no");

                    HttpConnection conn = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                            + Integer.parseInt(board_no) + "/articles/"
                            + Integer.parseInt(boardarticle_no), "GET", "");
                    String sResults = conn.init();


                    JSONObject jsonObject = new JSONObject(sResults);

                    JSONArray jsonArray = jsonObject.getJSONArray("comments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject commentObj = jsonArray.getJSONObject(i);
                        String commentN2 = commentObj.getString("boardcomment_no");

                        if (commentN.equals(commentN2)) {

                            String comment_content = commentObj.getString("content").toString();
                            JSONArray hashArr = commentObj.getJSONArray("comment_hash_tags");
                            HashMap commentHashMap = new HashMap();
                            if (hashArr.length() != 0) {
                                for (int j = 0; j < hashArr.length(); j++) {
                                    JSONObject hashObj = hashArr.getJSONObject(j);
                                    commentHashMap.put("hash_tag" + j, hashObj.getString("hash_tag"));
                                    commentHashMap.put("hash_tag_type" + j, hashObj.getString("hash_tag_type"));
                                }
                            }
                            adapter_comment.update(modify_position, comment_content, commentHashMap);
                        }
                    }


                } else if (insert_mode == 2) {
                    //delete
                    HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                            + board_no + "/articles/" + boardarticle_no + "/comments/"
                            + adapter_comment.board_comment_list_data.get(modify_position).boardcomment_no, "DELETE", "");
                    connection.init();

                    adapter_comment.board_comment_list_data.remove(modify_position);
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            edit_board_view_comment.setTag(null);
            adapter_comment.notifyDataSetChanged();
        }
    }

    class ViewAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            View header = getLayoutInflater().inflate(R.layout.board_list_header, null, false);

            header.setLongClickable(false);

            list_board_view_comment = (ListView) findViewById(R.id.list_board_view_comment);
            list_board_view_comment.addHeaderView(header);
            list_board_view_comment.setHeaderDividersEnabled(false);

            adapter_comment = new BoardCommentAdapter(BoardViewActivity.this);


            list_board_view_comment.setAdapter(adapter_comment);
            list_board_view_comment.setFocusable(false);

            layout_view_notice = (LinearLayout) findViewById(R.id.layout_view_notice);
            edit_board_view_comment = (EditText) findViewById(R.id.edit_board_view_comment);
            edit_board_view_comment.setOnClickListener(clickListener);
            btn_board_view_comment = (Button) findViewById(R.id.btn_board_view_comment);
            btn_board_view_comment.setOnClickListener(clickListener);

            text_board_view_title = (TextView) header.findViewById(R.id.text_board_view_title);
            text_board_view_writer = (TextView) header.findViewById(R.id.text_board_view_writer);
            text_board_view_date = (TextView) header.findViewById(R.id.text_board_view_date);

            webView_content = (WebView) header.findViewById(R.id.webView_content);

            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Log.d("test", "doInBackground");
                HttpConnection connection = new HttpConnection("http://www.cconma.com/admin/api/board/v1/boards/"
                        + Integer.parseInt(board_no) + "/articles/"
                        + Integer.parseInt(boardarticle_no), "GET", "");
                String sResult = connection.init();

                JSONObject json = new JSONObject(sResult);

                Log.d("test", json.toString());
                return json;
            } catch (Exception e) {
                Log.e("JSON", Log.getStackTraceString(e));
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            //pDialog.dismiss();
            try {
                String subject = json.getString("subject");
                String name = json.getString("name");
                String reg_date = json.getString("reg_date");
                String content = json.getString("content");

                view_content = Html.fromHtml(json.getString("content")).toString();

                JSONObject scrap = json.getJSONObject("scrap_info");
                String scrap_on = scrap.getString("scraped");

                if (scrap_on.equals("on"))
                    marked = true;
                else
                    marked = false;

                hashtag = new HashMap();


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
                        hashtag.put("hash_tag" + k, notice_tag);
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

                webView_content.getSettings().setBuiltInZoomControls(true);
                webView_content.canGoBackOrForward(10);
                webView_content.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                webView_content.loadData(content, "text/html; charset=utf-8", "UTF-8");
                webView_content.setWebViewClient(new WebClient());

                JSONArray jsonArray = json.getJSONArray("comments");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject commentObj = jsonArray.getJSONObject(i);
                    String comment_name = commentObj.getString("name");


                    String comment_reg_date = commentObj.getString("reg_date");
                    StringTokenizer st = new StringTokenizer(comment_reg_date, "- :");
                    int count = 0;
                    String date = "";
                    while (st.hasMoreTokens()) {
                        String stDate = st.nextToken();
                        if (count == 1) {
                            date = date + stDate;
                        } else if (count == 2) {
                            date = date + "/" + stDate;
                        } else if (count == 3) {
                            date = date + "\n" + stDate;
                        } else if (count == 4) {
                            date = date + ":" + stDate;
                            break;
                        }
                        count++;
                    }
                    comment_reg_date = date;

                    String comment_content = commentObj.getString("content").toString();
                    String boardcomment_no = commentObj.getString("boardcomment_no");
                    JSONArray hashArr = commentObj.getJSONArray("comment_hash_tags");
                    HashMap commentHashMap = new HashMap();
                    if (hashArr.length() != 0) {
                        for (int j = 0; j < hashArr.length(); j++) {
                            JSONObject hashObj = hashArr.getJSONObject(j);
                            commentHashMap.put("hash_tag" + j, hashObj.getString("hash_tag"));
                            commentHashMap.put("hash_tag_type" + j, hashObj.getString("hash_tag_type"));
                        }
                    }
                    adapter_comment.addItem(boardcomment_no, comment_name, comment_reg_date, comment_content, commentHashMap);
                }
                adapter_comment.notifyDataSetChanged();

            } catch (Exception e) {

            }

            circularProgressBar.setVisibility(View.GONE);
        }
    }

    public class BoardCommentAdapter extends BaseAdapter {


        public Context context = null;
        public ArrayList<BoardCommentData> board_comment_list_data = new ArrayList<BoardCommentData>();
        int width_comment = 0;

        public BoardCommentAdapter(Context context) {
            super();
            this.context = context;

            Display dis = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            width_comment = dis.getWidth() * 2 / 3;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_comment_list_item, null);

            holder.text_board_view_comment_writer = (TextView) convertView.findViewById(R.id.text_board_view_comment_writer);
            holder.text_board_view_comment = (TextView) convertView.findViewById(R.id.text_board_view_comment);
            holder.text_board_view_comment_date = (TextView) convertView.findViewById(R.id.text_board_view_comment_date);
            holder.layout_comment_notice = (LinearLayout) convertView.findViewById(R.id.layout_comment_notice);
            holder.btn_comment = (ImageButton) convertView.findViewById(R.id.btn_comment);
            holder.btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog(2, position);
                }
            });

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

            if (data.comment_hashMap.size() != 0) {
                int sum_of_width_notice = 0;
                int addingCount = 0;
                int layout_num = 0;
                LinearLayout l1 = new LinearLayout(convertView.getContext());
                LinearLayout l2 = new LinearLayout(convertView.getContext());
                LinearLayout l3 = new LinearLayout(convertView.getContext());
                for (int i = 0; i < data.comment_hashMap.size() / 2; i++) {
                    String hash_tag = data.comment_hashMap.get("hash_tag" + i).toString();
                    String hash_tag_type = data.comment_hashMap.get("hash_tag_type" + i).toString();

                    TextView textView = new TextView(convertView.getContext());
                    color(textView, hash_tag, hash_tag_type);

                    textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    sum_of_width_notice = sum_of_width_notice + textView.getMeasuredWidth() + 5;

                    if (sum_of_width_notice > width_comment) {
                        addingCount = 0;
                        layout_num++;
                        sum_of_width_notice = textView.getMeasuredWidth() + 5;
                    }

                    if (layout_num == 0 && addingCount == 0) {
                        holder.layout_comment_notice.addView(l1);
                    } else if (layout_num == 1 && addingCount == 0) {
                        holder.layout_comment_notice.addView(l2);
                    } else if (layout_num == 2 && addingCount == 0) {
                        holder.layout_comment_notice.addView(l3);
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

            holder.text_board_view_comment.setText(Html.fromHtml(data.comment));
            holder.text_board_view_comment.setMovementMethod(LinkMovementMethod.getInstance());
            //holder.text_board_view_comment.setAutoLinkMask(Linkify.WEB_URLS);
            holder.text_board_view_comment_date.setText(data.comment_date);

            CharSequence text = holder.text_board_view_comment.getText();
            if(text instanceof Spannable){
                int end = text.length();
                Spannable sp = (Spannable)holder.text_board_view_comment.getText();
                URLSpan[] urls=sp.getSpans(0, end, URLSpan.class);
                SpannableStringBuilder style=new SpannableStringBuilder(text);
                style.clearSpans();//should clear old spans
                for(URLSpan url : urls){
                    CustomerTextClick click = new CustomerTextClick(url.getURL());
                    style.setSpan(click, sp.getSpanStart(url),sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.text_board_view_comment.setText(style);
            }

            convertView.setFocusable(false);
            return convertView;
        }

        public void addItem(String boardcomment_no, String comment_writer, String comment_date, String comment, HashMap commentHashMap) {
            BoardCommentData addData = new BoardCommentData();

            addData.boardcomment_no = boardcomment_no;
            addData.comment = comment;
            addData.comment_date = comment_date;
            addData.commnet_writer = comment_writer;
            addData.comment_hashMap = commentHashMap;

            board_comment_list_data.add(addData);
        }

        public class ViewHolder {
            public TextView text_board_view_comment_writer;
            public TextView text_board_view_comment;
            public TextView text_board_view_comment_date;

            public ImageButton btn_comment;


            public LinearLayout layout_comment_notice;

        }

        public void update(int position, String comment, HashMap hashMap) {
            BoardCommentData updateData = board_comment_list_data.get(position);

            board_comment_list_data.get(position).comment = comment;
            board_comment_list_data.get(position).comment_hashMap = hashMap;
        }

        public void dialogComment(final int index, final int position) {
            String alert_message = "";
            AlertDialog.Builder alert_build = new AlertDialog.Builder(context);
            alert_build.setMessage("댓글을 삭제하시겠습니까?").setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ////댓글 삭제하는 코드
                            try {
                                insert_mode = 2;
                                modify_position = position;
                                new InsertAsyncTask().execute();
                            } catch (Exception e) {

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

    public void color(final TextView textView, String tag, String type) {
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
                Pattern pattern = Pattern.compile("@" + textView.getText().toString().substring(1));
                Matcher matcher = pattern.matcher(currentComment);
                while (matcher.find()) {
                    isValid = false;
                }
                if (isValid) {
                    edit_board_view_comment.setText(currentComment + "@" + textView.getText().toString().substring(1));
                    Editable edt = edit_board_view_comment.getText();
                    Selection.setSelection(edt, edt.length());
                }
            }
        });

        Resources res = getApplicationContext().getResources();
        if (type.equals("notice_myteam")) {
            Drawable d = res.getDrawable(R.drawable.notice_myteam);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(255, 255, 255));
        } else if (type.equals("notice_team")) {
            Drawable d = res.getDrawable(R.drawable.notice_team);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(42, 117, 0));
        } else if (type.equals("notice_member")) {
            Drawable d = res.getDrawable(R.drawable.notice_member);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(59, 89, 152));
        } else if (type.equals("notice_me")) {
            Drawable d = res.getDrawable(R.drawable.notice_me);
            textView.setBackgroundDrawable(d);
            textView.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (from.equals("Mark")) {
                    Intent intent = new Intent(this, BoardMarkedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
                return true;
            case R.id.favorite:
                AlertDialog.Builder alert_build = new AlertDialog.Builder(this);
                String alertStr = "";
                if (!marked) {
                    alertStr = "이 글을 즐겨찾기 등록하시겠습니까?";
                } else {
                    alertStr = "이 글을 즐겨찾기 해제하시겠습니까?";

                }
                alert_build.setMessage(alertStr).setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //수정하거나 삭제하는 코드.
                                BasicData basicData = BasicData.getInstance();
                                try {
                                    JSONObject json = new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/"
                                            + board_no
                                            + "/articles/" + boardarticle_no
                                            + "/scraped_members/" + basicData.getMem_no()
                                            , "PUT", "").execute().get();
                                    Log.d("scrap", json.get("status").toString());
                                } catch (Exception e) {

                                }

                                if (!marked) {
                                    Toast.makeText(getApplicationContext(), "즐겨찾기 추가되었습니다", Toast.LENGTH_SHORT).show();
                                    marked = true;
                                } else {

                                    Toast.makeText(getApplicationContext(), "즐겨찾기 해제되었습니다", Toast.LENGTH_SHORT).show();
                                    marked = false;
                                }


                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = alert_build.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
                break;
            case R.id.complete:
                AlertDialog.Builder alert_builder = new AlertDialog.Builder(this);
                String alertSt = "";
                final String complete = text_board_view_title.getText().toString().substring(0, 4);
                if (!complete.equals("[완료]")) {
                    alertSt = "이 글을 완료하시겠습니까?";
                } else {
                    alertSt = "이 글을 완료 해제하시겠습니까?";
                }
                final String html_content = view_content.replace("\n", "<br>");
                alert_builder.setMessage(alertSt).setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //수정하거나 삭제하는 코드.
                                BasicData basicData = BasicData.getInstance();
                                String tag = "";
                                for (int i = 0; i < hashtag.size(); i++) {
                                    tag = tag + "@" + hashtag.get("hash_tag" + i) + " ";
                                }
                                if (!complete.equals("[완료]")) {
                                    Toast.makeText(getApplicationContext(), "완료 되었습니다", Toast.LENGTH_SHORT).show();
                                    text_board_view_title.setText("[완료]" + text_board_view_title.getText());
                                } else {
                                    Toast.makeText(getApplicationContext(), "완료 해제되었습니다", Toast.LENGTH_SHORT).show();
                                    text_board_view_title.setText(text_board_view_title.getText().toString().substring(4));
                                }

                                try {

                                    String requestBody = "subject=" + text_board_view_title.getText()
                                            + "&content=" + tag + html_content
                                            + "&_METHOD=" + "PUT"
                                            + "&filename1=" + ""
                                            + "&filename2=" + "";

                                    Log.d("test", requestBody);
                                    new MainAsyncTask("http://www.cconma.com/admin/api/board/v1/boards/" + board_no
                                            + "/articles/" + boardarticle_no, "POST", requestBody).execute().get();
                                } catch (Exception e) {

                                }


                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alert_builder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                break;
            case R.id.modify:
                dialog(0, 0);
                break;
            case R.id.delete:
                dialog(1, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);

        return true;
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Cookies.getInstance(BoardViewActivity.this).updateCookies(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(webView_content.canGoBack())
            webView_content.goBack();
        else
            super.onBackPressed();
    }
    class CustomerTextClick extends ClickableSpan {

        private String mUrl;
        CustomerTextClick(String url) {
            mUrl =url;
        }
        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(BoardViewActivity.this, WebViewForOthers.class);
            intent.putExtra("url", mUrl);
            startActivity(intent);
        }
    }
}

