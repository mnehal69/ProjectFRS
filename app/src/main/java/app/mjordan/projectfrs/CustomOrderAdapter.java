package app.mjordan.projectfrs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.nikartm.support.ImageBadgeView;

public class CustomOrderAdapter extends RecyclerView.Adapter<CustomOrderAdapter.Card>  {
    private ArrayList<Menu> order;
    private Context mContext;
    private OrderFragmentInterface listener;
    private MKB_DB dbHelper;

    CustomOrderAdapter(Context c, ArrayList<Menu> orderList,OrderFragmentInterface listener) {
        this.order = orderList;
        this.mContext=c;
        this.listener=listener;
        dbHelper=new MKB_DB(c);
    }

    public void setOrder(ArrayList<Menu> order) {
        this.order = order;
        this.notifyDataSetChanged();
    }

    class Card extends RecyclerView.ViewHolder {
        TextView title,quantity,price;
        LinearLayout item,line;
        ImageView add,remove;
        Card(View view) {
            super(view);
            item=(LinearLayout)view.findViewById(R.id.card_menu);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.Price);
            quantity = (TextView) view.findViewById(R.id.Quantity);
            line=(LinearLayout)view.findViewById(R.id.centerline);
            add=(ImageView)view.findViewById(R.id.AddBtn);
            remove=(ImageView)view.findViewById(R.id.RemoveBtn);
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
        final Card card_holder = (Card)holder;
        card_holder.title.setText(order_of_menu.getProduct());
        String quantityString=order_of_menu.getDes();
        card_holder.quantity.setText(quantityString);
        String price=order_of_menu.getDes()+" X  Rs"+order_of_menu.getPrice();
        card_holder.price.setText(price);
        card_holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant= Integer.parseInt(card_holder.quantity.getText().toString());
                quant=quant+1;
                card_holder.quantity.setText(String.valueOf(quant));
                String price=quant+" X  Rs"+order_of_menu.getPrice();
                card_holder.price.setText(price);
                listener.OrderChanged(order_of_menu.getID(),quant);
                dbHelper.Update_quantityUsingID(order_of_menu.getID(), String.valueOf(quant));
            }
        });
        card_holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant= Integer.parseInt(card_holder.quantity.getText().toString());
                quant=quant-1;
                if (quant<=0){
                    quant=0;
                }
                card_holder.quantity.setText(String.valueOf(quant));
                String price=quant+" X  Rs"+order_of_menu.getPrice();
                card_holder.price.setText(price);
                listener.OrderChanged(order_of_menu.getID(),quant);
                dbHelper.Update_quantityUsingID(order_of_menu.getID(), String.valueOf(quant));
            }
        });

//        card_holder.item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return order.size();
    }


}
