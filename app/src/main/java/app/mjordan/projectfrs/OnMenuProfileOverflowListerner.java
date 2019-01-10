package app.mjordan.projectfrs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import app.mjordan.projectfrs.R;

public class OnMenuProfileOverflowListerner implements View.OnClickListener {
    private Context mContext;
    private MKB_DB dbHelper;
    //private MenuProfilerListerner editListerner;
    private FragmentTransaction ft;
    Activity activity;
    private String type,json,ListType;
    private int night;
    private boolean isLoggedIn;
    private String loginUsing;

    OnMenuProfileOverflowListerner(Context c,String type,String list,String json,String loginUsing, FragmentTransaction ft,int night){
        this.mContext=c;
        dbHelper=new MKB_DB(c);
        this.ft=ft;
        this.type=type;
        this.json=json;
        this.ListType=list;
        this.night=night;
        this.loginUsing=loginUsing;
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
       isLoggedIn = accessToken != null && !accessToken.isExpired();
    }

    @Override
    public void onClick(View view) {
        PopupMenu  popupMenu= new PopupMenu(this.mContext,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.edit:
                        String list="Edit";
                        Profile profile=new Profile();
                        Bundle bundle=new Bundle();
                        bundle.putString("Type",type);
                        bundle.putString("json",json);
                        bundle.putString("ListType",list);
                        bundle.putInt("Toggle",night);
                        bundle.putString("LoginUsing",loginUsing);
                        profile.setArguments(bundle);
                        ft.replace(R.id.TabFragment,profile);
                        ft.commit();
                        return true;
                    case R.id.LogOut:
                        if(isLoggedIn){
                           LoginManager.getInstance().logOut();

                        }else {
                            dbHelper.DeleteAll_IsLogged();
                        }
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        isLoggedIn = accessToken != null && !accessToken.isExpired();
                        if(!isLoggedIn) {
                            Intent login = new Intent(getmContext(), Login.class);
                            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getmContext().startActivity(login);
                        }
                        return true;
                    case R.id.Setting:
                        return true;
                }
            return false;
            }
        });
        popupMenu.inflate(R.menu.login_menu);
        popupMenu.show();
        MenuItem item=popupMenu.getMenu().findItem(R.id.edit);
        if(!this.ListType.equals("Edit")){
            item.setVisible(true);
        }else{
            item.setVisible(false);
        }
    }

    private Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
