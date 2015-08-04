package handong.cconma.cconmaadmin.etc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import handong.cconma.cconmaadmin.http.JSONResponse;

public class HTTPConnector extends AsyncTask<String, Void, Boolean> {
    public JSONResponse response = null;
    private Context con;
    private ProgressDialog pd;
    private HashMap<String, String> param = null;
    private JSONObject result;
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
        try {
            String line = null;
            String appended = "";

            HttpURLConnection con = (HttpURLConnection) (new URL(str[0])).openConnection();
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
                result = new JSONObject(appended);
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
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        pd.dismiss();
        String str;
        if (success) {
            response.processFinish(result);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("에러! 읽은 데이터:");
            if (result != null)
                str = result.toString();
            else
                str = "데이터 읽기 오류";
            builder.setMessage(str)
                    .setCancelable(true)
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
