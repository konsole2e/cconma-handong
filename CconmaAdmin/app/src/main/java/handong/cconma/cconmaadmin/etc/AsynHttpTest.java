package handong.cconma.cconmaadmin.etc;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JS on 2015-07-13.
 */
public class AsynHttpTest extends AsyncHttpClient{
    public void test() throws JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://api.androidhive.info/contacts/",
    }
}
