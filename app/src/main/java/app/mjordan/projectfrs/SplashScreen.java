package app.mjordan.projectfrs;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    private String server_url;
    HelperClass helperClass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MKB_DB dbHelper = new MKB_DB(this);
        Window window = getWindow();
        server_url=getResources().getString(R.string.website)+"user/check.php";
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Use to wake screen on this app
        helperClass=new HelperClass(this);
        Log.d("sadder", String.valueOf(dbHelper.getCount()));

            if (dbHelper.getCount() == 0) {
                Intent next = new Intent(this, Login.class); //This is used to move to next activity
                startActivity(next);
                finish();
            } else {
                Log.d("sadder USERID", dbHelper.get_UserLoggedID());
                fetch(dbHelper.get_UserLoggedID());
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
                    if(error!=null) {
                        Log.d("sadder error:", error.getMessage().toString());
                    }
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
