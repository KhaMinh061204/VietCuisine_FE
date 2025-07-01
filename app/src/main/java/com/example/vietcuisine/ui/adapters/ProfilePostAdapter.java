package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Post;
import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.PostViewHolder> {
    private List<Post> posts;
    private Context context;
    private OnPostClickListener listener;

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public ProfilePostAdapter(Context context, List<Post> posts, OnPostClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_grid, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.postCaption.setText(post.getCaption());

        if (post.getImage() != null && !post.getImage().isEmpty()) {
            Glide.with(context)
                    .load(post.getImage())
                    .placeholder(R.drawable.placeholder_post)
                    .into(holder.postImage);
            holder.postImage.setVisibility(View.VISIBLE);
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        holder.commentsCount.setText(String.valueOf(post.getCommentsCount()));
        holder.likesCount.setText(String.valueOf(post.getLikesCount()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPostClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }


    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postCaption, likesCount,commentsCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            postCaption = itemView.findViewById(R.id.postCaption);
            likesCount = itemView.findViewById(R.id.likesCount);
            commentsCount = itemView.findViewById(R.id.commentsCount);
        }
    }
}
