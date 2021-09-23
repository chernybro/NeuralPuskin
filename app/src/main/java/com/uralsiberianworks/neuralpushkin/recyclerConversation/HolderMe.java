package com.uralsiberianworks.neuralpushkin.recyclerConversation;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.R;




public class HolderMe extends RecyclerView.ViewHolder {

    private TextView chatText;


    public HolderMe(View v) {
        super(v);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
    }

    public TextView getChatText() {
        return chatText;
    }

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }
}
