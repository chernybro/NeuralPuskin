package com.uralsiberianworks.neuralpushkin.conversationRoom

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uralsiberianworks.neuralpushkin.R

class HolderMessageView(v: View) : RecyclerView.ViewHolder(v) {
    val chatText: TextView

    init {
        chatText = v.findViewById<View>(R.id.tv_chat_text) as TextView
    }
}