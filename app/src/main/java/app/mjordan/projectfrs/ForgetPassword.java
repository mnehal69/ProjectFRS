package app.mjordan.projectfrs;


import android.content.Intent;
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

        server_url=getResources().getString(R.string.website)+"user/";

        helperClass=new HelperClass(this);

        Email= findViewById(R.id.FEmail);
        Code= findViewById(R.id.FCode);
        Pass= findViewById(R.id.FPass);
        ConfirmPass= findViewById(R.id.FConfirmPass);

        EmailView= findViewById(R.id.EmailView);
        CodeView= findViewById(R.id.CodeView);
        PassView= findViewById(R.id.FPassView);

        ForgetBtn= findViewById(R.id.Check);
        closeBtn = findViewById(R.id.close_btn);

        closeBtn.setOnClickListener(this);
        ForgetBtn.setOnClickListener(this);

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

    // ***************************************
    // Private Method
    // ***************************************
    /**
     * Fetch_Code method pass the following parameters
     * @param email the email provided by user
     * through
     *@see Volley
     *to Server from which a recovery code is gernatered and send
     * back to app as json.
     * Apart from sending email,this method is also sending the recovery code
     * send by server to the user email address.
     * A Custom Dialog Fragment is called with the function defined in
     * @see HelperClass
     */

    private void fetch_code(final String email) {
            final FragmentManager fm = getSupportFragmentManager();
            helperClass.load_Fragment(true,fm);

        if (helperClass.Check_Internet()) {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"Forget.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Email",email);
                    return params;
                }
            };
            queue.add(stringRequest);

        }else{
            helperClass.load_Fragment(false,fm);
        }
    }



    /**
     * Fetch_Code method pass the following parameters
     * @param email the email provided by user
     * @param pass the new password provided by user
     * through
     *@see Volley
     *to Server from which a json is send
     * back to app and check if code is send in Volley's OnResponse Method.
     * A Custom Dialog Fragment is called with the function defined in
     * @see HelperClass
     */
    private void change_pass(final String email, final String pass) {
        final FragmentManager fm = getSupportFragmentManager();
        helperClass.load_Fragment(true,fm);
        if (helperClass.Check_Internet()) {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"PassChange.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean CodeSend=jObject.getBoolean("PassChange");
                                if(CodeSend){
                                    Intent main = new Intent(ForgetPassword.this, Login.class);
                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main);
                                }else{
                                    helperClass.Input_Error(Email,jObject.getString("Error"),R.color.ErrorDialog);
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
                protected Map<String, String> getParams()  {
                    Map<String, String> params = new HashMap<>();
                    params.put("Email",email);
                    params.put("Password",pass);
                    Log.d("zxc params",params.toString());
                    return params;
                }
            };
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
