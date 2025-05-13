package com.pbde401.studyworks.ui.employer.chats;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.ui.common.BaseChatListAdapter;
import com.pbde401.studyworks.ui.employer.chats.EmployerChatListAdapter;
import com.pbde401.studyworks.util.AuthManager;

public class EmployerChatsFragment extends Fragment implements BaseChatListAdapter.OnChatClickListener {
    private EmployerChatsViewModel viewModel;
    private AuthManager authManager;
    private EmployerChatListAdapter adapter;
    private RecyclerView rvChats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EmployerChatsViewModel.class);
        
        authManager = AuthManager.getInstance();
        authManager.getCurrentUser().observe(this, user -> {
            if (user != null) viewModel.loadChats(user);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer_chats, container, false);
        
        rvChats = view.findViewById(R.id.rvEmployerChats);
        rvChats.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new EmployerChatListAdapter(this);
        rvChats.setAdapter(adapter);

        viewModel.getChats().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null && !chats.isEmpty()) {
                adapter.setChats(chats);
                rvChats.setVisibility(View.VISIBLE);
            } else {
                rvChats.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onChatClick(Chat chat) {
        Intent intent = new Intent(requireContext(), EmployerChatActivity.class);
        intent.putExtra("chatId", chat.getId());
        startActivity(intent);
    }
}