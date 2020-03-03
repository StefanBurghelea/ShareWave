package com.example.sharewave.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.PostAdapter;
import com.example.sharewave.classes.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpecificFragment extends Fragment {

    private SpecificViewModel mViewModel;
    TextView txtId,txtBeachName,txtLocationName;
    RecyclerView recyclerView;
    List<Post> postList;
    private String jsonResponse;
    RequestQueue requestQueue;

    public static SpecificFragment newInstance() {
        return new SpecificFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.specific_fragment, container, false);

        Bundle arguments = getArguments();
        int id = arguments.getInt("id");
        String beach_name = arguments.getString("beach_name");
        String location_name = arguments.getString("location_name");

        declarar(root);

        txtBeachName.setText(beach_name);
        txtLocationName.setText(location_name);

        requestQueue = Volley.newRequestQueue(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();

        makeJsonArrayRequest(id);

        PostAdapter adapter = new PostAdapter(getContext(),postList);

        recyclerView.setAdapter(adapter);

        return root;
    }

    private void makeJsonArrayRequest(int id) {

        String urlJsonArry = "http://192.168.30.101:8000/api/posts/location/"+id;

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

    private void declarar(View root) {

        recyclerView = root.findViewById(R.id.recyclerView);
        txtBeachName = root.findViewById(R.id.beach_name);
        txtLocationName = root.findViewById(R.id.location_name);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SpecificViewModel.class);
        // TODO: Use the ViewModel
    }

}
