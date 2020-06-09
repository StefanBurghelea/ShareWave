package com.example.sharewave.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharewave.R;
import com.example.sharewave.classes.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    List<Post> posts;
    Context context;

    public ProfileAdapter(Context context,List<Post> posts){

       this.posts = posts;

        this.context = context;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.individual_profile_layout, null);

        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {

        Post post = posts.get(position);

        /*holder.txtBeach_Name.setText(post.getBeach_name());
        holder.txtCaption.setText(post.getCreated_at());
        holder.txtRating.setText(post.getRating());
        holder.txtLocation.setText(post.getLocation_name());*/

        Picasso.get().load(post.getPath()).placeholder(R.drawable.loading).fit().into(holder.imgProfile);


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile=itemView.findViewById(R.id.imgProfile);
        }
    }


}
