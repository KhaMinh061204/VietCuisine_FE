package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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

    private final List<Reel> reels;
    private final Context context;
    private final OnReelInteractionListener listener;
    private ReelViewHolder currentPlayingHolder;

    public interface OnReelInteractionListener {
        void onLikeClick(Reel reel, int pos);
        void onCommentClick(Reel reel,int pos);
        void onShareClick(Reel reel,int pos);
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
        Log.d("Get position","Position"+ reel);
        holder.captionText.setText(reel.getCaption());
        holder.authorName.setText(reel.getAuthor() != null ? reel.getAuthor().getName() : "Ẩn danh");
        holder.likeCount.setText(String.valueOf(reel.getLikesCount()));
        holder.likeButton.setImageResource(reel.isLiked() ?
                R.drawable.ic_favorite_active : R.drawable.ic_favorite_inactive);
        Log.d("Get like","is like"+ reel.isLiked());
        holder.videoView.setVideoURI(Uri.parse(reel.getVideo()));
        holder.videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            if (position == 0 && currentPlayingHolder == null) {
                currentPlayingHolder = holder;
                holder.videoView.start();
            }
        });

        holder.likeButton.setOnClickListener(v -> {
            Log.d("set like button","like button"+listener );
            if (listener != null) listener.onLikeClick(reel, position);
        });

        holder.commentButton.setOnClickListener(v -> {
            if (listener != null) listener.onCommentClick(reel,position);
        });

        holder.shareButton.setOnClickListener(v -> {
            if (listener != null) listener.onShareClick(reel,position);
        });
    }

    public void updateLikeState(int position, boolean liked, int newLikeCount) {
        Reel reel = reels.get(position);
        reel.setLiked(liked);
        reel.setLikesCount(newLikeCount);

        RecyclerView.ViewHolder holder = findViewHolderAt(position);
        if (holder instanceof ReelViewHolder) {
            ReelViewHolder viewHolder = (ReelViewHolder) holder;
            viewHolder.likeCount.setText(String.valueOf(newLikeCount));
            viewHolder.likeButton.setImageResource(liked ?
                    R.drawable.ic_favorite_active : R.drawable.ic_favorite_inactive);
        }
    }


    @Override
    public int getItemCount() {
        return reels.size();
    }

    public void playVideoAt(int position) {
        RecyclerView.ViewHolder holder = findViewHolderAt(position);
        if (holder instanceof ReelViewHolder) {
            ReelViewHolder reelHolder = (ReelViewHolder) holder;
            reelHolder.videoView.start();
            currentPlayingHolder = reelHolder;
        }
    }

    public void pauseVideoAt(int position) {
        RecyclerView.ViewHolder holder = findViewHolderAt(position);
        if (holder instanceof ReelViewHolder) {
            ((ReelViewHolder) holder).videoView.pause();
        }
    }

    private RecyclerView.ViewHolder findViewHolderAt(int position) {
        // Lưu lại reference khi được gắn vào RecyclerView trong Fragment nếu cần
        return currentPlayingHolder != null ? currentPlayingHolder.itemView.getParent() instanceof RecyclerView
                ? ((RecyclerView) currentPlayingHolder.itemView.getParent()).findViewHolderForAdapterPosition(position)
                : null
                : null;
    }

    static class ReelViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        ImageView authorImage;
        TextView authorName, captionText, likeCount;
        ImageButton likeButton, commentButton, shareButton;

        public ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            authorImage = itemView.findViewById(R.id.userAvatar);
            authorName = itemView.findViewById(R.id.usernameText);
            captionText = itemView.findViewById(R.id.descriptionText);
            likeCount = itemView.findViewById(R.id.likeCount);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            shareButton = itemView.findViewById(R.id.shareButton);
        }
    }
}
