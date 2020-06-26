package com.example.sharewave.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.PostAdapter;
import com.example.sharewave.classes.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecificFragment extends Fragment {

    private SpecificViewModel mViewModel;
    private TextView txtId,txtBeachName,txtLocationName;
    private RecyclerView recyclerView;
    public List<Post> postList;
    PostAdapter adapter;
    private String jsonResponse;
    private RequestQueue requestQueue;
    String beach_name,location_name,latitude,longitude;

    //forecast
    TextView height,wind;
    private String jsonResponseForecast;
    private RequestQueue requestQueueForecast;



    public static SpecificFragment newInstance() {
        return new SpecificFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.specific_fragment, container, false);

        Bundle arguments = getArguments();
        int id = arguments.getInt("id");
        beach_name = arguments.getString("beach_name");
        location_name = arguments.getString("location_name");
        latitude = arguments.getString("latitude");
        longitude = arguments.getString("longitude");

        declarar(root);

        txtBeachName.setText(beach_name);
        txtLocationName.setText(location_name);

        //forecast

        String url ="https://api.stormglass.io/forecast?lat=+"+latitude+"&lng="+longitude+"&params=waveHeight,windDirection&source=sg";

        requestQueueForecast = Volley.newRequestQueue(getContext());

        jsonRequestForecast(url);



        //post
        requestQueue = Volley.newRequestQueue(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();


        makeJsonArrayRequest(id, postList);

        adapter = new PostAdapter(getContext(),postList);

        recyclerView.setAdapter(adapter);

        return root;
    }

    private void jsonRequestForecast(String url) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String onda = null;

                try {

                    JSONArray jsonArray = response.getJSONArray("hours");

                    //para as horas
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray .getJSONObject(i);

                        JSONArray waves = jsonObject.getJSONArray("waveHeight");

                        for(int j=0; j<waves.length(); j++)
                        {
                            JSONObject wavesEntrada = waves.getJSONObject(i);

                            onda = String.valueOf(wavesEntrada.getDouble("value"));

                            height.setText(onda + " M");

                        }

                        JSONArray ventos = jsonObject.getJSONArray("windDirection");

                        for(int j=0; j<ventos.length(); j++)
                        {
                            JSONObject ventoEntrada = ventos.getJSONObject(i);

                            Double direcao = ventoEntrada.getDouble("value");

                            if (direcao >=0 && direcao<=90){
                                wind.setText("North");
                            }else if (direcao >90 && direcao<=180){
                                wind.setText("East");
                            }else if (direcao >180 && direcao<=270){
                                wind.setText("South");
                            }else wind.setText("West");


                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(
                            "Error: " , e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("erro", "Error: " + error.getMessage());


            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "e239ec18-b4e3-11ea-9409-0242ac130002-e239ed44-b4e3-11ea-9409-0242ac130002");

                return params;
            }
        };

        requestQueueForecast.add(req);

    }

    private void makeJsonArrayRequest(int id, final List<Post> postList) {

        String urlJsonArry = "http://checkwaves.com/api/posts/location/"+id;


        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

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
                                                path,
                                                caption,
                                                rating,
                                                image_size,
                                                id_location,
                                                id_user,
                                                created_at,
                                                beach_name,
                                                location_name
                                        )
                                );

                                adapter.notifyDataSetChanged();
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

    private void declarar(View root) {

        recyclerView = root.findViewById(R.id.recyclerView);
        txtBeachName = root.findViewById(R.id.beach_name);
        txtLocationName = root.findViewById(R.id.location_name);
        height = root.findViewById(R.id.waveHeight);
        wind = root.findViewById(R.id.wind);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SpecificViewModel.class);
        // TODO: Use the ViewModel
    }

}
