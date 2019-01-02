package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TITLE_LIST=0;
    private static final int TYPE_CARD_LIST=2;
    private ArrayList<Menu> menuArrayList;
    private Context mContext;

    class Title extends RecyclerView.ViewHolder {
        TextView title;

        Title(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.SubHeading);
        }
    }

    class Card extends RecyclerView.ViewHolder {
        TextView title,description,price;

        Card(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.SubHeading);
            price = (TextView) view.findViewById(R.id.Price);
            description = (TextView) view.findViewById(R.id.Des);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Menu menu =menuArrayList.get(position);
        if(menu.getIsTitle()){
            return TYPE_TITLE_LIST;
        }else{
            return TYPE_CARD_LIST;
        }
    }


    public MenuListAdapter(Context c, ArrayList<Menu> menuArrayList){
        this.mContext=c;
        this.menuArrayList=menuArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_menu_title, parent, false);
                return new Title(itemView);
            case 2:
                View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_card_item, parent, false);
                return new Card(itemView1);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Menu menu=menuArrayList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                Title title_holder = (Title) holder;
                title_holder.title.setText(menu.getTitle());
                break;

            case 2:
                Card card_holder = (Card)holder;
                card_holder.title.setText(menu.getTitle());
                card_holder.description.setText(menu.getDes());
                card_holder.price.setText(menu.getPrice());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }
}
