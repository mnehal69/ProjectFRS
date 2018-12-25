package app.mjordan.projectfrs;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements BottomNavBar.OnBottomNavListerner {
    MKB_DB dbHelper;
    ActionBar toolbar;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MKB_DB(this);
        toolbar=getSupportActionBar();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.TabFragment,new LoadingMain());
        ft.commit();
    }

    @Override
    public void fragment(int n) {
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        switch(n){
            case 1:
                ft.replace(R.id.TabFragment,new Order());
                Log.d("zxc","CASE1");
                break;
            case 2:
                ft.replace(R.id.TabFragment,new Eat());
                Log.d("zxc","CASE2");
                break;
            case 3:
                Log.d("zxc","CASE3");
                ft.replace(R.id.TabFragment,new Profile());
                break;
        }
        ft.commit();
    }
}
