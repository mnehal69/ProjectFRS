package app.mjordan.projectfrs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnImageChoiceListener} interface
 * to handle interaction events.
 *
 */
public class ImageChoice extends DialogFragment implements View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int OPEN_GALLERY_CODE = 2;
    private OnImageChoiceListener ICListener;
    Button camera, gallery;

    public ImageChoice() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_image_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        camera = view.findViewById(R.id.Camera);
        gallery = view.findViewById(R.id.gallery);

        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.Camera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity((Objects.requireNonNull(getActivity())).getPackageManager()) != null) {
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
        if (context instanceof OnImageChoiceListener) {
            ICListener = (OnImageChoiceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImageChoiceListener");
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
    public interface OnImageChoiceListener {
        // TODO: Update argument type and name
        void image_select(Uri uri);
    }


    /**
     * This method is used whenever a activity is expecting something
     * from another activity using these parameters
     * @param requestCode the code send by the sender activity
     * @param resultCode  the code send by the receiver activity
     * @param data the data send by the receiver activity
     * In this case, A Image uri is obtained from the camera or gallery and pass to
     *@see OnImageChoiceListener image_select as a parameter which is implemented in
     * @see MainActivity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap= data.getParcelableExtra("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(Objects.requireNonNull(getActivity()).getContentResolver(), bitmap, "Title", null);
            Uri imageUri=Uri.parse(path);
            ICListener.image_select(imageUri);

        }
        if (requestCode == OPEN_GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                ICListener.image_select(imageUri);
            }
        }
        dismiss();
    }
}
