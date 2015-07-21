package handong.cconma.cconmaadmin.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import handong.cconma.cconmaadmin.data.Cookies;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class HttpConnection  {
    private String url;
    private String method;
    private String jsonString;
    private HttpURLConnection conn;

    private static String TAG = "debugging";

    public HttpConnection(String url, String method, String jsonString){
        this.url = url;
        this.method = method;
        this.jsonString = jsonString;
    }

    public String init() {
        URL target_url = null;
        String sResult = "ERROR";

        try {
            target_url = new URL(this.url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "URL exception!!! " + e.getMessage());
        }

        try {
            conn = (HttpURLConnection) target_url.openConnection();
            Log.d(TAG, "Connection start");
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Cookie", Cookies.getInstance().getCurrentCookies());
            Log.d(TAG, "COOKIE: " + Cookies.getInstance().getCurrentCookies());
            conn.setDoInput(true);
            conn.setRequestMethod(method);

            //if(conn.getResponseMessage().equals("OK"));
            if(!jsonString.equals("")) {
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(jsonString);
                osw.flush();
            }

            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            sResult = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IO EXCEPTION!!! " + e.getMessage());
        }
        Log.d(TAG, "Return string is " + sResult);

        return sResult;
    }
}
