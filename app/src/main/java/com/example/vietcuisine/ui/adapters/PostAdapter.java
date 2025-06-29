package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.data.model.Comment;
import com.example.vietcuisine.data.model.CommentRequest;
import com.example.vietcuisine.data.model.Post;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.utils.DateTimeUtils;
import com.example.vietcuisine.utils.SharedPrefsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private Context context;
    private OnPostInteractionListener listener;
    private String userId;

    public interface OnPostInteractionListener {
        void onPostClick(Post post);
        void onLikeClick(Post post);
        void onCommentClick(Post post);
    }

    public PostAdapter(Context context, List<Post> posts, OnPostInteractionListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        this.userId = SharedPrefsManager.getInstance().getUserId();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.commentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.commentAdapter = new CommentAdapter(context, new ArrayList<>(),userId);
        holder.commentRecyclerView.setAdapter(holder.commentAdapter);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView userAvatar, postImage, likeButton, commentButton;
        private TextView userName, postTime, postContent, likesCount, commentsCount, viewMoreComments;
        private EditText commentEditText;
        private ImageButton sendCommentButton;
        private View commentSection;
        public RecyclerView commentRecyclerView;
        public CommentAdapter commentAdapter;
        private List<Comment> allComments = new ArrayList<>();


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

            commentEditText = itemView.findViewById(R.id.commentEditText);
            sendCommentButton = itemView.findViewById(R.id.sendCommentButton);
            commentSection = itemView.findViewById(R.id.commentSection);
            commentRecyclerView = itemView.findViewById(R.id.commentRecyclerView);
            viewMoreComments = itemView.findViewById(R.id.showMoreCommentsButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onPostClick(posts.get(position));
                }
            });

            likeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onLikeClick(posts.get(position));
                }
            });

            commentButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                Post post = posts.get(position);

                if (commentSection.getVisibility() == View.VISIBLE) {
                    commentSection.setVisibility(View.GONE);
                } else {
                    commentSection.setVisibility(View.VISIBLE);

                    ApiService apiService = ApiClient.getClient().create(ApiService.class);
                    apiService.getCommentsByTarget(post.getId(), "posts").enqueue(new Callback<List<Comment>>() {
                        @Override
                        public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                allComments = response.body();

                                boolean showMore = allComments.size() > 5;
                                List<Comment> initial = showMore
                                        ? allComments.subList(0, 5)
                                        : new ArrayList<>(allComments);

                                commentAdapter.setComments(initial);

                                viewMoreComments.setVisibility(showMore ? View.VISIBLE : View.GONE);
                                viewMoreComments.setText("Xem thêm");

                                viewMoreComments.setOnClickListener(toggleView -> {
                                    boolean isExpanded = viewMoreComments.getText().toString().equals("Rút gọn");
                                    if (isExpanded) {
                                        commentAdapter.setComments(allComments.subList(0, 5));
                                        viewMoreComments.setText("Xem thêm");
                                    } else {
                                        commentAdapter.setComments(allComments);
                                        viewMoreComments.setText("Rút gọn");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Comment>> call, Throwable t) {
                            Toast.makeText(context, "Không thể tải bình luận", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            sendCommentButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                String content = commentEditText.getText().toString().trim();
                if (content.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show();
                    return;
                }

                Post post = posts.get(position);
                CommentRequest request = new CommentRequest(content, post.getId(), "posts");
                ApiService apiService = ApiClient.getClient().create(ApiService.class);

                apiService.createComment(request).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đã bình luận", Toast.LENGTH_SHORT).show();
                            commentEditText.setText("");
                            post.setCommentsCount(post.getCommentsCount() + 1);
                            commentsCount.setText(String.valueOf(post.getCommentsCount()));


                            // 👉 Tạo comment mới
                            Comment newComment = new Comment();
                            newComment.setContent(content);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String currentTime = sdf.format(new Date());

                            newComment.setCreateAt(currentTime);

                            newComment.setUserId(post.getUserId());

                            // Thêm vào danh sách gốc
                            allComments.add(0, newComment);

                            // Cập nhật hiển thị: nếu đang ở chế độ xem 5 thì cắt
                            boolean isExpanded = viewMoreComments.getText().toString().equals("Rút gọn");
                            if (isExpanded) {
                                commentAdapter.setComments(allComments);
                            } else {
                                commentAdapter.setComments(
                                        allComments.size() > 5 ? allComments.subList(0, 5) : new ArrayList<>(allComments)
                                );
                            }

                            viewMoreComments.setVisibility(allComments.size() > 5 ? View.VISIBLE : View.GONE);
                        } else {
                            Toast.makeText(context, "Lỗi gửi bình luận", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(context, "Không thể gửi bình luận", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
            public void bind(Post post) {
            userName.setText(post.getUserId() != null && post.getUserId().getName() != null
                    ? post.getUserId().getName() : "Ẩn danh");

            if (post.getUserId() != null && post.getUserId().getAvatar() != null) {
                Glide.with(itemView.getContext())
                        .load(post.getUserId().getAvatar())
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .into(userAvatar);
            } else {
                userAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
            }

            postContent.setText(post.getCaption());
            postTime.setText(DateTimeUtils.formatToVietnamTime(post.getCreatedAt()));
            likesCount.setText(String.valueOf(post.getLikesCount()));
            commentsCount.setText(String.valueOf(post.getCommentsCount()));
            likeButton.setImageResource(post.isLiked() ? R.drawable.ic_favorite_active : R.drawable.ic_favorite_inactive);

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

            commentSection.setVisibility(View.GONE);
            commentEditText.setText("");
        }
    }
}
