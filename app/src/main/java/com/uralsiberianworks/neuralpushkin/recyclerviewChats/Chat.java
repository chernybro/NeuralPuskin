package com.uralsiberianworks.neuralpushkin.recyclerviewChats;


public class Chat {

    private String mName;
    private String mLastChat;
    private String mTime;
    private int mImage;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLastChat() {
        return mLastChat;
    }

    public void setLastChat(String lastChat) {
        mLastChat = lastChat;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }
}