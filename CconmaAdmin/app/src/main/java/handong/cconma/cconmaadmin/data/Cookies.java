package handong.cconma.cconmaadmin.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class Cookies extends Application {
    private CookieManager cookieManager;
    private static String currentCookies = "";
    private static String TAG = "debugging";
    private static Cookies mCookies;
    private Context context;


    public Cookies(Context context){
        this.context = context;
    }

    public static Cookies getInstance(Context context) {
        //if (mCookies == null) {
            mCookies = new Cookies(context);
            return mCookies;
        //}else{
        //    return mCookies;
       // }

    }

    public void updateCookies(String url){
        cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(url);
        Log.d(TAG, "Cookie is " + cookies);
        saveAutoLoginToken(cookies);
        currentCookies = cookies;
    }

    public void removeAllCookies(){
        cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
    public static String getCurrentCookies(){
        Log.d(TAG, "cookie sent: " + currentCookies);
        return currentCookies;
    }

    protected void saveAutoLoginToken(String cookies){
        String[] temp = cookies.split(";");

        for(int i = 0; i < temp.length; i++){
            String data;
            if(temp[i].contains("autoLoginAuthEnabled")){
                 data = temp[i].split("=")[1];
                new IntegratedSharedPreferences(context).
                        put("AUTO_LOGIN_AUTH_ENABLED", data);
            }
            if(temp[i].contains("autoLoginAuthToken")){
                data = temp[i].split("=")[1];
                new IntegratedSharedPreferences(context).
                        put("AUTO_LOGIN_AUTH_TOKEN", data);
            }
        }
    }
}
