package app.mjordan.projectfrs;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NameList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.nikartm.support.ImageBadgeView;

public class MenuActivity extends AppCompatActivity implements Counter.OnFragmentInteractionListener {
    String id,logo,background,url;
    ImageView backgroundimg,logoimg,back;
    HelperClass helperClass;
    RecyclerView menuList;
    LinearLayout noMenu;
    private ArrayList<Menu> menuArrayList=new ArrayList<>();
    MenuListAdapter mAdapter;
    int total_quantity;
    ImageBadgeView cart;
    private ArrayList<String> IDList=new ArrayList<>();
    private ArrayList<String> Namelist=new ArrayList<>();
    private ArrayList<Integer> itemlist=new ArrayList<>();
    private ArrayList<Integer> Pricelist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.loading_menu_fragment,new LoadingMain());
        ft.commit();

        DialogFragmentInterface listener =new DialogFragmentInterface() {
            @Override
            public void dialog_frag(Menu menu) {
                  final FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                  Bundle bundle=new Bundle();
                  Counter counter=new Counter();
                  bundle.putString("name",menu.getProduct());
                  bundle.putString("itemID",menu.getID());
                  bundle.putString("item",menu.getProduct());
                  bundle.putString("Price",menu.getPrice());
                  if (IDList.contains(menu.getID())){
                    int index=IDList.indexOf(menu.getID());
                    int value=itemlist.get(index);
                    bundle.putString("value", String.valueOf(value));
                }else{
                      bundle.putString("value", String.valueOf(0));
                }
                  counter.setArguments(bundle);
                  counter.show(fm, "loading_dialog");
            }};

        helperClass=new HelperClass(this);

        url=getResources().getString(R.string.website)+"res/menu_list.php";
        id=getIntent().getExtras().getString("ID");
        logo=getIntent().getExtras().getString("Logo");
        background=getIntent().getExtras().getString("Background");

        noMenu=(LinearLayout)findViewById(R.id.NoMenu);
        back=(ImageView) findViewById(R.id.back);
        logoimg=(ImageView)findViewById(R.id.rest_Logo);
        backgroundimg=(ImageView)findViewById(R.id.rest_background);
        cart=(ImageBadgeView)findViewById(R.id.cart);

        Picasso.get().load(logo).into(logoimg);
        Picasso.get().load(background).into(backgroundimg);

        menuList=(RecyclerView)findViewById(R.id.menu_list);
        mAdapter = new MenuListAdapter(this,menuArrayList,cart,listener);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        menuList.setLayoutManager(layoutManager);
        menuList.setItemAnimator(new DefaultItemAnimator());
        menuList.setAdapter(mAdapter);

        fetch();
        Gson gson= new Gson();

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                intent.putExtra("name",Namelist);
                intent.putExtra("id",IDList);
                intent.putExtra("item",itemlist);
                intent.putExtra("price",Pricelist);
                setResult(RESULT_OK,intent);
                finish();//finishing activity
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void fetch() {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */
        if (helperClass.Check_Internet()) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                            Log.d("cxz msg:", response);
                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean List=!(jObject.getBoolean("NoList"));
                                if(List){
                                    JSONObject jsonObject=jObject.getJSONObject("menu");
                                    JSONArray typearray=jsonObject.names();
                                    for(int i=0;i<typearray.length();i++){
                                        String type=typearray.get(i).toString();
                                        menuArrayList.add(new Menu(false,true,null,type,null,null,null,null));
                                        JSONArray subList=jsonObject.getJSONArray(type);
                                        for(int j=0;j<subList.length();j++){
                                            JSONObject subObj=subList.getJSONObject(j);
                                            boolean isDesc=subObj.getString("MDes").equals("null");
                                            String description=subObj.getString("MDes");
                                            menuArrayList.add(new Menu(!isDesc,false,subObj.getString("MID"),null,type,subObj.getString("Product"),description,subObj.getString("Price")));
                                        }
                                    }
                                    mAdapter.setMenuArrayList(menuArrayList);
                                }else{
                                    menuList.setVisibility(View.GONE);
                                    noMenu.setVisibility(View.VISIBLE);
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
            }) {

                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("RID",id);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{

        }
    }

    @Override
    public void UpdateOrder(String itemId,String name, int item,int price) {
            if (IDList.contains(itemId)) {
                if (item > 0) {
                int index = IDList.indexOf(itemId);
                itemlist.remove(index);
                Pricelist.remove(index);
                itemlist.add(index, item);
                Pricelist.add(index,price);
                Namelist.add(index,name);
                }
                if (item == 0) {
                    int index = IDList.indexOf(itemId);
                    IDList.remove(itemId);
                    itemlist.remove(index);
                    Pricelist.remove(index);
                    Namelist.remove(index);
                }
        }else {
                IDList.add(itemId);
                itemlist.add(item);
                Pricelist.add(price);
                Namelist.add(name);
            }

        total_quantity = 0;
        for (int i = 0; i < itemlist.size(); i++) {
            total_quantity = total_quantity + itemlist.get(i);
        }
        cart.setBadgeValue(total_quantity);
        Log.d("mlo", IDList.toString());
        Log.d("mlo", Namelist.toString());
        Log.d("mlo", itemlist.toString());
        Log.d("mlo", Pricelist.toString());
        }
    }
