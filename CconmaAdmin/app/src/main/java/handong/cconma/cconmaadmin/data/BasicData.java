package handong.cconma.cconmaadmin.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Young Bin Kim on 2015-07-30.
 */
public class BasicData {
    private String user_id, email, mem_no, name;

    private HashMap admin_board_list;
    private HashMap admin_board_hash_tag_list;

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

    public ArrayList<String> getUserInfo(){
        ArrayList<String> temp = new ArrayList<>();
        temp.add(user_id);
        temp.add(email);
        temp.add(mem_no);
        temp.add(name);

        return temp;
    }

    public HashMap getBoardList(){
        return admin_board_list;
    }

    public HashMap getHashTagList(){
        return admin_board_hash_tag_list;
    }
}
