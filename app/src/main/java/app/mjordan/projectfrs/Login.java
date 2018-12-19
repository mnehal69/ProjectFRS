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
    Button FacebookLogin,EmailSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#243b55"));
        FacebookLogin= (Button) findViewById(R.id.facebook);
        EmailSignUp= (Button) findViewById(R.id.email);
        EmailSignUp.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email:
            Intent EmailActivity = new Intent(getApplicationContext(), LoginWithEmail.class);
            startActivity(EmailActivity);
            break;
            case R.id.facebook:
                break;
        }
    }
}
