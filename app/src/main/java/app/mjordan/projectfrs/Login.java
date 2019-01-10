package app.mjordan.projectfrs;


import android.content.Intent;
import android.graphics.Color;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button FacebookLogin,EmailSignUp,GuessBtn;
    LoginButton fbLogin;
    CallbackManager callbackManager;
    HelperClass helperClass;
    ArrayList<String> fb_user=new ArrayList<>();
    String server_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        server_url=getResources().getString(R.string.website)+"user/fb_check_add.php";
        helperClass=new HelperClass(this);
        //SETTING THE COLOR OF THE STATUS BAR
        //Window window = getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(Color.parseColor("#243b55"));
        //INTIALIZING THE BUTTON
        fbLogin=findViewById(R.id.login_button);
        FacebookLogin= findViewById(R.id.facebook);
        EmailSignUp= findViewById(R.id.email);
        GuessBtn= findViewById(R.id.Guest);
        EmailSignUp.setOnClickListener(this);
        GuessBtn.setOnClickListener(this);
        FacebookLogin.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken =loginResult.getAccessToken().getToken();
                String link="https://graph.facebook.com/me?fields=picture,email,name&access_token="+accessToken;
                Log.d("zxc",link);
                GraphRequest request =  GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {

                                if (response.getError() == null){

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
                //fetch_fb(link);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    public void fb_server_fetch(final String id, final String name, final String email, final String picture) {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */

        final FragmentManager fm = getSupportFragmentManager();
        helperClass.load_Fragment(true, fm);
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
                                JSONObject jObject = new JSONObject(response);
                                boolean IsSuccess = jObject.getBoolean("IsFb");
                                Log.d("zxcv", String.valueOf(IsSuccess));
                                if (IsSuccess) {
                                    JSONObject User = jObject.getJSONObject("User");
                                    Log.d("zxcv",User.toString());
                                    Intent main = new Intent(Login.this, MainActivity.class);
                                    main.putExtra("Type","User");
                                    main.putExtra("UserData",User.toString());
                                    main.putExtra("LoginUsing","FB");
                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            helperClass.load_Fragment(false, fm);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong! Please Login again", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                    Log.d("zxcv error:", "That didn't work!");
                    helperClass.load_Fragment(false, fm);
                }
            }) {

                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("FID", id);
                    params.put("FEmail", email);
                    params.put("FName", name);
                    params.put("FAvatar",picture);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            helperClass.load_Fragment(false, fm);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email:
                //GOING TO NEXT ACTIVITY
            Intent EmailActivity = new Intent(getApplicationContext(), LoginWithEmail.class);
            startActivity(EmailActivity);
            break;
            case R.id.facebook:
                fbLogin.performClick();
                break;
            case R.id.Guest:
                Intent main = new Intent(Login.this, MainActivity.class);
                main.putExtra("Type","Guest");
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
