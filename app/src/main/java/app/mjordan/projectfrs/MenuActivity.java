package app.mjordan.projectfrs;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    String id,logo,background,url;
    ImageView backgroundimg,logoimg;
    HelperClass helperClass;
    RecyclerView menuList;
    private ArrayList<Menu> menuArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.loading_menu_fragment,new LoadingMain());
        ft.commit();


        helperClass=new HelperClass(this);
        url=getResources().getString(R.string.website)+"res/menu_list.php";
        id=getIntent().getExtras().getString("ID");
        logo=getIntent().getExtras().getString("Logo");
        background=getIntent().getExtras().getString("Background");
        logoimg=(ImageView)findViewById(R.id.rest_Logo);
        backgroundimg=(ImageView)findViewById(R.id.rest_background);
        Picasso.get().load(logo).into(logoimg);
        Picasso.get().load(background).into(backgroundimg);
        menuList=findViewById(R.id.menu_list);
        fetch();
        //Intent intent=new Intent(this,MainActivity.class);
        //intent.putExtra("zxc","zxc321");
        //setResult(RESULT_OK,intent);
        //finish();//finishing activity
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
                                        Log.d("bnm",typearray.get(i).toString());
                                    }
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
}
