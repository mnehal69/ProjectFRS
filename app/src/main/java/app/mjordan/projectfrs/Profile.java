package app.mjordan.projectfrs;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Profile extends Fragment implements View.OnClickListener {
    Button SignIn,SignOut;
    //private OnFragmentInteractionListener mListener;
    MKB_DB dbHelper;
    LinearLayout onLogin,offLogin;
    String type="Guest";
    CircleImageView userPic;
    HelperClass helperClass;
    String server_url;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new MKB_DB(getContext());

        SignIn= (Button) view.findViewById(R.id.SignIn);
        onLogin=(LinearLayout)view.findViewById(R.id.OnLogin);
        offLogin=(LinearLayout)view.findViewById(R.id.NotLogin);
        userPic=(CircleImageView) view.findViewById(R.id.profile_image);
        SignOut= (Button) view.findViewById(R.id.SignOut);
        helperClass=new HelperClass(getContext());
        server_url=getResources().getString(R.string.website)+"user/upload_image.php";

        Bundle bundle=getArguments();
        if(bundle!=null){
            type=bundle.getString("Type");
        }

        if(type.equals("User")){
            onLogin.setVisibility(View.VISIBLE);
            offLogin.setVisibility(View.GONE);
        }else{
            onLogin.setVisibility(View.GONE);
            offLogin.setVisibility(View.VISIBLE);
        }

        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);
        userPic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_image:
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
            case R.id.SignOut:
                dbHelper.DeleteAll_IsLogged();
                Intent login=new Intent(getContext(),Login.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
                break;
        }
    }

    void setImage( Uri uri_img){
        if(uri_img!=null){
            userPic.setImageURI(uri_img);
        }
        BitmapDrawable drawable = (BitmapDrawable) userPic.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //uploadImage(bitmap);
    }

    public void uploadImage(final Bitmap bitmap) {
        Log.d("sadder","YES IT'S WORKING");
        /*
         *
         * VOLLEY PASS THE PARAMETER (WHICH ARE IN GET_PARAMS) TO SERVER_URL
         * USTING GET OR POST METHOD AND THEN RECEIVE THE RESPONSE OF THE WEBSITE
         * */
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        helperClass.load_Fragment(true,fm);
        if (helperClass.Check_Internet()) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getContext());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("sadder msg:", response);
                            helperClass.load_Fragment(false,fm);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("sadder error:", "That didn't work!");
                    helperClass.load_Fragment(false,fm);
                }
            }) {

                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    String images=getStringImage(bitmap);
                    params.put("Avatar", images);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    public String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b= baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return  temp;
    }



//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
