package app.mjordan.projectfrs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomProfileAdapter extends ArrayAdapter<ProfileList> {
    private ArrayList<ProfileList> profileLists;
    private Context mContext;
    private static final int TYPE_EVEN = 0;
    private static final int TYPE_EDIT=2;
    private String ListType;
    private User editData;
    CustomProfileAdapter(ArrayList<ProfileList> profileLists, Context c,User data){
        super(c,0,profileLists);
        this.profileLists=profileLists;
        this.mContext=c;
        this.editData=data;
    }

    public String getListType() {
        return ListType;
    }

    public void setListType(String listType) {
        ListType = listType;
    }

    public User getEditData() {
        return editData;
    }

    public void setEditData(User editData) {
        this.editData = editData;
    }

    private static class ViewHolder{
        TextView title,val;
        LinearLayout item;
        EditText edit_val;
    }
     private ViewHolder viewHolder=null;

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return profileLists.get(position).getType();
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentview = convertView;
        final ProfileList currentProfile = profileLists.get(position);

        if (currentview == null) {
                if (currentProfile.getType() == TYPE_EVEN) {
                    currentview = LayoutInflater.from(mContext).inflate(R.layout.even, parent, false);
                }
                if(currentProfile.getType()==TYPE_EDIT){
                    currentview = LayoutInflater.from(mContext).inflate(R.layout.edit, parent, false);
                }
        }

        viewHolder = new ViewHolder();
        viewHolder.title = (TextView) currentview.findViewById(R.id.Title);
        viewHolder.title.setText(currentProfile.getTitle());

        if(currentProfile.getType()==TYPE_EDIT){
            viewHolder.edit_val = (EditText) currentview.findViewById(R.id.value);
            viewHolder.edit_val.setText(currentProfile.getValue());
            viewHolder.edit_val.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        LinearLayout lview= (LinearLayout) view.getParent();
                        final EditText edit= (EditText) lview.findViewById(R.id.value);
                        final TextView text= (TextView) lview.findViewById(R.id.Title);
                        final String what=text.getText().toString();
                        edit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                String value=edit.getText().toString();
                                //Log.d("zxc",text.getText().toString()+" is Clicked with value of "+value);
                                changed_value(what,value);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });



                    }
                }
            });
        }else {
            viewHolder.val = (TextView) currentview.findViewById(R.id.value);
            viewHolder.val.setText(currentProfile.getValue());
        }

            return currentview;
        }

        public void changed_value(String whichtype,String value){
        if(value.equals("No"+whichtype.toLowerCase()+"Specified")){
            value="null";
        }
            if(whichtype.equals("Name")){
                editData.Name=value;
            }
            if(whichtype.equals("Contact")){
                editData.Contact=value;
            }
            if(whichtype.equals("Address")){
                editData.Address=value;
            }
        }

}
