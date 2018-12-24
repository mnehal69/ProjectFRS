package app.mjordan.projectfrs;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BottomNavBar extends Fragment implements View.OnClickListener {
    OnBottomNavListerner mListener;
    LinearLayout tab1,tab2,tab3;
    public BottomNavBar(){}


    interface OnBottomNavListerner{
        public void fragment(int n );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnBottomNavListerner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnBottomNavListerner");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bottom_nav,container,false);
        tab1=(LinearLayout) view.findViewById(R.id.Tab_1);
        tab2=(LinearLayout) view.findViewById(R.id.Tab_2);
        tab3=(LinearLayout) view.findViewById(R.id.Tab_3);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        return view;
    }

    public void Tab_Icon(LinearLayout tab,int i){
        ImageView imageView;
        imageView = (ImageView) tab.getChildAt(0);
        imageView.setImageDrawable(getResources().getDrawable(i));
    }
    @Override
    public void onClick(View view) {
        Tab_Icon(tab1,R.drawable.money_silver);
        Tab_Icon(tab2,R.drawable.pizza_silver);
        Tab_Icon(tab3,R.drawable.face_silver);
        switch (view.getId()){
            case R.id.Tab_1:
                Tab_Icon(tab1,R.drawable.money);
                mListener.fragment(1);
                break;
            case R.id.Tab_2:
                Tab_Icon(tab2,R.drawable.pizza);
                mListener.fragment(2);
                break;
            case R.id.Tab_3:
                Tab_Icon(tab3,R.drawable.face);
                mListener.fragment(3);
                break;
        }
    }
}
