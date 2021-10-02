package com.uralsiberianworks.neuralpushkin.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(
        entity = Contact.class,
                parentColumns = "contactID",
        childColumns = "chatID", onDelete = CASCADE))
public class Chat {
    @PrimaryKey @NonNull
    private String chatID;
    private String sender;
    private String lastMessage;
    private String imagePath;

    public Chat(@NonNull String chatID, String sender, String lastMessage, String imagePath) {
        this.chatID = chatID;
        this.sender = sender;
        this.lastMessage = lastMessage;
        this.imagePath = imagePath;
    }

    public Chat() {

    }

    public void setChatID(String chatID) { this.chatID = chatID; }

    public String getChatID() {
        return chatID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getLastMessage() { return lastMessage; }

    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String image) { this.imagePath = image; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return chatID.equals(chat.chatID);
    }
}