package app.mjordan.projectfrs;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginWithEmail extends AppCompatActivity {
    EditText username;
    ImageButton closeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);
         username = (EditText) findViewById(R.id.UName);
         username.requestFocus();
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setTitle("");
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.custom_login);
            closeBtn= (ImageButton) actionBar.getCustomView().findViewById(R.id.close_btn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
