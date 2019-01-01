package app.mjordan.projectfrs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    private ArrayList<Rest_List> popularList;
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        LinearLayout popular_list;

        MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.PopularImg);
            popular_list=(LinearLayout)view.findViewById(R.id.List);
        }
    }


    public PopularAdapter(ArrayList<Rest_List> profilesList) {
        this.popularList = profilesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Rest_List popular = popularList.get(position);
        Picasso.get().load(popular.getLogo()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }
}
