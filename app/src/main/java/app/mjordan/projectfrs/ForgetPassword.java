package app.mjordan.projectfrs;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    EditText Email,Code,Pass,ConfirmPass;
    TextInputLayout FEmail,FCode,FPass,FConfirmPass;
    LinearLayout EmailView,CodeView,PassView;
    Button ForgetBtn;
    String server_url;
    ImageButton closeBtn;
    HelperClass helperClass;
    String MasterCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Email=(EditText) findViewById(R.id.FEmail);
        Code=(EditText)findViewById(R.id.FCode);
        Pass=(EditText)findViewById(R.id.FPass);
        ConfirmPass=(EditText) findViewById(R.id.FConfirmPass);

        EmailView=(LinearLayout)findViewById(R.id.EmailView);
        CodeView=(LinearLayout)findViewById(R.id.CodeView);
        PassView=(LinearLayout)findViewById(R.id.FPassView);

        ForgetBtn=(Button)findViewById(R.id.Check);
        closeBtn = (ImageButton) findViewById(R.id.close_btn);

        closeBtn.setOnClickListener(this);
        ForgetBtn.setOnClickListener(this);
        server_url=getResources().getString(R.string.website)+"user/";
        helperClass=new HelperClass(this);
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    helperClass.Input_Error(Email,null,R.color.Card);
                }
            }
        });

        Code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    helperClass.Input_Error(Code,null,R.color.Card);
                }
            }
        });

    }



    public void fetch_code(final String email) {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */
            //STARTING THE LOADING_DIALOG DIALOG FRAGMENT IN WHICH
            // THERE IS A PROGRESS BAR WHICH SHOWED FOR PROCESS
            final FragmentManager fm = getSupportFragmentManager();
            helperClass.load_Fragment(true,fm);
        if (helperClass.Check_Internet()) {
            //Log.d("zxc",server_url);
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"Forget.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //Log.d("zxc msg:", response);
                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean CodeSend=jObject.getBoolean("CodeSend");
                                if(CodeSend){
                                    MasterCode=jObject.getString("Code");
                                    helperClass.sendMessage(email,"MukBang Forget Code: "+MasterCode);
                                    CodeView.setVisibility(VISIBLE);
                                    EmailView.setVisibility(GONE);
                                    ForgetBtn.setText(R.string.confirm);
                                }else{
                                    helperClass.Input_Error(Email,jObject.getString("Error"),R.color.ErrorDialog);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d("zxc",e.getMessage());
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Email",email);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            helperClass.load_Fragment(false,fm);
        }
    }

    public void change_pass(final String email, final String pass) {
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */
        //STARTING THE LOADING_DIALOG DIALOG FRAGMENT IN WHICH
        // THERE IS A PROGRESS BAR WHICH SHOWED FOR PROCESS
        final FragmentManager fm = getSupportFragmentManager();
        helperClass.load_Fragment(true,fm);
        if (helperClass.Check_Internet()) {
            Log.d("zxc",server_url);
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"PassChange.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("zxc msg:", response);
                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean CodeSend=jObject.getBoolean("PassChange");
                                if(CodeSend){
                                    Log.d("zxc","PASS CHANGED");
                                    Intent main = new Intent(ForgetPassword.this, Login.class);
                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main);
                                }else{
                                    helperClass.Input_Error(Email,jObject.getString("Error"),R.color.ErrorDialog);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("zxc",e.getMessage());
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Email",email);
                    params.put("Password",pass);
                    Log.d("zxc params",params.toString());
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            helperClass.load_Fragment(false,fm);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Check:
                String forgetText=ForgetBtn.getText().toString();
                if(forgetText.equals(getResources().getString(R.string.confirm))){
                    String code=Code.getText().toString();
                    if (!(helperClass.CheckEmpty(Code))) {
                        if (code.equals(MasterCode)) {
                            PassView.setVisibility(VISIBLE);
                            CodeView.setVisibility(GONE);
                            ForgetBtn.setText(R.string.change);
                        } else {
                            helperClass.Input_Error(Code, "Invalid Code", R.color.ErrorDialog);
                        }
                    }
                }else if(forgetText.equals(getResources().getString(R.string.change))){
                    String pass=Pass.getText().toString();
                    String cpass=ConfirmPass.getText().toString();
                    Boolean check=helperClass.CheckEmpty(Pass) || helperClass.CheckEmpty(ConfirmPass);
                    if(!(check)){
                        if (pass.equals(cpass)) {
                            String email = Email.getText().toString();
                            change_pass(email,pass);
                        }else{
                            helperClass.Input_Error(Pass, "Password Doesn't Match", R.color.ErrorDialog);
                            helperClass.Input_Error(ConfirmPass, "Password Doesn't Match", R.color.ErrorDialog);
                        }
                    }
                }
                else {
                    if (!(helperClass.CheckEmpty(Email))) {
                        String email = Email.getText().toString();
                        Log.d("zxc args", email);
                        fetch_code(email);
                    }
                }
                break;
            case R.id.close_btn:
                finish();
                break;
        }
    }
}
