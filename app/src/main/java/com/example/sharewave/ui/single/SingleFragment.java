package com.example.sharewave.ui.single;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.R;
import com.example.sharewave.adapters.CommentAdapter;
import com.example.sharewave.auth.DatabaseHelper;
import com.example.sharewave.classes.Comment;
import com.example.sharewave.classes.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleFragment extends Fragment {


    TextView txtBeach,txtLocation,txtTitle,txtDate,txtCaption;
    RatingBar ratingBar;
    ImageView ivPost;

    //comment
    EditText comment;
    ImageButton btnComment;
    private String jsonResponse;
    RequestQueue requestQueue;

    //comment
    RecyclerView recyclerView;
    List<Comment> commentList;
    RequestQueue requestQueueComment;

    //postComment
    RequestQueue requestQueueCommentPost;


    private SingleViewModel mViewModel;

    public static SingleFragment newInstance() {
        return new SingleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.single_fragment, container, false);

        Bundle arguments = getArguments();
        final int id = arguments.getInt("id");
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


        //fazer Comentario
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtcomment;

                txtcomment = comment.getText().toString();

                if (txtcomment.isEmpty()||txtcomment.equals(' ')){

                    Toasty.error(getContext(), "Insert Something", Toast.LENGTH_LONG, true).show();

                }else {
                    DatabaseHelper db;
                    List<User> user = new ArrayList<>();
                    db = new DatabaseHelper(getContext());
                    user.addAll(db.getUser());
                    String userId= user.get(0).getId();

                    requestQueueCommentPost = Volley.newRequestQueue(getContext());

                    volleyRequestComment(userId,id,txtcomment);

                    comment.setText("");


                }

            }
        });

        CommentAdapter commentAdapter = new CommentAdapter(getContext(),commentList);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();


        return root;
    }

    private void volleyRequestComment(final String userId, final int id_Post, final String txtcomment) {

        String url="http://checkwaves.com/api/comment/store";

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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("comment",txtcomment );
                params.put("id_post", String.valueOf(id_Post));
                params.put("id_user", userId);

                return params;
            }
        };

        requestQueueCommentPost.add(postRequest);

    }


    private void makeJsonArrayRequestComment(int id) {

        String url = "http://checkwaves.com/api/comments/post/"+id;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

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

                                Log.d("comentario",comment);

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

        String urlJson = "http://checkwaves.com/api/posts/"+id;

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
                                ratingBar.setRating(Integer.parseInt(rating));



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
        ratingBar= root.findViewById(R.id.txtRating);
        txtCaption= root.findViewById(R.id.txtCaption);
        ivPost = root.findViewById(R.id.imgPost);
        recyclerView = root.findViewById(R.id.recyclerView);
        comment = root.findViewById(R.id.comment);
        btnComment=root.findViewById(R.id.btnComment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SingleViewModel.class);
        // TODO: Use the ViewModel
    }

}
