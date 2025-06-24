package com.example.vietcuisine.ui.comments;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Comment;
import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private TextView titleText;
    private RecyclerView commentsRecyclerView;
    private List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        
        initViews();
        setupClickListeners();
        setupRecyclerView();
        loadComments();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleText = findViewById(R.id.titleText);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set adapter when CommentsAdapter is created
    }

    private void loadComments() {
        String targetId = getIntent().getStringExtra("target_id");
        String onModel = getIntent().getStringExtra("on_model");
        // TODO: Load comments from API
    }
}
