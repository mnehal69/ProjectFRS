package app.mjordan.projectfrs;


import android.content.Intent;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton closeBtn;
    Button RegisterBtn;
    EditText UserName,UserEmail,UserPassword;
    String server_url;
    TextInputLayout userNameLayout,userEmailLayout,userPassLayout;
    MKB_DB dbHelper;
    HelperClass helperClass;
    /**This is the main method of this activity in which everything is done in this method
     * @param savedInstanceState
     * <h1>Register Activity</h1>
     * <p> This activity allow user to register user</p>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        helperClass=new HelperClass(this);
        dbHelper = new MKB_DB(this);

        server_url=getResources().getString(R.string.website)+"user/add.php" ;

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.Background));

        UserEmail= findViewById(R.id.REmail);
        UserName= findViewById(R.id.RUName);
        UserPassword= findViewById(R.id.RPass);
        closeBtn = findViewById(R.id.close_btn);
        RegisterBtn= findViewById(R.id.Register);

        userEmailLayout= findViewById(R.id.REmailLayout);
        userNameLayout= findViewById(R.id.RNameLayout);
        userPassLayout= findViewById(R.id.RPassLayout);

        closeBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);


        UserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    helperClass.Input_Error(UserEmail,null,R.color.ErrorDialog);
                }
            }
        });

        UserPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    helperClass.Input_Error(UserPassword,null,R.color.ErrorDialog);
                }
            }
        });
        UserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    helperClass.Input_Error(UserName,null,R.color.ErrorDialog);
                }
            }
        });

    }

    /**
     * Register method pass these parameters
     * @param Email the email provided by user through EditText
     * @param Name  the name provided by user through EditText
     * @param Password the password provided by user through EditText
     * to Restful api (server_url)
     * through
     * @see Volley
     */
    private void register(final String Email, final String Name, final String Password) {
        final FragmentManager fm = getSupportFragmentManager();
        helperClass.load_Fragment(true,fm);

        if (helperClass.Check_Internet()){

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean isRegister = jObject.getBoolean("IsRegister");
                                if(isRegister) {
                                    JSONObject User = jObject.getJSONObject("User");
                                    dbHelper.Insert_IsLogged(User.getString("ID"));
                                    Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                                    main.putExtra("UserData",User.toString());
                                    startActivity(main);
                                }else{
                                    Log.d("zxc","NOT REGISTER");
                                    helperClass.Input_Error(UserEmail,jObject.getString("Error"),R.color.ErrorDialog);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            helperClass.load_Fragment(false,fm);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong!Please try to register again!", Toast.LENGTH_SHORT).show();
                    helperClass.load_Fragment(false,fm);
                }
            }) {

                @Override
                protected Map<String, String> getParams()  {
                    Map<String, String> params = new HashMap<>();
                    params.put("Name",Name);
                    params.put("Email",Email);
                    params.put("Password",Password);
                    return params;
                }
            };

            queue.add(stringRequest);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                finish();
                break;
            case R.id.Register:
                String name=UserName.getText().toString().trim();
                String email=UserEmail.getText().toString().trim();
                String password=UserPassword.getText().toString().trim();
                boolean checked=helperClass.CheckEmpty(UserEmail) || helperClass.CheckEmpty(UserName) || helperClass.CheckEmpty(UserPassword);
                if(!(checked)){
                    register(email,name,password);
                }
                break;
        }
    }
}
