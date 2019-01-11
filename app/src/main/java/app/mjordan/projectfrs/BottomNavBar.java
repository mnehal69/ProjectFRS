package app.mjordan.projectfrs;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomNavBar.OnBottomNavListerner} interface
 * to handle interaction events.
 * This is custom bottom navigation which is used in MainActivity
 * In Landscape orientation only two tab will be shown tab2,tab3 and
 * In portrait orientation all tab is shown
 */
public class BottomNavBar extends Fragment implements View.OnClickListener {
    OnBottomNavListerner mListener;
    LinearLayout tab1,tab2,tab3;
    boolean portrait=true;


    public BottomNavBar(){}


    interface OnBottomNavListerner{
        void fragment(int n );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnBottomNavListerner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnBottomNavListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bottom_nav,container,false);
        tab2= view.findViewById(R.id.Tab_2);
        tab3=view.findViewById(R.id.Tab_3);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            portrait=true;
            tab1= view.findViewById(R.id.Tab_1);
            tab1.setOnClickListener(this);
        }else {
            portrait = false;
        }
        return view;
    }

    /**
     * Helper function to change icon of the Custom bottomNavBar
     * @param tab the layout in which bottomNavBar
     * @param i the tab which is selected
     */
    public void Tab_Icon(LinearLayout tab,int i){
        ImageView imageView;
        imageView = (ImageView) tab.getChildAt(0);
        imageView.setImageDrawable(getResources().getDrawable(i));
    }

    /**Manual Click
     * TabChanged is used to manually changed BottomNavBar
     * *NOTE* This work like a actual BottomNavigationBar, only difference is
     * it's selected icon is colorful
     * @param i the tab which is selected
     */
    public void TabChanged(int i) {
        if (portrait) {
            Tab_Icon(tab1, R.drawable.money_silver);
        }
        Tab_Icon(tab2,R.drawable.pizza_silver);
        Tab_Icon(tab3,R.drawable.face_silver);
        switch (i){
            case 1:
                Tab_Icon(tab1,R.drawable.money);

                break;
            case 2:
                Tab_Icon(tab2,R.drawable.pizza);
                break;
            case 3:
                Tab_Icon(tab3,R.drawable.face);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        if (portrait) {
            Tab_Icon(tab1, R.drawable.money_silver);
        }
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
