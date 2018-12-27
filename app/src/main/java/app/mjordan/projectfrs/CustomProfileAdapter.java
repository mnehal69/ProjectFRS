package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomProfileAdapter extends ArrayAdapter<ProfileList> {
    private ArrayList<ProfileList> profileLists;
    private Context mContext;
    public CustomProfileAdapter(ArrayList<ProfileList> profileLists,Context c){
        super(c,0,profileLists);
        this.profileLists=profileLists;
        this.mContext=c;
    }
    private static class ViewHolder{
        TextView title,val;
    }
    ViewHolder viewHolder=null;


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentview=convertView;
        if(currentview==null){
            currentview= LayoutInflater.from(mContext).inflate(R.layout.even,parent,false);
        }

        viewHolder=new ViewHolder();
        final ProfileList currentProfile=profileLists.get(position);
        viewHolder.title=(TextView) currentview.findViewById(R.id.title);
        viewHolder.title.setText(currentProfile.getTitle());
        viewHolder.val=(TextView) currentview.findViewById(R.id.value);
        viewHolder.val.setText(currentProfile.getValue());
        return currentview;
    }




}
