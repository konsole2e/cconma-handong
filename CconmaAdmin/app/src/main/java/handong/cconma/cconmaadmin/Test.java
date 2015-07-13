package handong.cconma.cconmaadmin;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Seoyul on 2015-07-10.
 */
public class Test extends Activity {

    private class DownloadJson extends AsyncTask<String, String, String> {

        protected String doInBackground(String... arg0) {

            Log.d("JSON Data", "doInBackground() is called");

            try {

                return (String) getData((String) arg0[0]);

            } catch (Exception e) {

                return "download fail";

            }

        }

        protected void onPostExecute(String result) {

            Log.d("JSON Data", "onPostExecute() is called");

            try{

                JSONArray jArray = new JSONArray(result);

                String[] jsonName = {"tm", "ts", "x", "y"};
                String[][] parsedData = new String[jArray.length()][jsonName.length];
                JSONObject json = null;

                for(int i = 0; i < jArray.length(); i++){

                    json = jArray.getJSONObject(i);

                    if(json != null){

                        for(int j = 0; j < jsonName.length; j++){

                            parsedData[i][j] = json.getString(jsonName[j]);
                            Log.d("JSON Data", "data : " + parsedData[i][j] + "**************");

                        }

                    }

                }

                for(int i = 0; i < parsedData.length; i++){

                    Toast.makeText(getApplicationContext(), "jasonName[i] : " + jsonName[i], Toast.LENGTH_LONG).show();

                }

            }catch(JSONException e){

                e.printStackTrace();

            }

        }

        private String getData(String strUrl) {

            Log.d("JSON Data", "getData() is called");

            StringBuilder sb = new StringBuilder();

            try {

                BufferedInputStream bis = null;
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int responseCode;

                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);

                responseCode = con.getResponseCode();

                if (responseCode == 300) {

                    bis = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
                    String line = null;

                    while ((line = reader.readLine()) != null)
                        sb.append(line);

                    bis.close();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

            return sb.toString();

        }

    }

    public void sendData(){

        Log.d("JSON Data", "sendData() is called");

        String url = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=73&gridy=116";

        try{

            ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            Log.d("JSON Data", "ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);");
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();
            Log.d("JSON Data", "NetworkInfo netInfo = conManager.getActiveNetworkInfo();");

            if(netInfo != null && netInfo.isConnected())
                new DownloadJson().execute(url);
            else
                Toast.makeText(getApplicationContext(), "Network is disconnected", Toast.LENGTH_LONG).show();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}
