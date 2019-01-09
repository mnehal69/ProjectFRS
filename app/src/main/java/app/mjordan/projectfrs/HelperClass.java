package app.mjordan.projectfrs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Objects;


public class HelperClass {
    private Context mContext;
    private Fragment prev;
    private LoadingDialog loadingDialog;
    private static final String MyPREFERENCES = "Theme" ;

    HelperClass(Context context){
        this.mContext=context;
        prev = ((Activity)mContext).getFragmentManager().findFragmentByTag("loading_dialog");
        loadingDialog = new LoadingDialog();
    }

    public void Reload(Activity activity,Boolean Isnight,String type,String json)
    {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Window window = activity.getWindow();
        if (Isnight){
            window.setStatusBarColor(activity.getResources().getColor(R.color.SplashBackground));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        activity.finish();
        Intent main = new Intent(activity, activity.getClass());
        main.putExtra("Type",type);
        main.putExtra("UserData",json);
        activity.startActivity(main);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    void Input_Error( EditText editText, String error, int id) {
        /*
         * THIS CHECK IF BOTH THE FIELD IS NOT EMPTY
         * AND SETTING THE ERROR AND COLOR OF BACKGROUND TINT AS RED
         * */
        if(!(error==null)) {
            editText.setError(error);
        }
        editText.getBackground().setColorFilter(ContextCompat.getColor(mContext, id), PorterDuff.Mode.SRC_ATOP);
    }
    boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    boolean CheckEmpty(EditText editText){
        String editval=editText.getText().toString().trim();
        TextInputLayout layout= (TextInputLayout) editText.getParentForAccessibility();
        String specific_field= Objects.requireNonNull(layout.getHint()).toString();
        //Log.d("zxc hint", String.valueOf(layout.getHint()));
        if(editval.equals("")){
            Input_Error(editText,specific_field+" is empty",R.color.ErrorDialog);
            return true;
        }
        return false;
    }

    public boolean Check_Internet(){
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        if(!connected) {
            Toast.makeText(mContext, "No Internet Connected", Toast.LENGTH_SHORT).show();
        }
        return connected;
    }

    public void load_Fragment(boolean load, android.support.v4.app.FragmentManager fm){
        if(load){
            if (prev == null) {
                //STARTING THE LOADING_DIALOG DIALOG FRAGMENT IN WHICH
                // THERE IS A PROGRESS BAR WHICH SHOWED FOR PROCESS
                loadingDialog.show(fm, "loading_dialog");
                //Log.d("sadder", "load");
            }
        }else{
            loadingDialog.dismiss();
        }
    }

     void sendMessage(final String reciver,final String msg) {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailSender sender = new GmailSender("mjordansite@gmail.com", "nehal123");
                    sender.sendMail("Forget Password MukBangCode", msg, "mjordansite@gmail.com", reciver);
                } catch (Exception e) {
                    Log.e("zxc Log MSG ERROR:::", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
}
