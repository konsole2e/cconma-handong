package handong.cconma.cconmaadmin;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by JS on 2015-07-10.
 */
public class AsynHttpTest {
    String url = "http://api.androidhive.info/contacts/";

    public AsynHttpTest() {
        JSONObject jsonObject =new JSONObject();
        JSONArray jsonArray = new JSONArray();

        AsyncHttpClient test = new AsyncHttpClient();
        test.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }

}