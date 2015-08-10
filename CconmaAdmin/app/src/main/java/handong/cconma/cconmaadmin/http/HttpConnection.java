package handong.cconma.cconmaadmin.http;

import android.app.SharedElementCallback;
import android.util.Log;
import android.util.Xml;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import handong.cconma.cconmaadmin.data.Cookies;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class HttpConnection  {
    private URL url;
    private String method;
    private String requestBody;
    private HttpURLConnection conn;
    private String responseBody;

    private static String TAG = "debugging";

    public HttpConnection(String url, String method, String requestBody){
        try {
            this.url = new URL(url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "URL exception!!! " + e.getMessage());
        }
        this.method = method;
        this.requestBody = requestBody;
    }
    public String init() {
        String sResult = "{\"empty\": \"none\"}";

        try {
            conn = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "Connection start");
            conn.setConnectTimeout(2000);
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Cookie", Cookies.getInstance(null).getCurrentCookies());
            Log.d("debugging", "conn cookie: " + conn.getRequestProperty("Cookie"));
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            if(method.equals("POST")){
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            else{
                conn.setRequestProperty("Content-Type", "application/json");
            }

            if(!requestBody.equals("")) {
                OutputStream os = conn.getOutputStream();
                os.write(requestBody.getBytes());
                Log.d("TAG", String.valueOf(requestBody.getBytes()));
                os.close();
            }

            int responseCode = conn.getResponseCode();

            InputStream inputStream = null;
            try {
                if (responseCode == 200) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }
                responseBody = getString(inputStream);
                Log.d(TAG, responseBody);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.d(TAG, "IOException in HttpConnection line 91: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IO EXCEPTION!!! " + e.getMessage());
        }

        Log.d(TAG, "Return string is " +  responseBody);

        conn.disconnect();
        return responseBody;
    }

    private String getString(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        String newLine;
        do {
            newLine = reader.readLine();
            if (newLine != null) {
                content.append(newLine).append('\n');
            }
        } while (newLine != null);
        if (content.length() > 0) {
            // strip last newline
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }
}
