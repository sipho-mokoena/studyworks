package com.pbde401.studyworks.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pbde401.studyworks.data.models.Chat;
import com.pbde401.studyworks.data.models.Message;
import com.pbde401.studyworks.data.models.enums.UserRole;
import java.util.*;

public class ChatsRepository {
    private static final String CHATS_COLLECTION = "chats";
    private static final String MESSAGES_COLLECTION = "messages";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<Chat> createChat(Chat chat) {
        MutableLiveData<Chat> chatLiveData = new MutableLiveData<>();
        
        db.collection(CHATS_COLLECTION)
            .document(chat.getId())
            .set(chatToMap(chat))
            .addOnSuccessListener(aVoid -> chatLiveData.setValue(chat))
            .addOnFailureListener(e -> chatLiveData.setValue(null));

        return chatLiveData;
    }

    public LiveData<Chat> getChat(String chatId) {
        MutableLiveData<Chat> chatLiveData = new MutableLiveData<>();

        db.collection(CHATS_COLLECTION)
            .document(chatId)
            .get()
            .addOnSuccessListener(document -> {
                if (document.exists()) {
                    chatLiveData.setValue(documentToChat(document));
                } else {
                    chatLiveData.setValue(null);
                }
            })
            .addOnFailureListener(e -> chatLiveData.setValue(null));

        return chatLiveData;
    }

    public LiveData<Chat> getChatByEmployerIdAndCandidateId(String employerId, String candidateId) {
        MutableLiveData<Chat> chatLiveData = new MutableLiveData<>();

        db.collection(CHATS_COLLECTION)
            .whereEqualTo("employerId", employerId)
            .whereEqualTo("candidateId", candidateId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    chatLiveData.setValue(documentToChat(queryDocumentSnapshots.getDocuments().get(0)));
                } else {
                    chatLiveData.setValue(null);
                }
            })
            .addOnFailureListener(e -> chatLiveData.setValue(null));

        return chatLiveData;
    }

    public void updateChat(String chatId, Map<String, Object> updates) {
        db.collection(CHATS_COLLECTION)
            .document(chatId)
            .update(updates);
    }

    public LiveData<List<Chat>> getCandidateChats(String candidateId) {
        MutableLiveData<List<Chat>> chatsLiveData = new MutableLiveData<>();

        db.collection(CHATS_COLLECTION)
            .whereEqualTo("candidateId", candidateId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Chat> chats = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    chats.add(documentToChat(document));
                }
                chatsLiveData.setValue(chats);
            })
            .addOnFailureListener(e -> chatsLiveData.setValue(new ArrayList<>()));

        return chatsLiveData;
    }

    public LiveData<List<Chat>> getEmployerChats(String employerId) {
        MutableLiveData<List<Chat>> chatsLiveData = new MutableLiveData<>();

        db.collection(CHATS_COLLECTION)
            .whereEqualTo("employerId", employerId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Chat> chats = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    chats.add(documentToChat(document));
                }
                chatsLiveData.setValue(chats);
            })
            .addOnFailureListener(e -> chatsLiveData.setValue(new ArrayList<>()));

        return chatsLiveData;
    }

    public LiveData<Chat> findOrCreateChat(String employerId, String candidateId) {
        MutableLiveData<Chat> chatLiveData = new MutableLiveData<>();

        getChatByEmployerIdAndCandidateId(employerId, candidateId)
            .observeForever(existingChat -> {
                if (existingChat != null) {
                    chatLiveData.setValue(existingChat);
                } else {
                    // Create new chat
                    Date now = new Date();
                    String chatId = UUID.randomUUID().toString();
                    Chat newChat = new Chat(chatId, now, now, candidateId, employerId);
                    createChat(newChat).observeForever(chatLiveData::setValue);
                }
            });

        return chatLiveData;
    }

    // Message operations
    public LiveData<Message> createMessage(Message message) {
        MutableLiveData<Message> messageLiveData = new MutableLiveData<>();

        db.collection(MESSAGES_COLLECTION)
            .document(message.getId())
            .set(messageToMap(message))
            .addOnSuccessListener(aVoid -> {
                // Update chat's last message info
                Map<String, Object> updates = new HashMap<>();
                updates.put("lastMessage", message.getContent());
                updates.put("lastMessageAt", message.getTimestamp());
                updateChat(message.getChatId(), updates);
                messageLiveData.setValue(message);
            })
            .addOnFailureListener(e -> messageLiveData.setValue(null));

        return messageLiveData;
    }

    public LiveData<List<Message>> getChatMessages(String chatId) {
        MutableLiveData<List<Message>> messagesLiveData = new MutableLiveData<>();

        db.collection(MESSAGES_COLLECTION)
            .whereEqualTo("chatId", chatId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Message> messages = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    messages.add(documentToMessage(document));
                }
                messagesLiveData.setValue(messages);
            })
            .addOnFailureListener(e -> messagesLiveData.setValue(new ArrayList<>()));

        return messagesLiveData;
    }

    public LiveData<List<Message>> getRecentMessages(String chatId, int limit) {
        MutableLiveData<List<Message>> messagesLiveData = new MutableLiveData<>();

        db.collection(MESSAGES_COLLECTION)
            .whereEqualTo("chatId", chatId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Message> messages = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    messages.add(documentToMessage(document));
                }
                Collections.reverse(messages);
                messagesLiveData.setValue(messages);
            })
            .addOnFailureListener(e -> messagesLiveData.setValue(new ArrayList<>()));

        return messagesLiveData;
    }

    private Map<String, Object> chatToMap(Chat chat) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", chat.getId());
        map.put("jobId", chat.getJobId());
        map.put("candidateId", chat.getCandidateId());
        map.put("employerId", chat.getEmployerId());
        map.put("lastMessage", chat.getLastMessage());
        map.put("lastMessageAt", chat.getLastMessageAt());
        map.put("createdAt", chat.getCreatedAt());
        map.put("updatedAt", chat.getUpdatedAt());
        return map;
    }

    private Chat documentToChat(DocumentSnapshot document) {
        String id = document.getId();
        String jobId = document.getString("jobId");
        String candidateId = document.getString("candidateId");
        String employerId = document.getString("employerId");
        String lastMessage = document.getString("lastMessage");
        Date lastMessageAt = document.getDate("lastMessageAt");
        Date createdAt = document.getDate("createdAt");
        Date updatedAt = document.getDate("updatedAt");

        return new Chat(id, createdAt, updatedAt, jobId, candidateId, employerId, lastMessage, lastMessageAt);
    }

    private Map<String, Object> messageToMap(Message message) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message.getId());
        map.put("chatId", message.getChatId());
        map.put("senderId", message.getSenderId());
        map.put("senderRole", message.getSenderRole().toString());
        map.put("content", message.getContent());
        map.put("timestamp", message.getTimestamp());
        map.put("createdAt", message.getCreatedAt());
        map.put("updatedAt", message.getUpdatedAt());
        return map;
    }

    private Message documentToMessage(DocumentSnapshot document) {
        String id = document.getId();
        String chatId = document.getString("chatId");
        String senderId = document.getString("senderId");
        UserRole senderRole = UserRole.valueOf(document.getString("senderRole"));
        String content = document.getString("content");
        Date timestamp = document.getDate("timestamp");
        Date createdAt = document.getDate("createdAt");
        Date updatedAt = document.getDate("updatedAt");

        return new Message(id, createdAt, updatedAt, chatId, senderId, senderRole, content, timestamp);
    }
}
