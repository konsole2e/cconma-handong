package handong.cconma.cconmaadmin.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import handong.cconma.cconmaadmin.etc.MainAsyncTask;
import handong.cconma.cconmaadmin.mainpage.StartPage;

/**
 * Created by Young Bin Kim on 2015-07-31.
 */
public class StartUp {
    private Context context;
    private JSONObject responseJson;
    public StartUp(Context context){
        this.context = context;
    }

    public void post(String requestBody){
        try {
            responseJson = new MainAsyncTask(
                    "http://www.cconma.com/mobile/admin-app/startup.pmv",
                    "POST", requestBody).execute().get();
            Cookies.getInstance(context).updateCookies("http://wwww.cconma.com/mobile/admin-app/startup.pmv");
        } catch (Exception e) {
            e.printStackTrace();
        }

        handleInfo(responseJson);
    }

    public void handleInfo(JSONObject responseJson){
        BasicData data;
        try {
            JSONObject json = responseJson.getJSONObject("user_info");
        }catch(Exception e){
            Log.e("debugging", "Exception in StartPage line 127: " + e.getMessage());
        }
    }
}
