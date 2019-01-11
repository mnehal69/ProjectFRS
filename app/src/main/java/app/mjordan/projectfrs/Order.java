package app.mjordan.projectfrs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //Order.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Order extends Fragment {

    MKB_DB dbHelper;
    ArrayList<String> ItemId=new ArrayList<>(),NameList=new ArrayList<>();
    ArrayList<Integer>quantityList=new ArrayList<>(),priceList=new ArrayList<>();
    RecyclerView orderList;
    NestedScrollView Order;
    LinearLayout noOrder;
    TextView bill,fees, total_bill;
    int total=0,fee=200,total_amount;
    ImageButton close;
    private ArrayList<Menu> orderArrayList=new ArrayList<>();
    CustomOrderAdapter mAdapter;
    public Order() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper=new MKB_DB(getContext());
        Bundle bundle=getArguments();
        close= view.findViewById(R.id.OrderClose);
        Order= view.findViewById(R.id.Order);
        orderList= view.findViewById(R.id.menu_list);
        noOrder= view.findViewById(R.id.NoOrder);
        bill= view.findViewById(R.id.Bill);
        fees= view.findViewById(R.id.Fees);
        total_bill = view.findViewById(R.id.TotalBill);
        OrderFragmentInterface listener= new OrderFragmentInterface() {
            @Override
            public void OrderChanged(String ID, int item) {
                if (!ItemId.isEmpty()) {
                    if (ItemId.contains(ID)) {
                        total=0;
                        int index = ItemId.indexOf(ID);
                        quantityList.remove(index);
                        quantityList.add(index, item);
                        for(int iterate=0;iterate<ItemId.size();iterate++){
                            total=total+(priceList.get(iterate)*quantityList.get(iterate));
                        }
                        bill.setText(String.valueOf(total));
                        total_amount=total+fee;
                        total_bill.setText(String.valueOf(total_amount));
                    }
                }
            }
        };

        mAdapter = new CustomOrderAdapter(getContext(),orderArrayList,listener);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderList.setLayoutManager(layoutManager);
        orderList.setItemAnimator(new DefaultItemAnimator());
        orderList.setAdapter(mAdapter);


        if(bundle!=null) {
            noOrder.setVisibility(View.GONE);
            Order.setVisibility(View.VISIBLE);
            ItemId = bundle.getStringArrayList("Id");
            quantityList = bundle.getIntegerArrayList("item");
            NameList = bundle.getStringArrayList("name");
            priceList = bundle.getIntegerArrayList("price");

            for(int iterate=0;iterate<ItemId.size();iterate++){
                orderArrayList.add(new Menu(true,false, ItemId.get(iterate),"","",NameList.get(iterate),quantityList.get(iterate).toString(),priceList.get(iterate).toString()));
                total=total+(priceList.get(iterate)*quantityList.get(iterate));
            }

            bill.setText(String.valueOf(total));
            fees.setText(String.valueOf(fee));
            total_amount=total+fee;
            total_bill.setText(String.valueOf(total_amount));
            mAdapter.setOrder(orderArrayList);
        }


        if (orderArrayList.isEmpty()){
            noOrder.setVisibility(View.VISIBLE);
            Order.setVisibility(View.GONE);
        }else {
            noOrder.setVisibility(View.GONE);
            Order.setVisibility(View.VISIBLE);
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderArrayList.clear();
                ItemId.clear();
                quantityList.clear();
                priceList.clear();
                NameList.clear();
                dbHelper.DeleteAll_Order();
                noOrder.setVisibility(View.VISIBLE);
                Order.setVisibility(View.GONE);
            }
        });

        dbHelper.close();
    }

}
