package handong.cconma.cconmaadmin.etc;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import handong.cconma.cconmaadmin.http.HttpConnection;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class MainAsyncTask extends AsyncTask<JSONObject, String, String> {

    private final static String TAG = "debugging";
    private String url;

    public MainAsyncTask(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        String sResult;

        Log.d(TAG, "task start");
        HttpConnection connection = new HttpConnection(url, "POST", "");
        sResult = connection.init();

        return sResult;
    }
}
