package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.nikartm.support.ImageBadgeView;

public class CustomOrderAdapter extends RecyclerView.Adapter<CustomOrderAdapter.Card>  {
    private ArrayList<Menu> order;
    private Context mContext;

    CustomOrderAdapter(Context c, ArrayList<Menu> orderList) {
        this.order = orderList;
        this.mContext=c;
    }

    public void setOrder(ArrayList<Menu> order) {
        this.order = order;
        this.notifyDataSetChanged();
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

    @NonNull
    @Override
    public Card onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card_item, parent, false);

        return new Card(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Card holder, int position) {
        final Menu order_of_menu=order.get(position);
        Card card_holder = (Card)holder;
        card_holder.title.setText(order_of_menu.getProduct());
        String quantityString=order_of_menu.getDes()+"X quantity";
        card_holder.description.setText(quantityString);
        String price=order_of_menu.getDes()+" X  Rs"+order_of_menu.getPrice();
        card_holder.price.setText(price);
        card_holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return order.size();
    }


}
