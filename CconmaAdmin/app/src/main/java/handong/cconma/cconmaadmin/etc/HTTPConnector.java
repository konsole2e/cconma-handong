package handong.cconma.cconmaadmin.etc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HTTPConnector extends AsyncTask<String, Void, Boolean> {
    public JSONResponse response = null;
    private Context con;
    private ProgressDialog pd;
    private JSONObject param = null;
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

    public HTTPConnector(Context context, JSONObject parameter) {
        con = context;
        param = parameter;
        response = (JSONResponse) context;
    }

    public HTTPConnector(Context context, JSONObject parameter, String requsestMethod) {
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
                con.setRequestMethod(method);
                //con.setDoOutput(true);
                con.setDoInput(true);
                BufferedInputStream is;
                //BufferedOutputStream os;

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //os = new BufferedInputStream(con.getOutputStream());
                    is = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while ((line = reader.readLine()) != null) {
                        appended = appended + line;
                    }
                    //  os.close();
                    is.close();
                    reader.close();
                    JSONObject j = new JSONObject(appended);
                    result.add(j);
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
}
