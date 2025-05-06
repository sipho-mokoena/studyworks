package com.pbde401.studyworks.ui.candidate.chats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.repository.ChatsRepository;

import java.util.List;

public class CandidateChatsViewModel extends ViewModel {
    private final ChatsRepository chatsRepository;
    private LiveData<List<Chat>> chats;

    public CandidateChatsViewModel() {
        this.chatsRepository = new ChatsRepository();
        loadChats();
    }

    private void loadChats() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chats = chatsRepository.getCandidateChats(currentUserId);
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }
}
