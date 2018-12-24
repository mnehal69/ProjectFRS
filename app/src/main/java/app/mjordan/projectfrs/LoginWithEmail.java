package app.mjordan.projectfrs;
import android.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.content.Intent;

import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class LoginWithEmail extends AppCompatActivity implements View.OnClickListener {
    EditText username,password;
    ImageButton closeBtn;
    Button ForgetBtn,RegisterBtn,LoginBtn;
    String server_url;
    TextInputLayout userEmailLayout,PassLayout;
    Fragment prev;
    LoadingDialog loadingDialog;
    MKB_DB dbHelper;
    HelperClass helperClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        server_url=getResources().getString(R.string.website)+"user/read.php";

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.Background));

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
        ForgetBtn.setOnClickListener(this);


         helperClass=new HelperClass(this);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    helperClass.Input_Error(username,null,R.color.Card);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    helperClass.Input_Error(password,null,R.color.Card);
                }
            }
        });

        dbHelper = new MKB_DB(this);
    }






     public void fetch(final String user, final String pass) {
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
             StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                     new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             // Display the first 500 characters of the response string.

                             //Log.d("sadder msg:", response);
                             try {
                                 JSONObject jObject = new JSONObject(response);
                                 boolean notFound = jObject.getBoolean("IsEmpty");
                                 if (!notFound) {
                                     boolean samePass = jObject.getBoolean("SamePass");
                                     if (!samePass) {
                                         helperClass.Input_Error(password,"Incorrect Password",R.color.ErrorDialog);
                                     } else {

                                         JSONObject User = jObject.getJSONObject("User");
                                         dbHelper.Insert_IsLogged(User.getString("ID"));
                                         Intent main = new Intent(LoginWithEmail.this, MainActivity.class);
                                         main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         startActivity(main);
                                     }

                                 } else {
                                     helperClass.Input_Error(username,"Invalid Email",R.color.ErrorDialog);
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
                     params.put("UEmail", user);
                     params.put("Pass", pass);
                     return params;
                 }
             };
             // Add the request to the RequestQueue.
             queue.add(stringRequest);
         }else{
             helperClass.load_Fragment(false,fm);
         }
     }
    void ClearFocus(EditText editText){
        /*
        * This fuction is used to unfocus both edit text and called when user
        * click login button in LoginWithEmail Screen
        *
         */
        editText.clearFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LoginBtn:
               boolean checked=!(helperClass.CheckEmpty(username) || helperClass.CheckEmpty(password));

               if (checked) {
                    ClearFocus(username);
                    ClearFocus(password);
                    String user=username.getText().toString();
                    String pass=password.getText().toString();
                    fetch(user,pass);
                }
                break;
            case R.id.close_btn:
                finish();
                break;
            case R.id.ForgetPass:
                Intent intent=new Intent(this,ForgetPassword.class);
                startActivity(intent);
                break;
            case R.id.RegisterLogin:
                //GOING TO REGISTERActivity
                Intent nextActivity=new Intent(this,RegisterActivity.class);
                startActivity(nextActivity);
                break;
        }
    }
}
