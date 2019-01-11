package app.mjordan.projectfrs;


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
    MKB_DB dbHelper;
    HelperClass helperClass;

    /**This is the main method of this activity in which everything is done in this method
     * @param savedInstanceState
     * <h1>Login with Email Activity</h1>
     * <pre> This activity provide authorization to registered user and
     * provide option to register and recover password if forget
     * </pre>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        server_url=getResources().getString(R.string.website)+"user/read.php";

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.Background));

        username = findViewById(R.id.UName);
        password= findViewById(R.id.PName);
        closeBtn = findViewById(R.id.close_btn);
        ForgetBtn= findViewById(R.id.ForgetPass);
        RegisterBtn= findViewById(R.id.RegisterLogin);
        LoginBtn= findViewById(R.id.LoginBtn);
        userEmailLayout= findViewById(R.id.UserEmailLayout);
        PassLayout= findViewById(R.id.PassLayout);


        closeBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
        ForgetBtn.setOnClickListener(this);

        helperClass=new HelperClass(this);
        dbHelper = new MKB_DB(this);


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


    }





    /**
     * Fetch method is checking if user is authorized or not through
     * @see Volley
     * by passing the
     * @param user the userName or UserEmail provided by user through EditText
     * @param pass the password of the account
     * to server_url and if user is authorized, this method is saving user id to local database and
     *  passing it to MainActivity through
     * @see Intent
     */
     private void fetch(final String user, final String pass) {
         final FragmentManager fm = getSupportFragmentManager();
         helperClass.load_Fragment(true,fm);
         if (helperClass.Check_Internet()) {
             RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
             StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                     new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             try {
                                 JSONObject jObject = new JSONObject(response);
                                 boolean notFound = jObject.getBoolean("IsEmpty");
                                 if (!notFound) {

                                     boolean samePass = jObject.getBoolean("SamePass");

                                     if (!samePass) {
                                         helperClass.Input_Error(password,"Incorrect Password",R.color.ErrorDialog);
                                        }
                                        else {

                                             JSONObject User = jObject.getJSONObject("User");
                                             dbHelper.Insert_IsLogged(User.getString("ID"));
                                             Intent main = new Intent(LoginWithEmail.this, MainActivity.class);
                                             main.putExtra("Type","User");
                                             main.putExtra("LoginUsing","Email");
                                             main.putExtra("UserData",User.toString());
                                             main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                             startActivity(main);
                                             dbHelper.close();
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
             queue.add(stringRequest);
         }else{
             helperClass.load_Fragment(false,fm);
         }
     }
    void ClearFocus(EditText editText){
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
                Intent nextActivity=new Intent(this,RegisterActivity.class);
                startActivity(nextActivity);
                break;
        }
    }
}
