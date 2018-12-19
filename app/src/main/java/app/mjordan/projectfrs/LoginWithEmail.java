package app.mjordan.projectfrs;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginWithEmail extends AppCompatActivity implements View.OnClickListener {
    EditText username;
    ImageButton closeBtn;
    Button ForgetBtn,RegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);
        username = (EditText) findViewById(R.id.UName);
        closeBtn = (ImageButton) findViewById(R.id.close_btn);
        ForgetBtn=(Button)findViewById(R.id.ForgetPass);
        RegisterBtn=(Button) findViewById(R.id.RegisterLogin);
        username.requestFocus();
        closeBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
