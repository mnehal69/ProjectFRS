package app.mjordan.projectfrs;


import android.content.Intent;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button FacebookLogin,EmailSignUp,GuessBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //SETTING THE COLOR OF THE STATUS BAR
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#243b55"));
        //INTIALIZING THE BUTTON
        FacebookLogin= (Button) findViewById(R.id.facebook);
        EmailSignUp= (Button) findViewById(R.id.email);
        GuessBtn=(Button) findViewById(R.id.Guest);
        EmailSignUp.setOnClickListener(this);
        GuessBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email:
                //GOING TO NEXT ACTIVITY
            Intent EmailActivity = new Intent(getApplicationContext(), LoginWithEmail.class);
            startActivity(EmailActivity);
            break;
            case R.id.facebook:
                break;
            case R.id.Guest:
                Intent main = new Intent(Login.this, MainActivity.class);
                main.putExtra("Type","Guest");
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                break;

        }
    }
}
