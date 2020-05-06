package com.example.sharewave.ui.single;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.CommentAdapter;
import com.example.sharewave.classes.Comment;
import com.example.sharewave.classes.Post;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SingleFragment extends Fragment {

    //comment
    TextView txtBeach,txtLocation,txtTitle,txtDate,txtRating,txtCaption;
    ImageView ivPost;
    private String jsonResponse;
    RequestQueue requestQueue;

    //comment
    RecyclerView recyclerView;
    List<Comment> commentList;
    RequestQueue requestQueueComment;


    private SingleViewModel mViewModel;

    public static SingleFragment newInstance() {
        return new SingleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.single_fragment, container, false);

        Bundle arguments = getArguments();
        int id = arguments.getInt("id");
        String beach  = arguments.getString("beach");
        String location = arguments.getString("location");

        declarar(root);
        //posts
        txtBeach.setText(beach);
        txtLocation.setText(location);

        requestQueue = Volley.newRequestQueue(getContext());
        makeJsonArrayRequest(id);

        //comments
        requestQueueComment = Volley.newRequestQueue(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        commentList = new ArrayList<>();



        makeJsonArrayRequestComment(id);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CommentAdapter commentAdapter = new CommentAdapter(getContext(),commentList);
        recyclerView.setAdapter(commentAdapter);


        return root;
    }

    private void makeJsonArrayRequestComment(int id) {

        String url = "http://192.168.1.156:8000/api/comments/post/"+id;

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
                                String comment = person.getString("comment");
                                String id_post = person.getString("id_post");
                                String id_user = person.getString("id_user");


                                commentList.add(
                                        new Comment(
                                                id,
                                                comment,
                                                id_post,
                                                id_user
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
        requestQueueComment.add(req);

    }

    private void makeJsonArrayRequest(int id) {

        String urlJson = "http://192.168.1.156:8000/api/posts/"+id;

        JsonObjectRequest req = new JsonObjectRequest(urlJson,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            jsonResponse = "";


                                String id = response.getString("id");
                                String path = response.getString("path");
                                String caption = response.getString("caption");
                                String rating = response.getString("rating");
                                String image_size = response.getString("image_size");
                                String id_user = response.getString("id_user");
                                String id_location = response.getString("id_location");
                                String created_at = response.getString("created_at");

                                txtTitle.setText(id_user);
                                txtDate.setText(created_at);
                                txtCaption.setText(caption);
                                txtRating.setText(rating);


                                Picasso.get().load(path).placeholder(R.drawable.loading).fit().into(ivPost);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.d("erro", e.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("erro", error.getMessage());
            }
        });

        // Adding request to request queue
        requestQueue.add(req);

    }

    private void declarar(View root) {

        txtBeach= root.findViewById(R.id.beach_name);
        txtLocation= root.findViewById(R.id.location_name);
        txtTitle = root.findViewById(R.id.txtTitle);
        txtDate= root.findViewById(R.id.txtLocation);
        txtRating= root.findViewById(R.id.txtRating);
        txtCaption= root.findViewById(R.id.txtCaption);
        ivPost = root.findViewById(R.id.imgPost);
        recyclerView = root.findViewById(R.id.recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SingleViewModel.class);
        // TODO: Use the ViewModel
    }

}
