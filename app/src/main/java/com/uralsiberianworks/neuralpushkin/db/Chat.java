package com.uralsiberianworks.neuralpushkin.db;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(
        entity = Contact.class,
        parentColumns = "contactID",
        childColumns = "chatID"))
public class Chat {
    @PrimaryKey @NonNull
    String chatID;
    String sender;
    String lastMessage;
    @DrawableRes int image;


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

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }
}
