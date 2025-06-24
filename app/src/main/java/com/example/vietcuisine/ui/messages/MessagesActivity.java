package com.example.vietcuisine.ui.messages;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vietcuisine.R;

public class MessagesActivity extends AppCompatActivity {
    
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}
