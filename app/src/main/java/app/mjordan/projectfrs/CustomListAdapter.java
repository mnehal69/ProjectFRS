package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Rest_List> {
    private ArrayList<Rest_List> RestArrayList;
    private Context mContext;
    CustomListAdapter(Context context, ArrayList<Rest_List> RestList) {
        super(context,0,RestList);
        this.mContext=context;
        this.RestArrayList=RestList;
    }
    private class ViewHolder{
        ImageView logo;
        TextView rating,title,location,time;
    }

    private ViewHolder viewHolder=null;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =convertView;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        }
        viewHolder=new ViewHolder();
        Rest_List rest=RestArrayList.get(position);

        viewHolder.logo=(ImageView)view.findViewById(R.id.List_Logo);
        Picasso.get().load(rest.getLogo()).into(viewHolder.logo);

        viewHolder.rating=(TextView)view.findViewById(R.id.ListRating);
        viewHolder.rating.setText(rest.getRating());

        viewHolder.title=(TextView)view.findViewById(R.id.ListName);
        viewHolder.title.setText(rest.getName());

        viewHolder.location=(TextView)view.findViewById(R.id.ListLocation);
        viewHolder.location.setText(rest.getDest());

        viewHolder.time=(TextView)view.findViewById(R.id.ListTime);
        viewHolder.time.setText(rest.getTime());

        return view;
    }

    @Override
    public int getCount() {
        return this.RestArrayList.size();
    }
}
