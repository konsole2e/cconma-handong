package handong.cconma.cconmaadmin.etc;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JS on 2015-07-15.
 */
public interface JSONResponse {
    void processFinish(ArrayList<JSONObject> output);
}
