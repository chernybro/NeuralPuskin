package com.uralsiberianworks.neuralpushkin.recyclerConversation;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.R;




public class HolderYou extends RecyclerView.ViewHolder {

    private TextView chatText;

    public HolderYou(View v) {
        super(v);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
    }

    public TextView getChatText() {
        return chatText;
    }

    public void setChatText(TextView chatText) {this.chatText = chatText; }
}
