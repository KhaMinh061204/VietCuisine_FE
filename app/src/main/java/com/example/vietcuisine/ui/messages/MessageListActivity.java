package com.example.vietcuisine.ui.messages;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
//        SharedPreferences prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE);
//        currentUserId = prefs.getString("user_id", null);
        currentUserId= SharedPrefsManager.getInstance().getUserId();
        Log.d("currentUser","userId"+currentUserId);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageListAdapter(this, userList);
        recyclerViewUsers.setAdapter(adapter);
        apiService = ApiClient.getClient().create(ApiService.class);
        ImageButton backButton = findViewById(R.id.backbuttonofspecificchat);
        backButton.setOnClickListener(v -> finish());
        fetchConversations();
    }

    private void fetchConversations() {
        Log.d("UserId message","message"+currentUserId);
        apiService.getConversations(currentUserId).enqueue(new Callback<List<MessageUser>>() {
            @Override
            public void onResponse(Call<List<MessageUser>> call, Response<List<MessageUser>> response) {
                Log.d("Response mes","mes"+response);
                if (response.isSuccessful() && response.body() != null) {
                    for (MessageUser user : response.body()) {
                        Log.d("MessageListActivity", "User: " + user.getName() + " - ID: " + user.getUserId());
                    }
                    userList.clear();
                    userList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MessageListActivity.this, "Không lấy được cuộc trò chuyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MessageUser>> call, Throwable t) {
                Log.e("MessageListActivity", "Lỗi mạng: " + t.getMessage(), t);
                Toast.makeText(MessageListActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
