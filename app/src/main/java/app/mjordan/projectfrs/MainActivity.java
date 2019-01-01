package app.mjordan.projectfrs;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavBar.OnBottomNavListerner,ImageChoice.OnImageChoiceListerner,Profile.OnFragmentInteractionListener {
    MKB_DB dbHelper;
    HelperClass helperClass;
    FragmentTransaction ft;
    boolean obtainList=false;
    String type,json,list,server_url,res,popular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MKB_DB(this);
        helperClass=new HelperClass(this);
        server_url=getResources().getString(R.string.website)+"res/eat_list.php";
        type=getIntent().getExtras().getString("Type","Guest");
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        json = getIntent().getExtras().getString("UserData",null);
        Profile profile=new Profile();
        Bundle bundle=new Bundle();
        list="List";
        bundle.putString("Type",type);
        bundle.putString("json",json);
        bundle.putString("ListType",list);
        profile.setArguments(bundle);
        ft.add(R.id.TabFragment,profile);
        ft.commit();
        PersmissionUtils.checkAndRequestPermissions(MainActivity.this);
    }

    public void fetch_list() {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */
        final FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        if (helperClass.Check_Internet()) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("zxc msg:", response);
                            try {
                                JSONObject jObject = new JSONObject(response);
                                obtainList =!(jObject.getBoolean("NoList"));
                                if(obtainList){
                                    res=String.valueOf(jObject.getJSONArray("restaurent"));
                                    popular=String.valueOf(jObject.getJSONArray("Popular"));
                                    Eat eat =new Eat();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("Res",res);
                                    bundle.putString("Popular",popular);
                                    ft.replace(R.id.TabFragment, eat);
                                    ft.commit();
                                }else{
                                    res=null;
                                    popular=null;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    //Log.d("sadder error:", "That didn't work!");
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

    }

    @Override
    public void fragment(int n) {
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        switch(n){
            case 1:

                ft.replace(R.id.TabFragment,new Order());
                Log.d("zxc","CASE1");
                break;
            case 2:

                Log.d("zxc","CASE2");
                ft.replace(R.id.TabFragment,new LoadingMain());
                fetch_list();
                break;
            case 3:
                Log.d("zxc","CASE3");
                Profile profile=new Profile();
                Bundle bundle=new Bundle();
                list="List";
                bundle.putString("Type",type);
                bundle.putString("json",json);
                bundle.putString("ListType",list);
                profile.setArguments(bundle);
                ft.replace(R.id.TabFragment,profile);
                break;
        }
        ft.commit();
    }
    @Override
    public void image_select( Uri uri) {
        Profile frag = (Profile)
                getSupportFragmentManager().findFragmentById(R.id.TabFragment);
        if(uri!=null){
            frag.setImage(uri);
        }
    }

    public boolean sameUserValue(User user,User another){
        if(!another.Name.equals(user.Name)){
            return false;
        }
        if(!another.Address.equals(user.Address)){
            return false;
        }
        if(!another.Contact.equals(user.Contact)){
            return false;
        }
        return true;
    }

    @Override
    public void Done(User user, User userData) {

        if(!sameUserValue(user,userData)) {
            Gson gson = new Gson();
            //String json = gson.toJson(userData);
            update(userData);
        }else{
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            list="List";
            Profile profile=new Profile();
            Bundle bundle=new Bundle();
            bundle.putString("Type",type);
            bundle.putString("json",json);
            bundle.putString("ListType",list);
            profile.setArguments(bundle);
            ft.replace(R.id.TabFragment,profile);
            ft.commit();
        }

    }


    public void update(final User user) {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */

        final FragmentManager fm = getSupportFragmentManager();
        helperClass.load_Fragment(true,fm);
        if (helperClass.Check_Internet()) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.website)+"user/update_user.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                            Log.d("sadder msg:", response);
                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean update = jObject.getBoolean("IsUpdate");
                                if (update) {
                                    Gson gson=new Gson();
                                        json=gson.toJson(user);
                                    FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                                    list="List";
                                    Profile profile=new Profile();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("Type",type);
                                    bundle.putString("json",json);
                                    bundle.putString("ListType",list);
                                    profile.setArguments(bundle);
                                    ft.replace(R.id.TabFragment,profile);
                                    ft.commit();
                                    }else{
                                    Toast.makeText(getApplicationContext(),jObject.getString("Error"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            helperClass.load_Fragment(false,fm);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    //Log.d("sadder error:", "That didn't work!");
                    helperClass.load_Fragment(false,fm);
                }
            }) {

                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("Name", user.Name);
                    params.put("ID", user.ID);
                    params.put("Contact", user.Contact);
                    params.put("Address", user.Address);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            helperClass.load_Fragment(false,fm);
        }
    }

}
