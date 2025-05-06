package com.pbde401.studyworks.ui.candidate.chats;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.util.AuthManager;

public class CandidateChatsFragment extends Fragment implements ChatListAdapter.OnChatClickListener {
    private CandidateChatsViewModel viewModel;
    private AuthManager authManager;
    private ChatListAdapter adapter;
    private RecyclerView rvChats;

    public CandidateChatsFragment() {
        // Required empty public constructor
    }

    public static CandidateChatsFragment newInstance() {
        CandidateChatsFragment fragment = new CandidateChatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CandidateChatsViewModel.class);

        authManager = AuthManager.getInstance();
        authManager.getCurrentUser().observe(this, user -> {
            if (user != null) viewModel.loadChats(user);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_chats, container, false);
        
        rvChats = view.findViewById(R.id.rvChats);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rvChats.setLayoutManager(layoutManager);
        
        adapter = new ChatListAdapter(this);
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
        // TODO: Navigate to chat detail screen
        Bundle args = new Bundle();
        args.putString("chatId", chat.getId());
//        Navigation.findNavController(requireView())
//                .navigate(R.id.action_navigation_chats_to_chat_detail, args);
    }
}