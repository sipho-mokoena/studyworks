package com.pbde401.studyworks.ui.employer.chats;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.Candidate;
import com.pbde401.studyworks.data.models.CandidateProfile;
import com.pbde401.studyworks.data.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class EmployerChatListAdapter extends RecyclerView.Adapter<EmployerChatListAdapter.ChatViewHolder> {
    private List<Chat> chats = new ArrayList<>();
    private final OnEmployerChatClickListener listener;
    private final UserRepository userRepository;

    public interface OnEmployerChatClickListener {
        void onChatClick(Chat chat);
    }

    public EmployerChatListAdapter(OnEmployerChatClickListener listener) {
        this.listener = listener;
        this.userRepository = new UserRepository();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvContactCharacter;
        private final TextView tvEmployerName;
        private final TextView tvLastMessage;
        private final TextView tvLastMessageTime;

        ChatViewHolder(View itemView) {
            super(itemView);
            tvContactCharacter = itemView.findViewById(R.id.tvContactCharacter);
            tvEmployerName = itemView.findViewById(R.id.tvEmployerName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
        }

        void bind(Chat chat) {
            userRepository.getUserById(chat.getCandidateId())
                .addOnSuccessListener(user -> {
                    if (user instanceof Candidate) {
                        Candidate candidate = (Candidate) user;
                        String fullName = user.getFullName();
                        tvContactCharacter.setText(user.getFullName().substring(0, 1).toUpperCase());
                        tvEmployerName.setText(fullName);
                    }
                });
            
            String lastMessage = chat.getLastMessage();
            tvLastMessage.setText(lastMessage != null ? lastMessage : "No messages yet");
            
            if (chat.getLastMessageAt() != null) {
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    chat.getLastMessageAt().getTime(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                );
                tvLastMessageTime.setText(timeAgo);
            } else {
                tvLastMessageTime.setText("");
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChatClick(chat);
                }
            });
        }
    }
}
