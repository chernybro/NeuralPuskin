package com.uralsiberianworks.neuralpushkin.recyclerConversation;


import androidx.annotation.DrawableRes;

public class ChatData {
    String type, text;
    @DrawableRes int image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
