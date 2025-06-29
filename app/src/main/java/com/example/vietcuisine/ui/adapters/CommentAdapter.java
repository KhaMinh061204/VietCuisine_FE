package com.example.vietcuisine.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.example.vietcuisine.utils.DateTimeUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private Context context;
    private String currentUserId;

    public CommentAdapter(Context context, List<Comment> comments, String currentUserId) {
        this.context = context;
        this.comments = comments;
        this.currentUserId = currentUserId;
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
        holder.userName.setText(comment.getUserId().getName());
        holder.content.setText(comment.getContent());
        holder.createAt.setText(DateTimeUtils.formatToVietnamTime(comment.getCreateAt()));
        Glide.with(context)
                .load(comment.getUserId().getAvatar())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(holder.avatar);

        holder.optionsButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.optionsButton);
            popup.getMenuInflater().inflate(R.menu.comment_options_menu, popup.getMenu());

            Log.d("test","currentUser"+currentUserId);
            if (!comment.getUserId().getId().equals(currentUserId)) {
                popup.getMenu().findItem(R.id.menu_delete_comment).setVisible(false);
            }else{popup.getMenu().findItem(R.id.menu_report_comment).setVisible(false);}

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
        });
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
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.deleteComment(comment.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    comments.remove(position);
                    notifyItemRemoved(position);
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

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.reportComment(reportRequest).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("Report", "report mes"+response);
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
        return comments.size();
    }

    public void setComments(List<Comment> newComments) {
        this.comments = newComments;
        notifyDataSetChanged();
    }
    public void addCommentAtTop(Comment comment) {
        comments.add(0, comment); // thêm lên đầu danh sách
        notifyItemInserted(0);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView userName, content, createAt;
        ImageButton optionsButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.commentAvatar);
            userName = itemView.findViewById(R.id.commentUserName);
            content = itemView.findViewById(R.id.commentContent);
            createAt = itemView.findViewById(R.id.commentCreatedAt);
            optionsButton = itemView.findViewById(R.id.commentOptionsButton);
        }
    }
}
