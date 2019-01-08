package app.mjordan.projectfrs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

   // private OnFragmentInteractionListener mListener;
    MKB_DB dbHelper;
    ArrayList<String> ItemId=new ArrayList<>(),NameList=new ArrayList<>();
    ArrayList<Integer>quantityList=new ArrayList<>(),priceList=new ArrayList<>();
    RecyclerView orderList;
    NestedScrollView Order;
    LinearLayout noOrder;
    TextView bill,fees,totalbill;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper=new MKB_DB(getContext());
        Bundle bundle=getArguments();
        close=(ImageButton)view.findViewById(R.id.OrderClose);
        Order=(NestedScrollView)view.findViewById(R.id.Order);
        orderList=(RecyclerView)view.findViewById(R.id.menu_list);
        noOrder=(LinearLayout)view.findViewById(R.id.NoOrder);
        bill=(TextView)view.findViewById(R.id.Bill);
        fees=(TextView)view.findViewById(R.id.Fees);
        totalbill=(TextView)view.findViewById(R.id.TotalBill);
        OrderFragmentInterface listerner= new OrderFragmentInterface() {
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
                        totalbill.setText(String.valueOf(total_amount));
                    }
                }
            }
        };

        mAdapter = new CustomOrderAdapter(getContext(),orderArrayList,listerner);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderList.setLayoutManager(layoutManager);
        orderList.setItemAnimator(new DefaultItemAnimator());
        orderList.setAdapter(mAdapter);
        Log.d("mji","bundle PASS/NOT PASS");
        if(bundle!=null) {
            noOrder.setVisibility(View.GONE);
            Order.setVisibility(View.VISIBLE);
            Log.d("mji","bundle pass");
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
            totalbill.setText(String.valueOf(total_amount));
            //Log.d("mji",orderArrayList.get(0).getProduct());
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
        Log.d("mji","bundle not pass");
        dbHelper.close();
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
