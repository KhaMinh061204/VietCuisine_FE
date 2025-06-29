package com.example.vietcuisine.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.MessageUser;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.ui.adapters.MessageListAdapter;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.message.ChatDetailActivity;
import com.example.vietcuisine.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private List<MessageUser> userList =  new ArrayList<>();
    private MessageListAdapter adapter;
    private ApiService apiService;
    private String currentUserId ;
    private static final int REQUEST_CODE_CHAT_DETAIL = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        currentUserId = SharedPrefsManager.getInstance().getUserId();
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageListAdapter(this, userList, user -> {
            Intent intent = new Intent(MessageListActivity.this, ChatDetailActivity.class);
            intent.putExtra("user_id", user.getUserId());
            intent.putExtra("user_name", user.getName());
            intent.putExtra("user_avatar", user.getAvatar());
            startActivityForResult(intent, REQUEST_CODE_CHAT_DETAIL);
        });
        recyclerViewUsers.setAdapter(adapter);
        apiService = ApiClient.getClient().create(ApiService.class);
        ImageButton backButton = findViewById(R.id.backbuttonofspecificchat);
        backButton.setOnClickListener(v -> finish());
        fetchConversations();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHAT_DETAIL) {
            fetchConversations(); // Gọi lại API khi quay về
        }
    }

    private void fetchConversations() {
        Log.d("UserId",currentUserId);
        apiService.getConversations(currentUserId).enqueue(new Callback<List<MessageUser>>() {
            @Override
            public void onResponse(Call<List<MessageUser>> call, Response<List<MessageUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (MessageUser user : response.body()) {
                        Log.d("MessageListActivity", "User: " + user.getName() + " - ID: " + user.getUserId());
                    }
                    userList.clear();
                    userList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MessageListActivity.this, "Không lấy được cuộc trò chuyện", Toast.LENGTH_SHORT).show();
                    Log.e("MessageListActivity", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MessageUser>> call, Throwable t) {
                Toast.makeText(MessageListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
