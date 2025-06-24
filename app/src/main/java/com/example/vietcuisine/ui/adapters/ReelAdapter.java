package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Reel;
import java.util.List;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ReelViewHolder> {
    private List<Reel> reels;
    private Context context;
    private OnReelInteractionListener listener;

    public interface OnReelInteractionListener {
        void onLikeClick(Reel reel);
        void onCommentClick(Reel reel);
        void onShareClick(Reel reel);
    }

    public ReelAdapter(Context context, List<Reel> reels, OnReelInteractionListener listener) {
        this.context = context;
        this.reels = reels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        Reel reel = reels.get(position);
        
        holder.captionText.setText(reel.getCaption());
        holder.authorName.setText(reel.getAuthor() != null ? reel.getAuthor().getName() : "Unknown");
        
        // TODO: Load video and author image
        // Glide.with(context).load(reel.getVideoUrl()).into(holder.videoView);

        holder.likeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLikeClick(reel);
            }
        });

        holder.commentButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCommentClick(reel);
            }
        });

        holder.shareButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShareClick(reel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    public void updateReels(List<Reel> newReels) {
        this.reels = newReels;
        notifyDataSetChanged();
    }

    public void pauseCurrentVideo() {
        // TODO: Implement video pause functionality
        // This would typically involve tracking the currently playing video
        // and pausing it when the fragment goes into background
    }

    static class ReelViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        ImageView authorImage;
        TextView authorName, captionText;
        ImageButton likeButton, commentButton, shareButton;

        public ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            authorImage = itemView.findViewById(R.id.authorImage);
            authorName = itemView.findViewById(R.id.authorName);
            captionText = itemView.findViewById(R.id.captionText);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            shareButton = itemView.findViewById(R.id.shareButton);
        }
    }
}
