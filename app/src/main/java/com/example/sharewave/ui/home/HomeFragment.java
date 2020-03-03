package com.example.sharewave.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.LocationAdapter;
import com.example.sharewave.adapters.PostAdapter;
import com.example.sharewave.classes.Location;
import com.example.sharewave.classes.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    //location
    ListView list;
    SearchView editsearch;
    ArrayAdapter arrayAdapter;
    LocationAdapter adapterLocation;
    ArrayList<Location> locationArrayList = new ArrayList<>();
    RequestQueue requestQueueLocations;
    String urlLocations= "http://192.168.30.101:8000/api/locations/";


    //posts
    RecyclerView recyclerView;
    List<Post> postList;
    private String jsonResponse;
    RequestQueue requestQueue;
    String urlJsonArry = "http://192.168.30.101:8000/api/posts/";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        declarar(root);

        //locationsantes
        requestQueueLocations=Volley.newRequestQueue(getContext());

        locationArrayList = new ArrayList<>();

        makeJsonArrayRequestLocation();

        adapterLocation = new LocationAdapter(getContext(), locationArrayList);

        list.setAdapter(adapterLocation);

        editsearch.setOnQueryTextListener(this);


        //posts

        requestQueue = Volley.newRequestQueue(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();

        postList.add(
                new Post(
                        1,
                        "Baleal",
                        "4",
                        "dedeede",
                        "https://postadmin.s3.eu-west-3.amazonaws.com/Screenshot_2019-12-20-18-25-22-210_com.instagram.android.jpg",
                        "Peniche"
                        )
        );
        postList.add(
                new Post(
                        2,
                        "SuperTubos",
                        "4",
                        "dededadsadasdasasdede",
                        "https://postadmin.s3.eu-west-3.amazonaws.com/Screenshot_2019-12-20-18-25-22-210_com.instagram.android.jpg",
                        "Peniche"

                )
        );


        makeJsonArrayRequest();

        PostAdapter adapter = new PostAdapter(getContext(),postList);

        recyclerView.setAdapter(adapter);

        return root;

    }

    //location
    private void makeJsonArrayRequestLocation() {

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
                                    Toast.LENGTH_LONG).show();
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

    //posts
    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            // Parsing json array response
                            // loop through each json object

                            jsonResponse = "";

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                int id = Integer.parseInt(person.getString("id"));
                                String path = person.getString("path");
                                String caption = person.getString("caption");
                                String rating = person.getString("rating");
                                String image_size = person.getString("image_size");
                                String id_user = person.getString("id_user");
                                String id_location = person.getString("id_location");
                                String created_at = person.getString("created_at");

                                postList.add(
                                        new Post(
                                                id,
                                                created_at,
                                                rating,
                                                caption,
                                                path,
                                                id_location
                                        )
                                );

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        requestQueue.add(req);

    }


    //declarar
    private void declarar(View root){

        recyclerView=root.findViewById(R.id.recyclerView);
        list=root.findViewById(R.id.listview);
        editsearch = root.findViewById(R.id.search);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)){
            adapterLocation.filter("");
            list.clearTextFilter();
        }
        else {
            adapterLocation.filter(newText);
        }
        return true;
    }
}