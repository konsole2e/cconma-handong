package handong.cconma.cconmaadmin.etc;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class MainAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {

    private final static String TAG = "debugging";
    private String url;
    private String method;
    private String requestBody;
    private String sResult;

    public MainAsyncTask(String url, String method, String requestBody) {
        this.url = url;
        this.method = method;
        this.requestBody = requestBody;

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
}