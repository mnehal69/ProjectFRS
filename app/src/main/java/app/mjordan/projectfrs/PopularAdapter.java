package app.mjordan.projectfrs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    private ArrayList<Rest_List> popularList;
    PopularItemClickListener listener;


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        LinearLayout popular_list;

        MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.PopularImg);
            popular_list=(LinearLayout)view.findViewById(R.id.List);
        }
    }


    public PopularAdapter(PopularItemClickListener listener,ArrayList<Rest_List> profilesList) {
        this.listener=listener;
        this.popularList = profilesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Rest_List popular = popularList.get(position);
        holder.popular_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(popular);
            }
        });
        Picasso.get().load(popular.getLogo()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }
}
