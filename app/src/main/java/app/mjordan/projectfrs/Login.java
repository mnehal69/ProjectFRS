
package app.mjordan.projectfrs;

import android.content.Intent;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements View.OnClickListener {

    Button FacebookLogin,EmailSignUp,GuessBtn;
    LoginButton fbLogin;
    CallbackManager callbackManager;
    HelperClass helperClass;
    String server_url;

    /**This is the main method of this activity in which everything is done in this method
     * @param savedInstanceState
     * <h1>Login Activity</h1>
     * <pre>This activity provides user with login option
     * Login with Facebook
     * SignIn With Email
     * As a Guest (Not completed thoroughly but guest can only see menu and reviews
     * /pre>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        server_url=getResources().getString(R.string.website)+"user/fb_check_add.php";
        helperClass=new HelperClass(this);
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
                //https://graph.facebook.com/me?fields=picture,email,name&access_token="+accessToken
                GraphRequest request =  GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {

                                if (response.getError() == null){
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
    // ********************************************
    // Private Method
    // ********************************************

    /**This method is used to saved information provided by
     * @see FacebookCallback
     * and
     * @see GraphRequest
     * to Restful API using
     *  @see Volley
     * by passing following parameters
     * @param id  the facebook user id
     * @param name the facebook user name
     * @param email the facebook user email
     * to server link
     *
     */
    private void fb_server_fetch(final String id, final String name, final String email) {
        final FragmentManager fm = getSupportFragmentManager();
        helperClass.load_Fragment(true, fm);
        if (helperClass.Check_Internet()) {
            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean IsSuccess = jObject.getBoolean("IsFb");
                                if (IsSuccess) {
                                    JSONObject User = jObject.getJSONObject("User");
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
                    helperClass.load_Fragment(false, fm);
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
        } else {
            helperClass.load_Fragment(false, fm);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email:
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
