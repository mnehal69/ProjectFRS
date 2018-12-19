package app.mjordan.projectfrs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton closeBtn;
    Button RegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        closeBtn = (ImageButton) findViewById(R.id.close_btn);
        RegisterBtn=(Button)findViewById(R.id.Register);
        closeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                finish();
                break;
            case R.id.Register:
                break;
        }
    }
}
