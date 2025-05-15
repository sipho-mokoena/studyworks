package com.pbde401.studyworks.ui.candidate.chats;

import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.Employer;
import com.pbde401.studyworks.data.models.EmployerProfile;
import com.pbde401.studyworks.ui.common.BaseChatListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CandidateChatListAdapter extends BaseChatListAdapter {
    private List<Chat> chats = new ArrayList<>();
    
    public CandidateChatListAdapter(OnChatClickListener listener) {
        super(listener);
    }
    
    public void setChats(List<Chat> chatList) {
        this.chats = chatList;
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount() {
        return chats.size();
    }
    
    @Override
    public Chat getChatAt(int position) {
        return chats.get(position);
    }

    // Remove @Override annotation as this method doesn't exist in the parent class with the same signature
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