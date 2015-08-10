package handong.cconma.cconmaadmin.mainpage;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import handong.cconma.cconmaadmin.R;

/**
 * Created by Young Bin Kim on 2015-07-02.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final int VIEW_TYPE_USER  = 0;
    private static final int VIEW_TYPE_NAVMENU = 1;

    private CharSequence navTitles[];
    private int naviIcons[];
    private String name;
    private int status;

    public RecyclerViewAdapter(CharSequence Titles[], int Icons[], String Name, int Status){
        navTitles = Titles;
        naviIcons = Icons;
        name = Name;
        status = Status;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        ImageView imageViewIcon;
        TextView textViewName;
        ImageButton userArrow;

        int holderId;

        public ViewHolder(View itemView, int ViewType){
            super(itemView);

            if(ViewType == VIEW_TYPE_USER){
                textViewName = (TextView)itemView.findViewById(R.id.name);
                userArrow = (ImageButton) itemView.findViewById(R.id.user_arrow);
                holderId = 0;
            }
            else {
                textViewTitle = (TextView) itemView.findViewById(R.id.rowText);
                imageViewIcon = (ImageView) itemView.findViewById(R.id.rowIcon);
                holderId = 1;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_NAVMENU){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item_row, parent, false); //Inflating the layout
            ViewHolder viewHolder = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return viewHolder;
        }
        else if(viewType == VIEW_TYPE_USER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            ViewHolder viewHolder = new ViewHolder(v, viewType);

            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if(viewHolder.holderId ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            Log.d("position", String.valueOf(position));
            viewHolder.textViewTitle.setText(navTitles[position - 1]); // Setting the Text with the array of our Titles
            viewHolder.imageViewIcon.setBackgroundResource(naviIcons[position - 1]);// Settimg the image with array of our icons
        }
        else{
            viewHolder.textViewName.setText(name);
            if(status == 0){
                viewHolder.userArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            }
            else if(status == 1){
                viewHolder.userArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return navTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return VIEW_TYPE_USER;

        return VIEW_TYPE_NAVMENU;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
