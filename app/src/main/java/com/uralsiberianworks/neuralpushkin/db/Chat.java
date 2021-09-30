package com.uralsiberianworks.neuralpushkin.db;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.UUID;

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
}