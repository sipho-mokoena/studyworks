package com.pbde401.studyworks.ui.employer.chats;

import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.ui.common.BaseChatListAdapter;
import com.pbde401.studyworks.data.models.Chat;

public class EmployerChatListAdapter extends BaseChatListAdapter {

    public EmployerChatListAdapter(OnChatClickListener listener) {
        super(listener);
    }

    @Override
    protected void loadUserData(Chat chat, ChatViewHolder holder) {
        userRepository.getUserById(chat.getCandidateId())
            .addOnSuccessListener(user -> {
                if (user instanceof Candidate) {
                    Candidate candidate = (Candidate) user;
                    String fullName = user.getFullName();
                    holder.tvContactCharacter.setText(fullName.substring(0, 1).toUpperCase());
                    holder.tvEmployerName.setText(fullName);
                }
            });
    }
}
