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
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //Order.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Order extends Fragment {

   // private OnFragmentInteractionListener mListener;
    ArrayList<String> ItemId,NameList;
    ArrayList<Integer>quantityList,priceList;
    RecyclerView orderList;
    NestedScrollView Order;
    LinearLayout noOrder;
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
        Bundle bundle=getArguments();
        Order=(NestedScrollView)view.findViewById(R.id.Order);
        orderList=(RecyclerView)view.findViewById(R.id.menu_list);
        noOrder=(LinearLayout)view.findViewById(R.id.NoOrder);
        mAdapter = new CustomOrderAdapter(getContext(),orderArrayList);
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
            }
            Log.d("mji",orderArrayList.get(0).getProduct());
            mAdapter.setOrder(orderArrayList);

        }
        Log.d("mji","bundle not pass");

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
