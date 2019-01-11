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

    /**
     * This class is a class of function/method
     * which are required in different activity
     *
     * @param context this is the context of that activity
     */
    HelperClass(Context context){
        this.mContext=context;
        prev = ((Activity)mContext).getFragmentManager().findFragmentByTag("loading_dialog");
        loadingDialog = new LoadingDialog();
    }

    /**
     * This is alternative of Recreate() function as it wasn't working so i create my own recreate;
     * @param activity the activity you required
     * @param Isnight boolean value to determine if it's day or night theme
     * @param type this is the activity parameter to pass in Intent
     * @param json this is the activity parameter to pass in Intent
     * @param loginUsing this is the activity parameter to pass in Intent
     */
    public void Reload(Activity activity,Boolean Isnight,String type,String json,String loginUsing)
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
        main.putExtra("LoginUsing",loginUsing);
        activity.startActivity(main);
    }

    /**
     * This method adjust the listview in Landscape screen which is in
     * Scroll View
     * @param listView the listview which u need to adjust
     */
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

    /** This method set the input background tint to @param id
     *
     * @param editText the editText which is required
     * @param error the error you need to show
     * @param id the color of the background Tint
     */
    void Input_Error( EditText editText, String error, int id) {
        if(!(error==null)) {
            editText.setError(error);
        }
        editText.getBackground().setColorFilter(ContextCompat.getColor(mContext, id), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Check if device is supporting Camera or Not
     * @return boolean value
     */
    boolean isDeviceSupportCamera() {
        return mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /**
     * CheckEmpty method is checking if the editText.getText().toString is empty or not
     * @param editText The editText required for check
     * @return true/false deping if empty or not
     */
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

    /**Check_Internet method check if device is connected to Internet or not
     *
     * @return true/false determine if Internet is connected
     */
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

    /**
     * Load_Fragment is used to show
     * @see LoadingDialog
     * This is progress bar showed in Dialog box
     * @param load TRUE/FALSE Determine to load or not
     * @param fm this is required parameter for FragmentManager
     */
    public void load_Fragment(boolean load, android.support.v4.app.FragmentManager fm){
        if(load){
            if (prev == null) {
                loadingDialog.show(fm, "loading_dialog");
            }
        }else{
            loadingDialog.dismiss();
        }
    }

    /**
     * This method is used to send recovery code to receiver email
     * @param receiver the email of the user
     * @param msg the code send to that email
     */
     void sendMessage(final String receiver,final String msg) {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailSender sender = new GmailSender("mjordansite@gmail.com", "nehal123");
                    sender.sendMail("Forget Password MukBangCode", msg, "mjordansite@gmail.com", receiver);
                } catch (Exception e) {
                    Log.e("zxc Log MSG ERROR:::", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
}
