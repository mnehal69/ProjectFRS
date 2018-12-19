package app.mjordan.projectfrs;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LoginWithEmail extends AppCompatActivity implements View.OnClickListener {
    EditText username,password;
    ImageButton closeBtn;
    Button ForgetBtn,RegisterBtn,LoginBtn;
    String user,pass;
    ArrayList<LoginDataSend> LoginDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);
        username = (EditText) findViewById(R.id.UName);
        password=(EditText) findViewById(R.id.PName);
        closeBtn = (ImageButton) findViewById(R.id.close_btn);
        ForgetBtn=(Button)findViewById(R.id.ForgetPass);
        RegisterBtn=(Button) findViewById(R.id.RegisterLogin);
        LoginBtn=(Button)findViewById(R.id.LoginBtn);
        /*username.requestFocus();*/
        closeBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    username.getBackground().setColorFilter(getResources().getColor(R.color.Card), PorterDuff.Mode.SRC_ATOP);
                    password.getBackground().setColorFilter(getResources().getColor(R.color.Card), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    username.getBackground().setColorFilter(getResources().getColor(R.color.Card), PorterDuff.Mode.SRC_ATOP);
                    password.getBackground().setColorFilter(getResources().getColor(R.color.Card), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }


    boolean Check_Input(){
        user=username.getText().toString();
        pass=password.getText().toString();
        if(user.equals("")){
            Toast.makeText(getApplicationContext(),"UserField is empty",Toast.LENGTH_SHORT).show();
            username.getBackground().setColorFilter(getResources().getColor(R.color.ErrorDialog), PorterDuff.Mode.SRC_ATOP);
            return false;
        }
        if( pass.equals("")){
            Toast.makeText(getApplicationContext(),"PassField is empty",Toast.LENGTH_SHORT).show();
            password.getBackground().setColorFilter(getResources().getColor(R.color.ErrorDialog), PorterDuff.Mode.SRC_ATOP);
            return false;
        }
        return true;
    }
    private class LoginDataSend{
        String name;
        String pass;
        LoginDataSend(String nameval,String passval){
            this.name=nameval;
            this.pass=passval;
        }
    }

    void fetch(){
        LoginDataSend data=new LoginDataSend(user,pass);
        LoginDataList.add(data);
        Gson gs=new Gson();
        String JsonConverted=gs.toJson(LoginDataList);
        Toast.makeText(getApplicationContext(),JsonConverted,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LoginBtn:
               boolean checked=Check_Input();
               if(checked){
                    fetch();
               }
                break;
            case R.id.close_btn:
                finish();
                break;
            case R.id.ForgetPass:
                break;
            case R.id.RegisterLogin:
                Intent nextActivity=new Intent(this,RegisterActivity.class);
                startActivity(nextActivity);
                break;
        }
    }
}
