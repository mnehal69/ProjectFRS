package app.mjordan.projectfrs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import app.mjordan.projectfrs.R;

public class HelperClass {
    private Context mContext;
    private Fragment prev;
    private LoadingDialog loadingDialog;
    HelperClass(Context context){
        this.mContext=context;
        prev = ((Activity)mContext).getFragmentManager().findFragmentByTag("loading_dialog");
        loadingDialog = new LoadingDialog();
    }

    void Input_Error( EditText editText, String error, int id) {
        /*
         * THIS CHECK IF BOTH THE FIELD IS NOT EMPTY
         * AND SETTING THE ERROR AND COLOR OF BACKGROUND TINT AS RED
         * */

        String editval = editText.getText().toString().trim();
        if(!(error==null)) {
            editText.setError(error);
        }
        editText.getBackground().setColorFilter(mContext.getResources().getColor(id), PorterDuff.Mode.SRC_ATOP);
    }

    boolean CheckEmpty(EditText editText){
        String editval=editText.getText().toString().trim();
        TextInputLayout layout= (TextInputLayout) editText.getParent();
        String specific_field=layout.getHint().toString();
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
