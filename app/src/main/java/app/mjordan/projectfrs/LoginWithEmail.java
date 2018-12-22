package app.mjordan.projectfrs;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginWithEmail extends AppCompatActivity implements View.OnClickListener {
    EditText username,password;
    ImageButton closeBtn;
    Button ForgetBtn,RegisterBtn,LoginBtn;
    String user,pass,server_url="http://192.168.10.4:8081/FRS/user/read.php";
    TextInputLayout userEmailLayout,PassLayout;
    Fragment prev;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.Background));

        username = (EditText) findViewById(R.id.UName);
        password=(EditText) findViewById(R.id.PName);
        closeBtn = (ImageButton) findViewById(R.id.close_btn);
        ForgetBtn=(Button)findViewById(R.id.ForgetPass);
        RegisterBtn=(Button) findViewById(R.id.RegisterLogin);
        LoginBtn=(Button)findViewById(R.id.LoginBtn);
        userEmailLayout=(TextInputLayout) findViewById(R.id.UserEmailLayout);
        PassLayout=(TextInputLayout) findViewById(R.id.PassLayout);
        /*username.requestFocus();*/
        closeBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
        prev = getFragmentManager().findFragmentByTag("loading_dialog");
        loadingDialog = new LoadingDialog();
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    userEmailLayout.setErrorEnabled(false);;
                    username.getBackground().setColorFilter(getResources().getColor(R.color.Card), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    PassLayout.setErrorEnabled(false);
                    password.getBackground().setColorFilter(getResources().getColor(R.color.Card), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }



    boolean Check_Input(){
        /*
        * THIS CHECK IF BOTH THE FIELD IS NOT EMPTY
        * AND SETTING THE ERROR AND COLOR OF BACKGROUND TINT AS RED
        * */
        user=username.getText().toString().trim();
        pass=password.getText().toString().trim();
        if(user.equals("") && pass.equals("") ){

            username.setError(" Username Field Empty");
            password.setError(" Password Field Empty");
            username.getBackground().setColorFilter(getResources().getColor(R.color.ErrorDialog), PorterDuff.Mode.SRC_ATOP);
            password.getBackground().setColorFilter(getResources().getColor(R.color.ErrorDialog), PorterDuff.Mode.SRC_ATOP);
            return false;
        }else {
            if (user.equals("")) {
               username.setError(" Username Field Empty");
                username.getBackground().setColorFilter(getResources().getColor(R.color.ErrorDialog), PorterDuff.Mode.SRC_ATOP);
                return false;
            }
            if (pass.equals("")) {
                password.setError(" Password Field Empty");
                password.getBackground().setColorFilter(getResources().getColor(R.color.ErrorDialog), PorterDuff.Mode.SRC_ATOP);
                return false;
            }
        }
        return true;
    }


     public void fetch() {
         /*
          *
          * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
          * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
          * */
         if (prev == null) {
             //STARTING THE LOADING_DIALOG DIALOG FRAGMENT IN WHICH
             // THERE IS A PROGRESS BAR WHICH SHOWED FOR PROCESS
             FragmentManager fm = getSupportFragmentManager();
             loadingDialog.show(fm, "loading_dialog");
             Log.d("sadder", "load");
         }
         if (Check_Internet()) {
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
                                 boolean notFound = jObject.getBoolean("IsEmpty");
                                 if (!notFound) {
                                     boolean samePass = jObject.getBoolean("SamePass");
                                     if (!samePass) {
                                         password.setError("Incorrect Password");
                                         password.getBackground().setColorFilter(getResources().getColor(R.color.ErrorDialog), PorterDuff.Mode.SRC_ATOP);
                                     } else {
                                         //Log.d("sadder yeah", String.valueOf(jObject.getJSONObject("User")));
                                         JSONObject User = jObject.getJSONObject("User");
                                         Intent main = new Intent(LoginWithEmail.this, MainActivity.class);

                                         startActivity(main);
                                     }

                                 } else {
                                     Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                                 }

                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                             loadingDialog.dismiss();
                         }
                     }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                     Log.d("sadder error:", "That didn't work!");
                     loadingDialog.dismiss();
                 }
             }) {

                 @Override
                 protected Map<String, String> getParams() throws AuthFailureError {
                     Map<String, String> params = new HashMap<>();
                     params.put("UEmail", user);
                     params.put("Pass", pass);
                     return params;
                 }
             };
             // Add the request to the RequestQueue.
             queue.add(stringRequest);
         }else{
             loadingDialog.dismiss();
         }
     }
    void ClearFocus(){
        /*
        * This fuction is used to unfocus both edit text and called when user
        * click login button in LoginWithEmail Screen
        *
         */
        username.clearFocus();
        password.clearFocus();
    }
    public boolean Check_Internet(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        if(!connected) {
            Toast.makeText(getApplicationContext(), "No Internet Connected", Toast.LENGTH_SHORT).show();
        }
        return connected;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LoginBtn:
               boolean checked=Check_Input();
                if (checked) {
                    ClearFocus();
                    fetch();
                } else {
                    Log.d("sadder", "nope send");
                }
                break;
            case R.id.close_btn:
                finish();
                break;
            case R.id.ForgetPass:
                break;
            case R.id.RegisterLogin:
                //GOING TO REGISTERActivity
                Intent nextActivity=new Intent(this,RegisterActivity.class);
                startActivity(nextActivity);
                break;
        }
    }
}
