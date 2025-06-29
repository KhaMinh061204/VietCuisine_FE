package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderId().equals(currentUserId)) {
            return TYPE_SENT;
        } else {
            return TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message_sender, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message_receive, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).messageText.setText(message.getText());
            ((SentViewHolder) holder).timeText.setText(formatTime(message.getCreatedAt()));
        } else {
            ((ReceivedViewHolder) holder).messageText.setText(message.getText());
            ((ReceivedViewHolder) holder).timeText.setText(formatTime(message.getCreatedAt()));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        SentViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.sendermessage);
            timeText = view.findViewById(R.id.timeofmessage);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        ReceivedViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.sendermessage);
            timeText = view.findViewById(R.id.timeofmessage);
        }
    }
    private String formatTime(String isoDateTime) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = isoFormat.parse(isoDateTime);

            SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            hourMinuteFormat.setTimeZone(TimeZone.getDefault());

            return hourMinuteFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
