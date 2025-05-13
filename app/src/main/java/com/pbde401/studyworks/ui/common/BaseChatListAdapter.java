package com.pbde401.studyworks.ui.common;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseChatListAdapter extends RecyclerView.Adapter<BaseChatListAdapter.ChatViewHolder> {
    protected List<Chat> chats = new ArrayList<>();
    protected final UserRepository userRepository;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    protected final OnChatClickListener listener;

    public BaseChatListAdapter(OnChatClickListener listener) {
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
        Chat chat = chats.get(position);
        bindViewHolder(holder, chat);
    }

    private void bindViewHolder(ChatViewHolder holder, Chat chat) {
        loadUserData(chat, holder);
        
        String lastMessage = chat.getLastMessage();
        holder.tvLastMessage.setText(lastMessage != null ? lastMessage : "No messages yet");
        
        if (chat.getLastMessageAt() != null) {
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                chat.getLastMessageAt().getTime(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            );
            holder.tvLastMessageTime.setText(timeAgo);
        } else {
            holder.tvLastMessageTime.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    protected abstract void loadUserData(Chat chat, ChatViewHolder holder);

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvContactCharacter;
        public final TextView tvEmployerName;
        public final TextView tvLastMessage;
        public final TextView tvLastMessageTime;

        public ChatViewHolder(View itemView) {
            super(itemView);
            tvContactCharacter = itemView.findViewById(R.id.tvContactCharacter);
            tvEmployerName = itemView.findViewById(R.id.tvEmployerName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
        }
    }
}
