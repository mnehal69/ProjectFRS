package app.mjordan.projectfrs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Restaurent_List_Fragment extends ListFragment implements OnItemClickListener {
    private CustomListAdapter customListAdapter;
    private ArrayList<Rest_List> rest_list = new ArrayList<>();
    private Bundle bundle;
    private String res;
    private ListView list;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        bundle=getArguments();
        if(bundle!=null) {
            res = bundle.getString("Res");
        }
        return inflater.inflate(R.layout.restaurent_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (ListView) view.findViewById(R.id.list);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customListAdapter=new CustomListAdapter(getContext(),rest_list);
        setListAdapter(customListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}