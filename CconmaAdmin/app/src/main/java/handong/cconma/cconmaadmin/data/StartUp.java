package handong.cconma.cconmaadmin.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

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
            handleInfo(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleInfo(JSONObject responseJson){
        BasicData data = BasicData.getInstance();
        JSONObject json;
        JSONArray jsonArray;
        try {
            json = responseJson.getJSONObject("user_info");
            data.setUserInfo(json.getString("user_id"), json.getString("email"),
                    json.getString("mem_no"), json.getString("name"));

            jsonArray = responseJson.getJSONArray("admin_board_list");
            for(int i = 0; i < jsonArray.length(); i++){
                json = jsonArray.getJSONObject(i);
                String str = json.getString("board_no");
                String str2 = json.getString("board_title");

                data.setBoardList("board_no" + i, json.getString("board_no"));
                data.setBoardList("board_title" + i, json.getString("board_title"));
            }

            jsonArray = responseJson.getJSONArray("admin_board_hash_tag_list");
            for(int i = 0; i < jsonArray.length(); i++){
                json = jsonArray.getJSONObject(i);
                data.setHashTagList("hash_tag_type" + i, json.getString("hash_tag_type"));
                data.setHashTagList("hash_tag" + i, json.getString("hash_tag"));
            }

            jsonArray = responseJson.getJSONArray("admin_chart_menu_list");
            for(int i = 0; i < jsonArray.length(); i++){
                json = jsonArray.getJSONObject(i);

                data.setChartList("chart_name" + i, json.getString("chart_menu_name"));
                data.setChartList("chart_path" + i, json.getString("chart_api_path"));
            }

            jsonArray = responseJson.getJSONArray("admin_webview_menu_list");
            for(int i = 0; i < jsonArray.length(); i++) {
                json = jsonArray.getJSONObject(i);
                data.setMenuNameList("menu_name" + i, json.getString("webview_menu_name"));

                JSONArray jsonArray_temp = json.getJSONArray("webview_submenu");
                HashMap<String, String> submenu = new HashMap<>();
                for (int j = 0; j < jsonArray_temp.length(); j++) {
                    JSONObject json_temp = jsonArray_temp.getJSONObject(j);
                    submenu.put("submenu_name" + i + "-" + j, json_temp.getString("webview_submenu_name"));
                    submenu.put("submenu_url" + i + "-" + j, json_temp.getString("webview_url"));
                }
                data.setSubmenuNameList(submenu);
            }

            jsonArray = responseJson.getJSONArray("admin_stat_menu_list");
            for(int i = 0; i < jsonArray.length(); i++) {
                json = jsonArray.getJSONObject(i);

                data.setTextChartList("stat_name" + i, json.getString("stat_menu_name"));
                data.setTextChartList("stat_path" + i, json.getString("stat_api_path"));
            }
        }catch(Exception e){
            Log.e("debugging", "Exception in StartPage line 127: " + e.getMessage());
        }
    }
}
