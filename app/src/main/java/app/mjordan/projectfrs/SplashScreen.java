package app.mjordan.projectfrs;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    private String server_url;
    HelperClass helperClass;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Theme" ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        MKB_DB dbHelper = new MKB_DB(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Use to wake screen on this app


       Boolean night=sharedpreferences.getBoolean("NightTheme",false);

        if (night){
            window.setStatusBarColor(getResources().getColor(R.color.SplashBackground));
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
       }else{
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       }

        helperClass=new HelperClass(this);
        if(isLoggedIn){
            server_url=getResources().getString(R.string.website)+"user/fb_check_add.php";
            //String link="https://graph.facebook.com/me?fields=picture,email,name&access_token="+accessToken;
            //Log.d("zxc",link);
            GraphRequest request =  GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {

                            if (response.getError() == null) {
                                String ID = me.optString("id");
                                String name= me.optString("name");
                                String email= me.optString("email");
                                JSONObject picture =response.getJSONObject().optJSONObject("picture");

                                try {
                                    JSONObject data=picture.getJSONObject("data");
                                    String url=data.getString("url");
                                    Log.d("zxcv",ID+name+email+url);
                                    fb_server_fetch(ID,name,email,url);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "picture.type(large),name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }else {
            server_url=getResources().getString(R.string.website)+"user/check.php";
            Log.d("sadder", String.valueOf(dbHelper.getCount()));

            if (dbHelper.getCount() == 0) {
                Intent next = new Intent(this, Login.class); //This is used to move to next activity
                startActivity(next);
                finish();
            } else {
                Log.d("sadder USERID", dbHelper.get_UserLoggedID());
                fetch(dbHelper.get_UserLoggedID());
                dbHelper.close();
            }
        }
    }


    public void fb_server_fetch(final String id, final String name, final String email, final String picture) {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */
        Log.d("LoginUsing","yup");

        final FragmentManager fm = getSupportFragmentManager();
        if (helperClass.Check_Internet()) {
            // Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                            Log.d("zxcv",response);
                            try {
                                JSONObject jObject  = new JSONObject(response);
                                boolean IsSuccess = jObject.getBoolean("IsFb");
                                Log.d("zxcv", String.valueOf(IsSuccess));
                                if (IsSuccess) {
                                    JSONObject User = jObject.getJSONObject("User");
                                    Log.d("zxcv",User.toString());
                                    Intent main = new Intent(SplashScreen.this, MainActivity.class);
                                    main.putExtra("Type","User");
                                    main.putExtra("UserData",User.toString());
                                    main.putExtra("LoginUsing","FB");
                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong! Please Login again", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                    Log.d("zxcv error:", "That didn't work!");
                }
            }) {

                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("FID", id);
                    params.put("FEmail", email);
                    params.put("FName", name);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }






    public void fetch(final String UserID) {
        if (helperClass.Check_Internet()) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                            Log.d("sadder msg:", response);
                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean isLogged = jObject.getBoolean("isLogged");
                                if(isLogged) {
                                    JSONObject User = jObject.getJSONObject("User");
                                    Intent main = new Intent(SplashScreen.this, MainActivity.class);
                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    main.putExtra("Type","User");
                                    main.putExtra("UserData",User.toString());
                                    main.putExtra("LoginUsing","Email");
                                    startActivity(main);
                                }else{
                                    Intent intent=new Intent(SplashScreen.this,Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID",UserID);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            Intent intent=new Intent(SplashScreen.this,Login.class);
            startActivity(intent);
        }
    }
}
