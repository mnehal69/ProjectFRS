package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomProfileAdapter extends ArrayAdapter<ProfileList> {
    private ArrayList<ProfileList> profileLists;
    private Context mContext;
    private static final int TYPE_EVEN = 0;
    private static final int TYPE_ODD = 1;
    private static final int TYPE_EDIT=2;
    private String ListType;
    CustomProfileAdapter(ArrayList<ProfileList> profileLists, Context c){
        super(c,0,profileLists);
        this.profileLists=profileLists;
        this.mContext=c;
    }

    public String getListType() {
        return ListType;
    }

    public void setListType(String listType) {
        ListType = listType;
    }

    private static class ViewHolder{
        TextView title,val;
        LinearLayout item;
        EditText edit_val;
    }
     private ViewHolder viewHolder=null;

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return profileLists.get(position).getType();
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentview = convertView;
        final ProfileList currentProfile = profileLists.get(position);

        if (currentview == null) {
                if (currentProfile.getType() == TYPE_EVEN) {
                    currentview = LayoutInflater.from(mContext).inflate(R.layout.even, parent, false);
                }
                if (currentProfile.getType() == TYPE_ODD) {
                    currentview = LayoutInflater.from(mContext).inflate(R.layout.odd, parent, false);
                }
                if(currentProfile.getType()==TYPE_EDIT){
                    currentview = LayoutInflater.from(mContext).inflate(R.layout.edit, parent, false);
                }
        }

        viewHolder = new ViewHolder();
        viewHolder.title = (TextView) currentview.findViewById(R.id.Title);
        viewHolder.title.setText(currentProfile.getTitle());

        if(currentProfile.getType()==TYPE_EDIT){
            viewHolder.edit_val = (EditText) currentview.findViewById(R.id.value);
            viewHolder.edit_val.setText(currentProfile.getValue());
        }else {
            viewHolder.val = (TextView) currentview.findViewById(R.id.value);
            viewHolder.val.setText(currentProfile.getValue());
        }

        if (currentProfile.getType() == TYPE_ODD) {
            viewHolder.item=(LinearLayout) currentview.findViewById(R.id.item);
            viewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        }
            return currentview;
        }
}
