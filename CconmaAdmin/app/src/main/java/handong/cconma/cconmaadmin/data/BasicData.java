package handong.cconma.cconmaadmin.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Young Bin Kim on 2015-07-30.
 */
public class BasicData {
    private String user_id, email, mem_no, name;
    private HashMap admin_stat_list = new HashMap();
    private HashMap admin_chart_list = new HashMap();
    private HashMap admin_board_list = new HashMap();
    private HashMap admin_board_hash_tag_list = new HashMap();
    private HashMap menu_name_list = new HashMap();
    private ArrayList<HashMap> submenu_name_list = new ArrayList<>();
    private static BasicData basicData;

    public static BasicData getInstance(){
            if (basicData == null) {
                basicData = new BasicData();
                return basicData;
            }else{
                return basicData;
             }
    }

    public void setUserInfo(String user_id, String email, String mem_no, String name){
        this.user_id = user_id;
        this.email = email;
        this.mem_no = mem_no;
        this.name = name;
    }

    public void setBoardList(String key, String value){
        admin_board_list.put(key, value);
    }

    public void setHashTagList(String key, String value){
        admin_board_hash_tag_list.put(key, value);
    }

    public void setMenuNameList(String key, String value){ menu_name_list.put(key, value); }

    public void setSubmenuNameList(HashMap list){
        submenu_name_list.add(list);
    }

    public ArrayList<String> getUserInfo(){
        ArrayList<String> temp = new ArrayList<>();
        temp.add(user_id);
        temp.add(email);
        temp.add(mem_no);
        temp.add(name);

        return temp;
    }

    public String getName(){ return name; }

    public HashMap getBoardList(){
        return admin_board_list;
    }

    public HashMap getHashTagList(){
        return admin_board_hash_tag_list;
    }

    public void setChartList(String key, String value){
        admin_chart_list.put(key, value);
    }

    public HashMap getChartList() {
        return admin_chart_list;
    }

    public String getMem_no(){
        return mem_no;
    }

    public HashMap getMenuNameList(){ return menu_name_list; }

    public ArrayList getSubmenuNameList(){ return submenu_name_list; }

    public void setTextChartList(String key, String value){
        admin_stat_list.put(key, value);
    }

    public HashMap getTextChartList() {
        return admin_stat_list;
    }
}
