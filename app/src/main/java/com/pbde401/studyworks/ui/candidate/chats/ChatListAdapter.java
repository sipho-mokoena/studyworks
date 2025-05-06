package com.pbde401.studyworks.ui.candidate.chats;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;
import com.pbde401.studyworks.R;
import com.pbde401.studyworks.data.models.Chat;
import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private List<Chat> chats = new ArrayList<>();
    private final OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatListAdapter(OnChatClickListener listener) {
        this.listener = listener;
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
        private final ShapeableImageView ivEmployerAvatar;
        private final TextView tvEmployerName;
        private final TextView tvLastMessage;
        private final TextView tvLastMessageTime;

        ChatViewHolder(View itemView) {
            super(itemView);
            ivEmployerAvatar = itemView.findViewById(R.id.ivEmployerAvatar);
            tvEmployerName = itemView.findViewById(R.id.tvEmployerName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
        }

        void bind(Chat chat) {
            // TODO: Load employer name from UserRepository
            tvEmployerName.setText("Company Name"); // Placeholder
            
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