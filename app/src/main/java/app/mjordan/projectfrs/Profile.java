package app.mjordan.projectfrs;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Profile extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    Button SignIn;
    private OnFragmentInteractionListener mListener;
    MKB_DB dbHelper;
    LinearLayout offLogin,Menu,Done,onLogin;
    String type="Guest";
    CircleImageView userPic;
    HelperClass helperClass;
    String server_url;
    String mediaPath,json;
    User userData,editData;
    Switch ThemeSwitch;
    ListView listView;
    ArrayList<ProfileList> profileListArrayList;
    int ListType=0,night;
    CustomProfileAdapter customProfileAdapter;
    Boolean show=false;
    public Profile() {
        // Required empty public constructor

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new MKB_DB(getContext());
        helperClass=new HelperClass(getContext());
        SignIn= view.findViewById(R.id.SignIn);
        onLogin= view.findViewById(R.id.OnLogin);
        offLogin= view.findViewById(R.id.NotLogin);
        Menu= view.findViewById(R.id.profile_menu);
        Done= view.findViewById(R.id.done);
        userPic= view.findViewById(R.id.profile_image);
        listView= view.findViewById(R.id.list);
        ThemeSwitch= view.findViewById(R.id.ThemeSet);
        helperClass=new HelperClass(getContext());
        server_url=getResources().getString(R.string.website)+"user/upload_image.php";
        Bundle bundle=getArguments();
        if(bundle!=null){
            type=bundle.getString("Type");
            json=bundle.getString("json");
            String listType=bundle.getString("ListType");
            night=bundle.getInt("Toggle");
            if(night==1){
                ThemeSwitch.setChecked(true);
            }else{
                ThemeSwitch.setChecked(false);
            }
            if(listType != null && listType.equals("Edit")){
                ListType=2;
                show=true;
            }else{
                ListType=0;
            }
            //Log.d("zxc", String.valueOf(ListType));
            if(json!=null){
                FragmentTransaction ft= Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                Menu.setOnClickListener(new OnMenuProfileOverflowListerner(getContext(),type,listType,json,ft,night));
                profileListArrayList=new ArrayList<>();
                Gson gson = new Gson();
                userData = gson.fromJson(json, User.class);
                editData = gson.fromJson(json, User.class);
                profileListArrayList.add(new ProfileList("Name",userData.Name,ListType));
                profileListArrayList.add(new ProfileList("Email",userData.Email,0));
                profileListArrayList.add(new ProfileList("Contact",userData.Contact,ListType));
                profileListArrayList.add(new ProfileList("Address",userData.Address,ListType));

                customProfileAdapter=new CustomProfileAdapter(profileListArrayList,getContext(),editData);
                if(userData.Avatar!=null) {
                    Picasso.get().load(getResources().getString(R.string.website)+userData.Avatar).into(userPic);
                }
                listView.setAdapter(customProfileAdapter);
            }
        }

        if(type.equals("User")){
            onLogin.setVisibility(View.VISIBLE);
            offLogin.setVisibility(View.GONE);
        }else{
            onLogin.setVisibility(View.GONE);
            offLogin.setVisibility(View.VISIBLE);
        }
        if(show){
            Done.setVisibility(View.VISIBLE);
        }else{
            Done.setVisibility(View.GONE);
        }
        helperClass.setListViewHeightBasedOnChildren(listView);
        Done.setOnClickListener(this);
        SignIn.setOnClickListener(this);
        userPic.setOnClickListener(this);
        ThemeSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_image:
                assert getFragmentManager() != null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                ImageChoice dialogFragment = new ImageChoice();
                dialogFragment.show(ft, "dialog");
                break;
            case R.id.SignIn:
                Intent main = new Intent(getContext(), Login.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                break;
            case R.id.done:
                mListener.Done(userData,customProfileAdapter.getEditData());
                break;
        }
    }

    void setImage( Uri uri){
        if(uri!=null) {
            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor= (Objects.requireNonNull(getActivity())).getContentResolver().query(uri,filePathColumn,null,null,null);
            assert cursor!=null;
            cursor.moveToFirst();
            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
            mediaPath=cursor.getString(columnIndex);
            Log.d("zxc",mediaPath);
            Picasso.get().load(uri.toString()).into(userPic);
            cursor.close();
        }
        uploadFile();
    }



    // Uploading Image/Video
    private void uploadFile() {
        final FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        helperClass.load_Fragment(true,fm);

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", userData.ID+".png", requestBody);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(retrofit2.Call call, retrofit2.Response response) {
                ServerResponse serverResponse = (ServerResponse) response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        userPic.setImageDrawable(getResources().getDrawable(R.drawable.bartender));
                    }
                }
                helperClass.load_Fragment(false,fm);
            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                helperClass.load_Fragment(false,fm);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            mListener.Theme(b);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void Done(User user, User userData);
        void Theme(Boolean Isnight);
    }
}
