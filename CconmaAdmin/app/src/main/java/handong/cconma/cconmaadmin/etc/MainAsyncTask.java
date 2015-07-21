package handong.cconma.cconmaadmin.etc;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class MainAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {

    private final static String TAG = "debugging";
    private String url;
    private String method;
    private String jsonString;
    private JSONObject json;

    public MainAsyncTask(String url, String method, String jsonString) {
        this.url = url;
        this.method = method;
        this.jsonString = jsonString;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        String sResult;

        Log.d(TAG, "task start");
        HttpConnection connection = new HttpConnection(url, method, jsonString);
        sResult = connection.init();
        Log.d(TAG, sResult);
        try {
            json = new JSONObject(sResult);
        }catch(Exception e){
            Log.d(TAG, "JSON Exception: " + e.getMessage());
        }

        return json;
    }
}