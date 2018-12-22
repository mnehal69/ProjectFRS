package app.mjordan.projectfrs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button SignOut;
    MKB_DB dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignOut= (Button) findViewById(R.id.SignOut);
        SignOut.setOnClickListener(this);
        dbHelper = new MKB_DB(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.SignOut:
                dbHelper.DeleteAll_IsLogged();
                Intent login=new Intent(this,Login.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
                break;
        }
    }
}
