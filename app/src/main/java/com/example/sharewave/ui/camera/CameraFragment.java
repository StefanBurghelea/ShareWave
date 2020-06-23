package com.example.sharewave.ui.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.SpinnerAdapter;
import com.example.sharewave.classes.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;


public class CameraFragment extends Fragment{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imgNew;
    RatingBar ratingBar;

    //locations
    Spinner spinner;
    EditText txtCaption;
    RequestQueue requestQueueLocations;
    private String jsonResponse;
    ArrayList<Location> locationArrayList = new ArrayList<>();
    SpinnerAdapter spinnerAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        //Camera
        dispatchTakePictureIntent();

        declarar(root);

        //Rating Bar
        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingBar.getRating();
            }
        });

        //Spinner Locations
        requestQueueLocations= Volley.newRequestQueue(getContext());

        locationArrayList = new ArrayList<>();

        locationArrayList.add(
                new Location(
                        12,
                        "Baleal",
                        "Peniche"
                        ,"11","12"
                )
        );

        makeJsonArrayRequestLocation();

        spinnerAdapter= new SpinnerAdapter(getContext(),locationArrayList);

        spinnerAdapter.notifyDataSetChanged();

        spinner.setAdapter(spinnerAdapter);

        spinnerAdapter.notifyDataSetChanged();

        spinner.setSelection(spinnerAdapter.NO_SELECTION, false);


        return root;

    }

    private void declarar(View root) {
        imgNew= root.findViewById(R.id.imgNew);
        txtCaption=root.findViewById(R.id.imageCaption);
        ratingBar=root.findViewById(R.id.ratingBar);
        spinner = root.findViewById(R.id.spinner);
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



    //location
    private void makeJsonArrayRequestLocation() {

        String urlLocations= "http://checkwaves.com/api/locations/";

        JsonArrayRequest req = new JsonArrayRequest(urlLocations,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            jsonResponse = "";

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                int id = Integer.parseInt(person.getString("id"));
                                String beach_name = person.getString("beach_name");
                                String location_name = person.getString("location_name");
                                String longitude = person.getString("longitude");
                                String latitude = person.getString("latitude");

                                locationArrayList.add(
                                        new Location(
                                                id,
                                                beach_name,
                                                location_name,
                                                longitude,
                                                latitude
                                        )
                                );

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        requestQueueLocations.add(req);

    }



}