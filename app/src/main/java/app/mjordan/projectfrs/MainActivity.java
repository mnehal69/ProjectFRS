package app.mjordan.projectfrs;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements BottomNavBar.OnBottomNavListerner,ImageChoice.OnImageChoiceListerner {
    MKB_DB dbHelper;
    ActionBar toolbar;
    FragmentTransaction ft;
    String type,json;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MKB_DB(this);
        toolbar=getSupportActionBar();
        type=getIntent().getExtras().getString("Type","Guest");
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        json = getIntent().getExtras().getString("UserData",null);
        Profile profile=new Profile();
        Bundle bundle=new Bundle();
        bundle.putString("Type",type);
        bundle.putString("json",json);
        profile.setArguments(bundle);
        ft.add(R.id.TabFragment,profile);
        ft.commit();
        PersmissionUtils.checkAndRequestPermissions(MainActivity.this);
        actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Setting");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.login_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.LogOut:
                dbHelper.DeleteAll_IsLogged();
                Intent login=new Intent(this,Login.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
                break;
            case R.id.edit:

                break;
        }
        return false;
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
                Profile profile=new Profile();
                Bundle bundle=new Bundle();
                bundle.putString("Type",type);
                bundle.putString("json",json);
                profile.setArguments(bundle);
                ft.replace(R.id.TabFragment,profile);
                break;
        }
        ft.commit();
    }
    @Override
    public void image_select( Uri uri) {
        Profile frag = (Profile)
                getSupportFragmentManager().findFragmentById(R.id.TabFragment);
        if(uri!=null){
            frag.setImage(uri);
        }
    }

}
