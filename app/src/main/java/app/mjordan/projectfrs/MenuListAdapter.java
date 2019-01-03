package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.nikartm.support.ImageBadgeView;

public class MenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int TYPE_TITLE_LIST=0;
    private static final int TYPE_CARD_LIST=2;
    private ArrayList<Menu> menuArrayList;
    private Context mContext;
    private int item;

    ImageBadgeView cart;
    DialogFragmentInterface listener;

    public ArrayList<Menu> getMenuArrayList() {
        return menuArrayList;
    }

    public void setMenuArrayList(ArrayList<Menu> menuArrayList) {
        this.menuArrayList = menuArrayList;
        this.notifyDataSetChanged();

    }


    class Title extends RecyclerView.ViewHolder {
        TextView title;

        Title(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.SubHeading);
        }
    }

    class Card extends RecyclerView.ViewHolder {
        TextView title,description,price;
        LinearLayout item,line;
        Card(View view) {
            super(view);
            item=(LinearLayout)view.findViewById(R.id.card_menu);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.Price);
            description = (TextView) view.findViewById(R.id.Des);
            line=(LinearLayout)view.findViewById(R.id.centerline);
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


    MenuListAdapter(Context c, ArrayList<Menu> menuArrayList,ImageBadgeView cart,DialogFragmentInterface listener){
        this.mContext=c;
        this.menuArrayList=menuArrayList;
        this.cart=cart;
        this.listener=listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.sub_menu_title, parent, false);
                return new Title(itemView);
            case 2:
                View itemView1 = LayoutInflater.from(mContext).inflate(R.layout.sub_card_item, parent, false);
                return new Card(itemView1);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Menu menu=menuArrayList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                Title title_holder = (Title) holder;
                title_holder.title.setText(menu.getTitle());
                break;

            case 2:
                Card card_holder = (Card)holder;
                card_holder.title.setText(menu.getProduct());
                card_holder.description.setText(menu.getDes());
                String price="Rs "+menu.getPrice();
                card_holder.price.setText(price);
                if(menu.isDesc()){
                    card_holder.description.setVisibility(View.VISIBLE);
                    card_holder.description.setText(menu.getDes());
                    ViewGroup.LayoutParams params = card_holder.line.getLayoutParams();
                    // Changes the height and width to the specified *pixels*
                    params.height = card_holder.description.getMaxHeight();
                    params.width = params.width;
                    card_holder.line.setLayoutParams(params);
                }else{
                    card_holder.description.setVisibility(View.GONE);
                }

                card_holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.dialog_frag(menu);
                    }

                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }


}
