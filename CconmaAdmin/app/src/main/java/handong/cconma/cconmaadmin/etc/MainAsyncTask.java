package handong.cconma.cconmaadmin.etc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import handong.cconma.cconmaadmin.http.HttpConnection;
import handong.cconma.cconmaadmin.http.JSONResponse;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class MainAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
    private final static String TAG = "debugging";
    private String url;
    private String method;
    private String requestBody;
    private String sResult;
    private Context con = null;
    private String msg = "";
    private ProgressDialog pd;

    public MainAsyncTask(String url, String method, String requestBody) {
        this.url = url;
        this.method = method;
        this.requestBody = requestBody;
    }

    public MainAsyncTask(String url, String method, String requestBody, Context context) {
        this.url = url;
        this.method = method;
        this.requestBody = requestBody;
        this.con = context;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {

        HttpConnection connection = new HttpConnection(url, method, requestBody);
        sResult = connection.init();
        Log.d(TAG, sResult);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(sResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!msg.equals("")) {
            pd = ProgressDialog.show(con, "", msg, true, true);
        }
    }

    public void setMessage(String str) {
        msg = str;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if (!msg.equals("")) {
            pd.dismiss();
        }
        if (con != null) {
            JSONResponse response = (JSONResponse) con;
            response.processFinish(jsonObject);
        }
        return;
    }
}