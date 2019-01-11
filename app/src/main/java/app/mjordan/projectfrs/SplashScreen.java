package app.mjordan.projectfrs;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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

/**
 * <h1>SplashScreen</h1>
 * This activity is used to check if user is login or not
 * SplashScreen of this project
 * @author Mirza Nehal baig 19-10607
 * @author Osman Shaikh 19-10615
 * @version 1.0
 * @since 1/11/2018
 */
public class SplashScreen extends AppCompatActivity {
    //********************************************
    // Constants and variable
    //******************************************
    /**
     * Restful Api link
     */
    private String server_url;

    HelperClass helperClass;
    /**
        SharedPreferences used for day/night theme
     */
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Theme" ;

    // ***********************************************
    // OnCreate
    // ***********************************************

    /**
     * This is the main method of this activity in which everything is done in this method
     * @param savedInstanceState
     * Checking if login is with facebook by access token is in shared prefrences which is then used to retrieve user information
     *by Graph API and then checked if it's in Restful API of this app and if not by facebook, the id of the user
     * which is saved by user is fetched from local database and then by user id ,user information is retrieve through
     * online Restful API.
     */
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
            GraphRequest request =  GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {

                            if (response.getError() == null) {
                                String ID = me.optString("id");
                                String name= me.optString("name");
                                String email= me.optString("email");
                                    fb_server_fetch(ID,name,email);
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "picture.type(large),name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }else {
            server_url=getResources().getString(R.string.website)+"user/check.php";

            if (dbHelper.getCount() == 0) {
                Intent next = new Intent(this, Login.class); //This is used to move to next activity
                startActivity(next);
                finish();
            } else {
                fetch(dbHelper.get_UserLoggedID());
                dbHelper.close();
            }
        }
    }

    // ***********************************************
    //  Private Method
    // ***********************************************

    /**
     * fb_server_fetch is storing the information obtained from Graph API to online restful api using
     * @see Volley
     * by passing following parameters
     * @param id the facebook user id obtained from acess token by Graph API
     * @param name the facebook user name obtained from acess token by Graph API
     * @param email the facebook user email obtained from acess token by Graph API
     * to @literal server_url and then passing it to MainActivity using intent
     */
    private void fb_server_fetch(final String id, final String name, final String email) {

        final FragmentManager fm = getSupportFragmentManager();
        if (helperClass.Check_Internet()) {

            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            final StringRequest stringRequest = new StringRequest(Request.Method.POST,server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jObject  = new JSONObject(response);
                                boolean IsSuccess = jObject.getBoolean("IsFb");
                                if (IsSuccess) {
                                    JSONObject User = jObject.getJSONObject("User");
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
            queue.add(stringRequest);
        }
    }


    /**
     * Fetch is obtaining user information through
     * @see Volley
     * by passing the
     * @param UserID the userID stored in local Database
     * and then passing it to MainActivity using intent
     */
    private void fetch(final String UserID) {
        if (helperClass.Check_Internet()) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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
