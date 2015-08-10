package handong.cconma.cconmaadmin.textStatics;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JS on 2015-08-06.
 */
public class TextStaticsManager {
    private Context con;

    public TextStaticsManager(Context context) {
        con = context;
    }

    public void setData(JSONObject json, LinearLayout left, LinearLayout right) {
        String str ="";
        int dpInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, con.getResources().getDisplayMetrics());
        try {
            JSONArray valueList = json.getJSONArray(TextStaticsVariables.stat_value_list);
            for (int i = 0; i < valueList.length(); i++) {
                JSONObject obj = valueList.getJSONObject(i);

                TextView nameTv = new TextView(con);
                nameTv.setText(obj.optString(TextStaticsVariables.stat_name, ""));
                nameTv.setSingleLine();
                nameTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                nameTv.setEllipsize(TextUtils.TruncateAt.END);
                LinearLayout.LayoutParams paramsLeft = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsLeft.setMargins(dpInPx, dpInPx, dpInPx, 0);
                nameTv.setLayoutParams(paramsLeft);
                left.addView(nameTv);

                TextView valueTv = new TextView(con);
                LinearLayout.LayoutParams paramsRight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsRight.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, con.getResources().getDisplayMetrics()), dpInPx, dpInPx, 0);
                valueTv.setText(obj.optString(TextStaticsVariables.stat_value, ""));
                valueTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                valueTv.setSingleLine();
                valueTv.setLayoutParams(paramsRight);
                right.addView(valueTv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
