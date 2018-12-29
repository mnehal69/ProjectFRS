package app.mjordan.projectfrs;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import app.mjordan.projectfrs.R;

public class OnMenuProfileOverflowListerner implements View.OnClickListener {
    private Context mContext;
    MKB_DB dbHelper;
    public OnMenuProfileOverflowListerner(Context c){
        this.mContext=c;
        dbHelper=new MKB_DB(c);
    }
    @Override
    public void onClick(View view) {
        PopupMenu  popupMenu= new PopupMenu(this.mContext,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.edit:
                        return true;
                    case R.id.LogOut:
                        dbHelper.DeleteAll_IsLogged();
                        Intent login=new Intent(getmContext(),Login.class);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getmContext().startActivity(login);
                        return true;
                    case R.id.Setting:
                        return true;
                }
            return false;
            }
        });
        popupMenu.inflate(R.menu.login_menu);
        popupMenu.show();
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
