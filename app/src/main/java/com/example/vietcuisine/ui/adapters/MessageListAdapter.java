package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.MessageUser;
import com.example.vietcuisine.ui.message.ChatDetailActivity;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.UserViewHolder> {

    private Context context;
    private List<MessageUser> userList;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(MessageUser user);
    }

    public MessageListAdapter(Context context, List<MessageUser> userList,OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_message, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        MessageUser user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.statusTextView.setText(user.getLastMessage());


        Glide.with(context).load(user.getAvatar()).into(holder.avatarImageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, statusTextView;
        ImageView avatarImageView;
        CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameofuser);
            statusTextView = itemView.findViewById(R.id.statusofuser);
            avatarImageView = itemView.findViewById(R.id.imageviewofuser);
            cardView = itemView.findViewById(R.id.cardviewofuser);
        }
    }
}
