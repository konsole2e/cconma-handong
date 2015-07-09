package handong.cconma.cconmaadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eundi on 15. 7. 9..
 */
public class PushAdapter extends BaseAdapter{

    public Context context = null;
    public ArrayList<PushData> push_list_data = new ArrayList<PushData>();

    public PushAdapter(Context context){
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return push_list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return push_list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderBoard vhBoard;
        ViewHolderStock vhStock;
        ViewHolderQna vhQna;

        if(convertView == null){
            vhBoard = new ViewHolderBoard();
            vhStock = new ViewHolderStock();
            vhQna = new ViewHolderQna();

            LayoutInflater inflaterBoard = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflaterStock = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflaterQna = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaterBoard.inflate(R.layout.push_list_item_board, null);
            convertView = inflaterStock.inflate(R.layout.push_list_item_stock, null);
            convertView = inflaterQna.inflate(R.layout.push_list_item_qna, null);

            vhBoard.text_push_board = (TextView)convertView.findViewById(R.id.text_push_board);
            vhStock.text_push_stock = (TextView)convertView.findViewById(R.id.text_push_stock);
            vhQna.text_push_qna = (TextView)convertView.findViewById(R.id.text_push_qna);

            convertView.setTag(vhBoard);
            convertView.setTag(vhStock);
            convertView.setTag(vhQna);

        }



        return null;
    }


    public class ViewHolderBoard{
        public TextView text_push_board;
    }
    public class ViewHolderStock{
        public TextView text_push_stock;
    }
    public class ViewHolderQna{
        public TextView text_push_qna;
    }
}
