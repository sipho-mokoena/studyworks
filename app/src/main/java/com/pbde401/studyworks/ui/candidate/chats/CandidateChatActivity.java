package com.pbde401.studyworks.ui.candidate.chats;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.pbde401.studyworks.R;

public class CandidateChatActivity extends AppCompatActivity {
    private ChatViewModel viewModel;
    private MessagesAdapter adapter;
    private EditText messageInput;
    private RecyclerView messagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_chat);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat");

        // Initialize views
        messageInput = findViewById(R.id.messageInput);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        MaterialButton sendButton = findViewById(R.id.sendButton);

        // Setup RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(layoutManager);
        adapter = new MessagesAdapter();
        messagesRecyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // Get chat ID from intent
        String chatId = getIntent().getStringExtra("chatId");
        if (chatId != null) {
            viewModel.loadMessages(chatId);
        }

        // Observe messages
        viewModel.getMessages().observe(this, messages -> {
            adapter.setMessages(messages);
            messagesRecyclerView.scrollToPosition(messages.size() - 1);
        });

        // Setup send button
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                viewModel.sendMessage(message);
                messageInput.setText("");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}