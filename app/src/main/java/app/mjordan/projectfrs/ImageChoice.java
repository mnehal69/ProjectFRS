package app.mjordan.projectfrs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link //Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //ImageChoice.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ImageChoice extends DialogFragment implements View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int OPEN_GALLERY_CODE = 2;
    private OnImageChoiceListerner ICListener;
    Button camerabtn,gallerybtn;
    public ImageChoice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_choice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        camerabtn= (Button) view.findViewById(R.id.Camera);
        gallerybtn=(Button) view.findViewById(R.id.gallery);
        camerabtn.setOnClickListener(this);
        gallerybtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.Camera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity((getActivity()).getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.gallery:
                //intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.setType("image/*");
                intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, OPEN_GALLERY_CODE);
                break;
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageChoiceListerner) {
            ICListener = (OnImageChoiceListerner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImageChoiceListerner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ICListener = null;
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
    public interface OnImageChoiceListerner{
        // TODO: Update argument type and name
        void image_select(Uri uri);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap= data.getParcelableExtra("data");
            Log.d("zxc","WORK BITCH");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
            Uri imageUri=Uri.parse(path);
            // Do other work with full size photo saved in mLocationForPhotos
            ICListener.image_select(imageUri);

        }
        if (requestCode == OPEN_GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // this is the image selected by the user
                Uri imageUri = data.getData();
                ICListener.image_select(imageUri);
            }
        }
        dismiss();
    }
}
