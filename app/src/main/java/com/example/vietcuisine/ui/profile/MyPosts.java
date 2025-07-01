package com.example.vietcuisine.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Post;
import com.example.vietcuisine.data.model.PostResponse;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.adapters.ProfilePostAdapter;
import com.example.vietcuisine.ui.adapters.ProfileRecipeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPosts extends Fragment {

    private RecyclerView recyclerView;
    private ProfilePostAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewMyPosts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        adapter = new ProfilePostAdapter(getContext(), postList, post -> {
            //Toast.makeText(getContext(), "Đã chọn: " + post.getCaption(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        loadMyPosts();

        return view;
    }

    private void loadMyPosts() {
        Call<List<Post>> call = apiService.getMyPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    if (posts != null) {
                        postList.clear();
                        postList.addAll(posts);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Không có dữ liệu trả về", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải bài viết", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
