package app.mjordan.projectfrs;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    private boolean loggedIn = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Use to wake screen on this app
        Intent next=new Intent(this,Login.class); //This is used to move to next activity
        startActivity(next);
        finish();
    }
}
