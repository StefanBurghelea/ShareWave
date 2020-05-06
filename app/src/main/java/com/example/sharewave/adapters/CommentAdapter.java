package com.example.sharewave.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sharewave.R;
import com.example.sharewave.classes.Comment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context ctx;
    private List<Comment> commentList;

    public CommentAdapter(Context ctx,List<Comment> commentList){
        this.ctx=ctx;
        this.commentList=commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View view = inflater.inflate(R.layout.comment_view_items,null);

        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {

        Comment comment = commentList.get(position);

        holder.tuser.setText(comment.getId_user());
        holder.tcomment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tuser,tcomment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tuser = itemView.findViewById(R.id.user);
            tcomment = itemView.findViewById(R.id.comment);
        }
    }
}
