package app.mjordan.projectfrs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Counter.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Counter extends DialogFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    int item,itemPrice;
    TextView title,quantity,price;
    ImageView add,remove,close;
    String Id;
    String itemString,name;
    int total;
    boolean changed=false;

    /**
     * This DialogFragment show a counter with increase and decrease button by which
     * the food quantity can be increased or decreased and at the same time it is showing
     * the price of that specific item and with quantity i.e 2 X RS 300 = 600
     */
    public Counter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_counter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Bundle bundle =getArguments();
            add= view.findViewById(R.id.AddBtn);
            remove= view.findViewById(R.id.RemoveBtn);
            close= view.findViewById(R.id.close_btn);
            title= view.findViewById(R.id.title);
            price= view.findViewById(R.id.PriceShow);
            quantity= view.findViewById(R.id.Quantity);
            if (bundle != null) {
                title.setText(bundle.getString("item","null"));
                item=Integer.parseInt(bundle.getString("value","0"));
                quantity.setText(String.valueOf(item));
                Id=bundle.getString("itemID","null");
                name=bundle.getString("name","null");
                itemPrice=Integer.parseInt(bundle.getString("Price","0"));
            }

            add.setOnClickListener(this);
            remove.setOnClickListener(this);
            close.setOnClickListener(this);

            if(item>0){
                total=item*itemPrice;
                itemString=String.valueOf(item)+" X Rs"+String.valueOf(itemPrice)+" = Rs"+String.valueOf(total);
                price.setText(itemString);
            }
    }

    /**
     * onClick function is used for three things
     * AddBtn:
     *      this button is increasing the quantity and at the same time total price of that item with
     *      quantity is shown
     *  RemoveBtn:
     *      this button is decreasing the quantity and at the same time total price of that item with
     *      quantity is shown
     *  CloseBtn:
     *      it is checking if the quantity isn't zero of the item and if it isn't
     *      it is updating the orderList by passing as parameters to
     *      @see OnFragmentInteractionListener'S Update Order.
     * @param view the pressed view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AddBtn:
                changed=true;
                item=item+1;
                quantity.setText(String.valueOf(item));
                break;
            case R.id.RemoveBtn:
                changed=true;
                item=item-1;
                if(item<=0){
                    item=0;
                }
                quantity.setText(String.valueOf(item));
                break;
            case R.id.close_btn:
                changed = item == 0 || Integer.parseInt(quantity.getText().toString()) == 0;
                mListener.UpdateOrder(changed,Id,name,item,itemPrice);
                dismiss();
                break;
        }

        if(item>0) {
            total = item * itemPrice;
            itemString = String.valueOf(item) + " X Rs" + String.valueOf(itemPrice) + " = Rs" + String.valueOf(total);
            price.setText(itemString);

        }else {
            price.setText("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void UpdateOrder(boolean changed,String itemId,String name,int item,int price);
    }
}
