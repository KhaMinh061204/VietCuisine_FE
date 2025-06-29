package com.example.vietcuisine.ui.message;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Message;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.adapters.MessageAdapter;
import com.example.vietcuisine.utils.SharedPrefsManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private String currentUserId;
    private String receiverId;
    private int currentPage;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private Socket socket;
    private final String SOCKET_URL = "http://10.0.2.2:3001";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        currentUserId = SharedPrefsManager.getInstance().getUserId();
        // Lấy view
        TextView userNameTextView = findViewById(R.id.Nameofspecificuser);
        ImageView avatarImageView = findViewById(R.id.specificuserimageinimageview);

        // Nhận dữ liệu từ Intent
        String userName = getIntent().getStringExtra("user_name");
        String userAvatar = getIntent().getStringExtra("user_avatar");

        // Set dữ liệu
        userNameTextView.setText(userName);
        Glide.with(this).load(userAvatar).into(avatarImageView);

        ImageButton backButton = findViewById(R.id.backbuttonofspecificchat);
        backButton.setOnClickListener(v -> {
            setResult(RESULT_OK); // Trả về kết quả OK
            finish();
        });

        recyclerView = findViewById(R.id.recyclerviewofspecific);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageAdapter = new MessageAdapter(messageList, currentUserId);
        recyclerView.setAdapter(messageAdapter);

        receiverId = getIntent().getStringExtra("user_id");

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && !isLastPage && layoutManager != null &&
                        layoutManager.findFirstVisibleItemPosition() == 0) {

                    currentPage--; // Tải page cũ hơn
                    if (currentPage > 0) {
                        fetchMessages(currentPage, true);
                    } else {
                        isLastPage = true;
                    }
                }
            }
        });

        // Load tin nhắn từ API
        findLatestPage();
        try {
            IO.Options options = new IO.Options();
            socket = IO.socket(SOCKET_URL, options);
            socket.connect();

            // Join room
            JSONObject joinData = new JSONObject();
            joinData.put("userId", currentUserId);
            joinData.put("partnerId", receiverId);
            socket.emit("joinRoom", joinData);

            // Listen nhận tin nhắn
            socket.on("receiveMessage", args -> runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    String senderId = data.getString("senderId");
                    String receiverId = data.getString("receiverId");
                    String text = data.getString("text");
                    String createdAt = data.getString("createdAt");

                    Message message = new Message(senderId, receiverId, text, createdAt);
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }));

        } catch (URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
        // Bắt sự kiện gửi tin nhắn khi người dùng nhấn gửi
        ImageButton sendButton = findViewById(R.id.imageviewsendmessage);
        EditText inputMessage = findViewById(R.id.getmessage);
        sendButton.setOnClickListener(v -> {
            String text = inputMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                inputMessage.setText(""); // Xóa nội dung sau khi gửi
            }
        });

        // Lắng nghe tin nhắn mới từ server
        socket.on("receiveMessage", args -> runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            // Xử lý JSON và thêm vào messageList
        }));
    }

    private void sendMessage(String text) {
        JSONObject message = new JSONObject();
        try {
            message.put("senderId", currentUserId);
            message.put("receiverId", receiverId);
            message.put("text", text);
            socket.emit("sendMessage", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchMessages(int page, boolean appendToTop) {
        isLoading = true;
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMessagesBetweenUsers(currentUserId, receiverId, page).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<Message> newMessages = response.body();

                    if (newMessages.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

                    if (appendToTop) {
                        messageList.addAll(0, newMessages);
                        messageAdapter.notifyItemRangeInserted(0, newMessages.size());
                    } else {
                        messageList.clear();
                        messageList.addAll(newMessages);
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                isLoading = false;
                Log.e("ChatDetail", "Lỗi tải tin nhắn", t);
            }
        });
    }

    private void findLatestPage() {
        findPageRecursive(1);
    }

    private void findPageRecursive(int page) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMessagesBetweenUsers(currentUserId, receiverId, page).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Message> messages = response.body();
                    if (messages.isEmpty()) {
                        // Không còn trang nào → page - 1 là trang cuối
                        currentPage = page - 1;
                        fetchMessages(currentPage,false);
                    } else {
                        // Còn tin nhắn → thử trang kế tiếp
                        findPageRecursive(page + 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("ChatDetail", "Lỗi tìm trang cuối", t);
            }
        });
    }


}
