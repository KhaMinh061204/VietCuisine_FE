package com.example.vietcuisine.ui.adapters;

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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    
    private List<Post> posts;
    private OnPostInteractionListener listener;

    public interface OnPostInteractionListener {
        void onPostClick(Post post);
        void onLikeClick(Post post);
        void onCommentClick(Post post);
    }

    public PostAdapter(List<Post> posts, OnPostInteractionListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView userAvatar, postImage, likeButton, commentButton;
        private TextView userName, postTime, postContent, likesCount, commentsCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userName = itemView.findViewById(R.id.userName);
            postTime = itemView.findViewById(R.id.postTime);
            postContent = itemView.findViewById(R.id.postContent);
            postImage = itemView.findViewById(R.id.postImage);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            likesCount = itemView.findViewById(R.id.likesCount);
            commentsCount = itemView.findViewById(R.id.commentsCount);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onPostClick(posts.get(position));
                    }
                }
            });
            
            likeButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onLikeClick(posts.get(position));
                    }
                }
            });
            
            commentButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCommentClick(posts.get(position));
                    }
                }
            });
        }        public void bind(Post post) {
            // TODO: Load user data when User object is added to Post model
            userName.setText("Unknown User");
            userAvatar.setImageResource(R.drawable.ic_person);
            
            postContent.setText(post.getCaption());
            postTime.setText(post.getCreatedAt());
            likesCount.setText(String.valueOf(post.getLikesCount()));
            commentsCount.setText(String.valueOf(post.getCommentsCount()));
            
            // Set like button state
            likeButton.setImageResource(post.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            
            if (post.getImage() != null && !post.getImage().isEmpty()) {
                postImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                    .load(post.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_post)
                    .into(postImage);
            } else {
                postImage.setVisibility(View.GONE);
            }
        }
    }
}
