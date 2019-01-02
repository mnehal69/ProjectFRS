package app.mjordan.projectfrs;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.loading_menu_fragment,new LoadingMain());
        ft.commit();
        //Intent intent=new Intent(this,MainActivity.class);
        //intent.putExtra("zxc","zxc321");
        //setResult(RESULT_OK,intent);
        //finish();//finishing activity
    }
}
