package com.pbde401.studyworks.ui.candidate.chats;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Chat;

public class CandidateChatsFragment extends Fragment implements ChatListAdapter.OnChatClickListener {
    private CandidateChatsViewModel viewModel;
    private ChatListAdapter adapter;
    private RecyclerView rvChats;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CandidateChatsFragment() {
        // Required empty public constructor
    }

    public static CandidateChatsFragment newInstance(String param1, String param2) {
        CandidateChatsFragment fragment = new CandidateChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CandidateChatsViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_chats, container, false);
        
        rvChats = view.findViewById(R.id.rvChats);
        adapter = new ChatListAdapter(this);
        rvChats.setAdapter(adapter);

        viewModel.getChats().observe(getViewLifecycleOwner(), chats -> {
            adapter.setChats(chats);
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