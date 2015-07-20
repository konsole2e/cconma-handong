package handong.cconma.cconmaadmin.data;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created by Young Bin Kim on 2015-07-20.
 */
public class Cookies {
    private CookieManager cookieManager;
    private static String currentCookies;
    private static String TAG = "debugging";
    private static Cookies mCookies;

    public Cookies(){
    }

    public static Cookies getInstance() {
        if (mCookies == null) {
            mCookies = new Cookies();
            return mCookies;
        }else{
            return mCookies;
        }
    }

    public void updateCookies(String url){
        cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(url);
        Log.d(TAG, "Cookie is " + cookies);
        currentCookies = cookies;
    }

    public static String getCurrentCookies(){
        Log.d(TAG, "cookie sent: " + currentCookies);
        return currentCookies;
    }
}
