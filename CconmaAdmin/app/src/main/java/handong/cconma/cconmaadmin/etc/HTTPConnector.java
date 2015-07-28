package handong.cconma.cconmaadmin.etc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.CookieManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPConnector extends AsyncTask<String, Void, Boolean> {
    public JSONResponse response = null;
    private Context con;
    private ProgressDialog pd;
    private HashMap<String, String> param = null;
    private ArrayList<JSONObject> result = new ArrayList<>();
    private String method = "GET";
    private String msg = "잠시 기다려 주세요";

    public HTTPConnector(Context context) {
        con = context;
        response = (JSONResponse) context;
    }

    public HTTPConnector(Context context, String requsestMethod) {
        con = context;
        method = requsestMethod;
        response = (JSONResponse) context;
    }

    public HTTPConnector(Context context, HashMap<String, String> parameter) {
        con = context;
        param = parameter;
        response = (JSONResponse) context;
    }

    public HTTPConnector(Context context, HashMap<String, String> parameter, String requsestMethod) {
        con = context;
        param = parameter;
        method = requsestMethod;
        response = (JSONResponse) context;
    }

    public HTTPConnector setProgressMessage(String message) {
        msg = message;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = ProgressDialog.show(con, "", msg, true, true);
    }

    @Override
    protected Boolean doInBackground(String... str) {
        for (String s : str) {
            try {
                String line = null;
                String appended = "";

                HttpURLConnection con = (HttpURLConnection) (new URL(s)).openConnection();
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setRequestMethod(method);
                con.setDoOutput(true);
                con.setDoInput(true);

                if (param != null) {
                    BufferedOutputStream os = new BufferedOutputStream(con.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(param));
                    writer.flush();
                    writer.close();
                    os.close();
                }

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedInputStream is = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "EUC-KR"));
                    while ((line = reader.readLine()) != null) {
                        appended = appended + line;
                    }
                    is.close();
                    reader.close();
                    result.add(new JSONObject(appended));
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        pd.dismiss();
        if (success) {
            response.processFinish(result);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("네트워크 오류");
            builder.setMessage("데이터를 읽어 올 수 없습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).show();

        }
    }

    private String getPostDataString(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return result.toString();
    }
}
