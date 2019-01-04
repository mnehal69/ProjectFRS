package app.mjordan.projectfrs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link Eat.OnFragmentInteractionListener} interface
// * to handle interaction events.
// */
public class Eat extends Fragment {
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ListView list;
    private ArrayList<Rest_List> rest_list = new ArrayList<>();
    private PopularAdapter mAdapter;
    private Bundle bundle;
    private String res;
    private int MENU_ACTIVITY_ORDER=2;
    public Eat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle=getArguments();
        if(bundle!=null) {
            res = bundle.getString("Res");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray obj = new JSONArray(res);
                    int i,size=obj.length();
                    for(i=0;i<size;i++){
                        JSONObject jobj= (JSONObject) obj.get(i);
                        rest_list.add(new Rest_List(jobj.getString("RID"),getResources().getString(R.string.website)+jobj.getString("RLogo"),jobj.getString("RName"),jobj.getBoolean("Popular"),getResources().getString(R.string.website)+jobj.getString("RBackground"),jobj.getString("RDest"),jobj.getString("Location"),jobj.getString("RTime"),jobj.getString("Rating")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        PopularItemClickListener listener = new PopularItemClickListener() {
            @Override
            public void onItemClick(Rest_List restList) {
                Intent menu =new Intent(getApplicationContext(),MenuActivity.class);
                menu.putExtra("ID",restList.getId());
                menu.putExtra("Logo",restList.getLogo());
                menu.putExtra("Background",restList.getBackground());
                startActivityForResult(menu,MENU_ACTIVITY_ORDER);
            }
        };

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            mAdapter = new PopularAdapter(listener,rest_list);
            LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            final FragmentTransaction ft= getFragmentManager().beginTransaction();
            Restaurent_List_Fragment list =new Restaurent_List_Fragment();
           Bundle bundle=new Bundle();
          bundle.putString("Res",res);
           list.setArguments(bundle);
            ft.add(R.id.ListFragment,list);
            ft.commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MENU_ACTIVITY_ORDER && resultCode==RESULT_OK){
            if(data!=null){
                ArrayList<String> id= (ArrayList<String>)data.getStringArrayListExtra("id");
                ArrayList<Integer> item=(ArrayList<Integer>)data.getExtras().getIntegerArrayList("item");
                ArrayList<String> name=(ArrayList<String>)data.getExtras().getStringArrayList("name");
                ArrayList<Integer> price=(ArrayList<Integer>)data.getExtras().getIntegerArrayList("price");
                mListener.BottomNavChangeTav(1);
                mListener.OrderFragment(id,name,item,price);
            }
        }
    }



    public interface RecyclerViewClickListener {
        void onClick(View view, int position);

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
        void BottomNavChangeTav(int tab);
        void OrderFragment(ArrayList<String> Id_list,ArrayList<String> NameList,ArrayList<Integer> itemList,ArrayList<Integer> priceList);
    }
}
