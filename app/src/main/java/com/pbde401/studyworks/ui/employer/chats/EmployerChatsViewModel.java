package com.pbde401.studyworks.ui.employer.chats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.User;
import com.pbde401.studyworks.data.repository.ChatsRepository;

import java.util.ArrayList;
import java.util.List;

public class EmployerChatsViewModel extends ViewModel {
    private final ChatsRepository chatsRepository;
    private final MutableLiveData<List<Chat>> _chats = new MutableLiveData<>();
    private final LiveData<List<Chat>> chats = _chats;

    public EmployerChatsViewModel() {
        this.chatsRepository = new ChatsRepository();
    }

    public void loadChats(User user) {
        String currentUserId = user.getId();
        chatsRepository.getEmployerChats(currentUserId).observeForever(chatsList -> {
            _chats.setValue(chatsList != null ? chatsList : new ArrayList<>());
        });
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }
}
