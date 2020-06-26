package com.example.sharewave.ui.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.SpinnerAdapter;
import com.example.sharewave.auth.DatabaseHelper;
import com.example.sharewave.classes.Location;
import com.example.sharewave.classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;


public class CameraFragment extends Fragment{


    RatingBar ratingBar;

    //locations
    Spinner spinner;
    EditText txtCaption;
    RequestQueue requestQueueLocations;
    private String jsonResponse;
    ArrayList<Location> locationArrayList = new ArrayList<>();
    SpinnerAdapter spinnerAdapter;
    public String ids;

    //camera
    public String filePath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imgNew;
    public String currentTime;
    public File destination;

    FloatingActionButton floatingActionButton;
    TextView txtTempo;

    RequestQueue requestQueuePost;



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

        makeJsonArrayRequestLocation(locationArrayList);

        spinnerAdapter= new SpinnerAdapter(getContext(),locationArrayList);

        spinnerAdapter.notifyDataSetChanged();

        spinner.setAdapter(spinnerAdapter);

        spinnerAdapter.notifyDataSetChanged();

        spinner.setSelection(spinnerAdapter.NO_SELECTION, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Location location = (Location) adapterView.getSelectedItem();
                ids = String.valueOf(location.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        //enviar photo para s3

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            



                //AMAZON

                /*String  ACCESS_KEY="AKIAXQ7JDRSM4PMPL27I",
                        SECRET_KEY="SsYF1NLBmLyQn1ohQWumunPhIiycY1pnjYXO",
                        MY_BUCKET="postadmin";


                AmazonS3Client s3Client =   new AmazonS3Client( new BasicAWSCredentials( ACCESS_KEY, SECRET_KEY ) );
                s3Client.setRegion(Region.getRegion(Regions.EU_WEST_3));
                s3Client.setEndpoint("https://postadmin.s3.eu-west-3.amazonaws.com/");


                TransferUtility transferUtility =
                        TransferUtility.builder()
                                .context(getActivity())
                                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                                .defaultBucket(MY_BUCKET)
                                .s3Client(s3Client)
                                .build();

                TransferObserver uploadObserver =
                        transferUtility.upload( MY_BUCKET,currentTime + ".jpg", destination);

                uploadObserver.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {

                        if (TransferState.COMPLETED == state) {
                            Toast.makeText(getContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();
                            Log.d("feito","Feito");
                            destination.delete();

                        }

                        Toast.makeText(getContext(), "State Change" + state,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                        float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                        int percentDone = (int) percentDonef;

                        txtTempo.setText("bytesCurrent: " + bytesCurrent + "|bytesTotal: " + bytesTotal + "|" + percentDone + "%");

                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        ex.printStackTrace();
                    }
                });*/

                /*if (txtCaption.equals("")|| txtCaption.equals(" ")){

                }
                else {

                    requestQueuePost = Volley.newRequestQueue(getContext());

                    DatabaseHelper db;
                    List<User> user11 = new ArrayList<>();
                    db = new DatabaseHelper(getContext());
                    user11.addAll(db.getUser());
                    final String name= user11.get(0).getId();


                    String url="http://checkwaves.com/api/post/store";

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Log.d("Error.Response", String.valueOf(error));
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            String cap = txtCaption.getText().toString();
                            String rating=String.valueOf(ratingBar.getRating());

                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("path", currentTime+".jpg");
                            params.put("caption", cap);
                            params.put("rating", rating);
                            params.put("image_size", "1000");
                            params.put("id_location","1");
                            params.put("id_user",ids);

                            return params;
                        }
                    };

                    requestQueuePost.add(postRequest);

                }*/

            }
        });

        return root;

    }

    private void declarar(View root) {
        imgNew= root.findViewById(R.id.imgNew);
        txtCaption=root.findViewById(R.id.imageCaption);
        ratingBar=root.findViewById(R.id.ratingBar);
        spinner = root.findViewById(R.id.spinner);
        floatingActionButton = root.findViewById(R.id.floatingActionButton);
        txtTempo = root.findViewById(R.id.textView);
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

            onCaptureImageResult(data);

        }
    }

    private void onCaptureImageResult(Intent data) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        currentTime = df.format(Calendar.getInstance().getTime());

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] byteArray = bytes.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        destination = new File(Environment.getExternalStorageDirectory(),
                currentTime + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgNew.setImageBitmap(thumbnail);
        filePath = destination.getAbsolutePath();

        Log.d("Path", filePath);
    }


    //location
    private void makeJsonArrayRequestLocation(final ArrayList<Location> locationArrayList) {

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

                                spinnerAdapter.notifyDataSetChanged();

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