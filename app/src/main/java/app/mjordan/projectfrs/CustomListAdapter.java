package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.MyViewHolder>  {
    private ArrayList<Rest_List> AroundRestaurentList;
    PopularItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView rating,title,location,time;
        LinearLayout Item;

        MyViewHolder(View view) {
            super(view);
            logo = view.findViewById(R.id.List_Logo);
            Item= view.findViewById(R.id.List);
            rating= view.findViewById(R.id.ListRating);
            title= view.findViewById(R.id.ListName);
            location= view.findViewById(R.id.ListLocation);
            time= view.findViewById(R.id.ListTime);
        }
    }

    CustomListAdapter(PopularItemClickListener listener,ArrayList<Rest_List> RestList) {
        this.listener=listener;
        this.AroundRestaurentList=RestList;
    }

    @NonNull
    @Override
    public CustomListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Rest_List rest=AroundRestaurentList.get(position);
        holder.Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(rest);
            }
        });
        Picasso.get().load(rest.getLogo()).into(holder.logo);
        holder.rating.setText(rest.getRating());
        holder.title.setText(rest.getName());
        holder.location.setText(rest.getDest());
        holder.time.setText(rest.getTime());
    }


    public int getItemCount() {
        return AroundRestaurentList.size();
    }
}
