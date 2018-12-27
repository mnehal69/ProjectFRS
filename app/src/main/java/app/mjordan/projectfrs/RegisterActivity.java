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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        server_url=getResources().getString(R.string.website)+"user/add.php" ;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.Background));
        UserEmail=(EditText) findViewById(R.id.REmail);
        UserName=(EditText) findViewById(R.id.RUName);
        UserPassword=(EditText) findViewById(R.id.RPass);
        closeBtn = (ImageButton) findViewById(R.id.close_btn);
        RegisterBtn=(Button)findViewById(R.id.Register);
        closeBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        userEmailLayout=(TextInputLayout) findViewById(R.id.REmailLayout);
        userNameLayout=(TextInputLayout) findViewById(R.id.RNameLayout);
        userPassLayout=(TextInputLayout) findViewById(R.id.RPassLayout);

        helperClass=new HelperClass(this);

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
        dbHelper = new MKB_DB(this);
    }

    public void fetch(final String Email, final String Name, final String Password) {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */
        final FragmentManager fm = getSupportFragmentManager();
        helperClass.load_Fragment(true,fm);
        if (helperClass.Check_Internet()){
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("zxc msg:", response);
                            // Display the first 500 characters of the response string.
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
                                    //Toast.makeText(getApplicationContext(),jObject.getString("Error"),Toast.LENGTH_SHORT).show();
                                    helperClass.Input_Error(UserEmail,jObject.getString("Error"),R.color.ErrorDialog);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("zxc",e.getMessage());
                            }
                            helperClass.load_Fragment(false,fm);                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("sadder error:", "That didn't work!");
                    helperClass.load_Fragment(false,fm);                }
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
            // Add the request to the RequestQueue.
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
                    fetch(email,name,password);
                }
                break;
        }
    }
}
