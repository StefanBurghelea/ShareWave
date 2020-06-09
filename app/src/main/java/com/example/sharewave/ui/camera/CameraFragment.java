package com.example.sharewave.ui.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.sharewave.R;

import static android.app.Activity.RESULT_OK;


public class CameraFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imgNew;
    RatingBar ratingBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        dispatchTakePictureIntent();

        declarar(root);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingBar.getRating();
            }
        });

        return root;

    }

    private void declarar(View root) {
        imgNew= root.findViewById(R.id.imgNew);
        ratingBar=root.findViewById(R.id.ratingBar);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgNew.setImageBitmap(imageBitmap);
        }
    }

}