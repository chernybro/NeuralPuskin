package com.uralsiberianworks.neuralpushkin.recyclerConversation;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.R;


/**
 * Created by Dytstudio.
 */

public class HolderMe extends RecyclerView.ViewHolder {

    private TextView time, chatText;

    public HolderMe(View v) {
        super(v);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getChatText() {
        return chatText;
    }

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }
}
