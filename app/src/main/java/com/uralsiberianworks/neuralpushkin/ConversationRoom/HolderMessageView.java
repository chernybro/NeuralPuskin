package com.uralsiberianworks.neuralpushkin.ConversationRoom;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.R;




public class HolderMessageView extends RecyclerView.ViewHolder {

    private final TextView chatText;


    public HolderMessageView(View v) {
        super(v);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
    }

    public TextView getChatText() {
        return chatText;
    }
}
