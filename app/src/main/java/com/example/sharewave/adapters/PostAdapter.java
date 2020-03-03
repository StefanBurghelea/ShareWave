package com.example.sharewave.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharewave.R;
import com.example.sharewave.classes.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context ctx;
    private List<Post> postsList;

    public PostAdapter(Context ctx,List<Post> postsList){

        this.ctx=ctx;
        this.postsList=postsList;

    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View view = inflater.inflate(R.layout.individual, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {

        Post post = postsList.get(position);

        holder.txtBeach_Name.setText(post.getBeach_name());
        holder.txtCaption.setText(post.getCaption());
        holder.txtRating.setText(post.getRating());
        holder.txtLocation.setText(post.getLocation());

        Picasso.get().load(post.getPath()).placeholder(R.drawable.loading).fit().into(holder.imgPost);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtBeach_Name,txtRating,txtCaption,txtLocation;
        ImageView imgPost;


        public MyViewHolder(View view) {
            super(view);

            txtBeach_Name = view.findViewById(R.id.txtTitle);
            txtLocation=view.findViewById(R.id.txtLocation);
            txtCaption = view.findViewById(R.id.txtCaption);
            txtRating = view.findViewById(R.id.txtRating);
            imgPost = view.findViewById(R.id.imgPost);
        }
    }
}