package com.pbde401.studyworks.ui.candidate.chats;

import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.Employer;
import com.pbde401.studyworks.data.models.EmployerProfile;
import com.pbde401.studyworks.ui.common.BaseChatListAdapter;

public class CandidateChatListAdapter extends BaseChatListAdapter {

    public CandidateChatListAdapter(OnChatClickListener listener) {
        super(listener);
    }

    @Override
    protected void loadUserData(Chat chat, ChatViewHolder holder) {
        userRepository.getUserById(chat.getEmployerId())
            .addOnSuccessListener(user -> {
                if (user instanceof Employer) {
                    Employer employer = (Employer) user;
                    EmployerProfile employerProfile = employer.getProfile();
                    String companyName = employerProfile.getCompanyName();
                    holder.tvContactCharacter.setText(companyName.substring(0, 1).toUpperCase());
                    holder.tvEmployerName.setText(companyName);
                }
            });
    }
}