package com.example.sharewave.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.ProfileAdapter;
import com.example.sharewave.auth.AuthActivity;
import com.example.sharewave.auth.DatabaseHelper;
import com.example.sharewave.classes.Post;
import com.example.sharewave.classes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {


    private ProfileViewModel notificationsViewModel;

    //user
    ImageView imgLogout;
    TextView username;
    private DatabaseHelper db;
    List<User> user = new ArrayList<>();

    //posts
    RecyclerView recyclerView;
    ProfileAdapter profileAdapter;
    RecyclerView.LayoutManager recyclerviewLayout;
    RequestQueue requestQueue;
    private String jsonResponse;
    List<Post> postList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        declarar(root);

        //User
        db = new DatabaseHelper(getContext());

        user.addAll(db.getUser());

        String name= user.get(0).getUsername();

        username.setText(name);


        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.deleteUsers();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);

            }
        });

        //Posts
        requestQueue= Volley.newRequestQueue(getContext());

        recyclerviewLayout = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(recyclerviewLayout);

        postList= new ArrayList<>();
        postList.add(
                new Post(
                        1,
                        "https://postadmin.s3.eu-west-3.amazonaws.com/Screenshot_2019-12-20-18-25-22-210_com.instagram.android.jpg",
                        "Muito Bom",
                        "3",
                        "340",
                        "1",
                        "11",
                        "2020-03-20",
                        "Baleal",
                        "Peniche"
                )
        );

        makeJsonArrayRequest();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (postList.size()==0){
            Toasty.error(getContext(), "Nenhuma imagem publicada :(", Toast.LENGTH_SHORT, true).show();
        }
        ProfileAdapter profileAdapter = new ProfileAdapter(getContext(),postList);
        recyclerView.setAdapter(profileAdapter);




        return root;
    }

    private void makeJsonArrayRequest() {
        String id= user.get(0).getId();
        String url = "http://checkwaves.com/api/post/userPosts/"+id;

        JsonArrayRequest req = new JsonArrayRequest(url,
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
                                                path,
                                                caption,
                                                rating,
                                                image_size,
                                                id_location,
                                                id_user,
                                                created_at,
                                                null,
                                                null
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

        imgLogout = root.findViewById(R.id.imglogout);
        username= root.findViewById(R.id.name);
        recyclerView = root.findViewById(R.id.recyclerViewProfile);

    }
}