package com.example.vietcuisine.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Comment;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.data.model.ReportRequest;
import com.example.vietcuisine.data.model.CommentRequest;
import com.example.vietcuisine.utils.DateTimeUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    private List<Comment> comments;
    private final String currentUserId;
    private final OnCommentActionListener commentActionListener;
    private int replyingPosition = -1;

    public interface OnCommentActionListener {
        void onReply(Comment parentComment);
    }

    public CommentAdapter(Context context, List<Comment> comments, String currentUserId, OnCommentActionListener listener) {
        this.context = context;
        this.comments = comments;
        this.currentUserId = currentUserId;
        this.commentActionListener = listener;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        // Hiển thị avatar và tên
        if (comment.getUserId() != null) {
            holder.commentUserName.setText(comment.getUserId().getName());
            Glide.with(context)
                    .load(comment.getUserId().getAvatar())
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .into(holder.commentAvatar);
        } else {
            holder.commentUserName.setText("Ẩn danh");
            holder.commentAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
        }

        holder.commentContent.setText(comment.getContent());
        holder.commentCreatedAt.setText(DateTimeUtils.formatToVietnamTime(comment.getCreateAt()));
        holder.replyBox.setVisibility(position == replyingPosition ? View.VISIBLE : View.GONE);

        // Nút trả lời
        holder.commentReplyButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            replyingPosition = (replyingPosition == currentPosition) ? -1 : currentPosition;
            notifyDataSetChanged();

            if (commentActionListener != null) {
                commentActionListener.onReply(comments.get(currentPosition));
            }
        });

        // Gửi phản hồi
        holder.sendReplyButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            Comment parent = comments.get(currentPosition);
            String replyText = holder.replyEditText.getText().toString().trim();
            if (replyText.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập nội dung phản hồi", Toast.LENGTH_SHORT).show();
                return;
            }

            CommentRequest replyRequest = new CommentRequest(
                    replyText,
                    parent.getTargetId(),
                    parent.getOnModel(),
                    parent.getId()
            );

            ApiClient.getClient().create(ApiService.class)
                    .createComment(replyRequest)
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Đã phản hồi", Toast.LENGTH_SHORT).show();

                                // 1. Tạo đối tượng Comment giả lập từ dữ liệu đã gửi
                                Comment reply = new Comment();
                                reply.setContent(replyText);
                                reply.setParentId(parent.getId());
                                reply.setCreateAt(String.valueOf(System.currentTimeMillis()));


                                // 2. Gán thông tin người dùng (từ local nếu có)
                                reply.setUserId(parent.getUserId()); // hoặc currentUser nếu bạn có biến user hiện tại

                                // 3. Thêm vào danh sách replies của comment cha
                                if (parent.getReplies() != null) {
                                    parent.getReplies().add(reply);
                                }

                                // 4. Xóa ô nhập và cập nhật lại item vừa thay đổi
                                holder.replyEditText.setText("");
                                replyingPosition = -1;
                                notifyItemChanged(currentPosition);
                            } else {
                                Toast.makeText(context, "Lỗi khi phản hồi", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(context, "Không thể phản hồi", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Tùy chọn cho comment chính
        holder.commentOptionsButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                showCommentOptions(comments.get(currentPosition), holder.commentOptionsButton, currentPosition);
            }
        });

        // Hiển thị danh sách phản hồi
        holder.replyContainer.removeAllViews();
        for (Comment reply : comment.getReplies()) {
            View replyView = LayoutInflater.from(context).inflate(R.layout.item_comment_reply, holder.replyContainer, false);

            TextView replyUser = replyView.findViewById(R.id.replyUserName);
            TextView replyContent = replyView.findViewById(R.id.replyContent);
            TextView replyTime = replyView.findViewById(R.id.replyCreatedAt);
            ImageView replyAvatar = replyView.findViewById(R.id.replyAvatar);
            ImageButton replyOptionsButton = replyView.findViewById(R.id.replyOptionsButton);

            if (reply.getUserId() != null) {
                replyUser.setText(reply.getUserId().getName());
                Glide.with(context)
                        .load(reply.getUserId().getAvatar())
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .into(replyAvatar);
            } else {
                replyUser.setText("Ẩn danh");
                replyAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
            }

            replyContent.setText(reply.getContent());
            replyTime.setText(DateTimeUtils.formatToVietnamTime(reply.getCreateAt()));

            replyOptionsButton.setOnClickListener(v -> {
                showCommentOptions(reply, replyOptionsButton, -1);
            });

            holder.replyContainer.addView(replyView);
        }
    }

    private void showCommentOptions(Comment comment, View anchorView, int position) {
        PopupMenu popup = new PopupMenu(context, anchorView);
        popup.getMenuInflater().inflate(R.menu.comment_options_menu, popup.getMenu());

        if (comment.getUserId() == null || !comment.getUserId().getId().equals(currentUserId)) {
            popup.getMenu().findItem(R.id.menu_delete_comment).setVisible(false);
        } else {
            popup.getMenu().findItem(R.id.menu_report_comment).setVisible(false);
        }

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete_comment) {
                showDeleteConfirmationDialog(comment, position);
                return true;
            } else if (item.getItemId() == R.id.menu_report_comment) {
                reportComment(comment);
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void showDeleteConfirmationDialog(Comment comment, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa bình luận")
                .setMessage("Bạn có chắc muốn xóa bình luận này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteComment(comment, position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteComment(Comment comment, int position) {
        ApiClient.getClient().create(ApiService.class)
                .deleteComment(comment.getId())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            if (position >= 0 && position < comments.size()) {
                                comments.remove(position);
                                notifyItemRemoved(position);
                            }
                            Toast.makeText(context, "Đã xóa bình luận", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi khi xóa bình luận", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(context, "Không thể xóa bình luận", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void reportComment(Comment comment) {
        SharedPreferences prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) {
            Toast.makeText(context, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ReportRequest reportRequest = new ReportRequest("comments", comment.getId(), "Nội dung không phù hợp");

        ApiClient.getClient().create(ApiService.class)
                .reportComment(reportRequest)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đã báo cáo bình luận", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi khi báo cáo", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(context, "Không thể báo cáo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView commentAvatar;
        TextView commentUserName, commentContent, commentCreatedAt, commentReplyButton;
        ImageButton commentOptionsButton, sendReplyButton;
        LinearLayout replyBox, replyContainer;
        EditText replyEditText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAvatar = itemView.findViewById(R.id.commentAvatar);
            commentUserName = itemView.findViewById(R.id.commentUserName);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentCreatedAt = itemView.findViewById(R.id.commentCreatedAt);
            commentReplyButton = itemView.findViewById(R.id.commentReplyButton);
            commentOptionsButton = itemView.findViewById(R.id.commentOptionsButton);
            replyBox = itemView.findViewById(R.id.replyBox);
            replyEditText = itemView.findViewById(R.id.replyEditText);
            sendReplyButton = itemView.findViewById(R.id.sendReplyButton);
            replyContainer = itemView.findViewById(R.id.replyContainer);
        }
    }
}
