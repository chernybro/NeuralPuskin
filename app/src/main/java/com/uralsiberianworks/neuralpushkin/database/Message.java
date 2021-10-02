package com.uralsiberianworks.neuralpushkin.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(
        entity = Chat.class,
        parentColumns = "chatID",
        childColumns = "chatID", onDelete = CASCADE))
public class Message {
    @PrimaryKey @NonNull
    String messageID;
    String text;
    String chatID;
    String type;
    private int initialLength;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChatID() {return chatID; }

    public void setChatID(String chatID) { this.chatID = chatID; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public int getInitialLength() { return initialLength; }

    public void setInitialLength(int initialLength) { this.initialLength = initialLength; }
}
