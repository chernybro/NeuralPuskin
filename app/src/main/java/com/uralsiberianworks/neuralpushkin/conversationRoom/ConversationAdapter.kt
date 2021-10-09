package com.uralsiberianworks.neuralpushkin.conversationRoom

import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.uralsiberianworks.neuralpushkin.R
import com.uralsiberianworks.neuralpushkin.database.Message
import java.io.File

class ConversationAdapter // Provide a suitable constructor (depends on the kind of dataset)
(// The items to display in your RecyclerView
        private var items: MutableList<Message?>?, val recipientImagePath: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val YOU = 1
    private val ME = 2
    private val TYPING = 3

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return items?.size!!
    }

    fun updateLastMessage(items: MutableList<Message?>?) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        //More to come
        when (items?.get(position)?.type) {
            "1" -> return YOU
            "2" -> return ME
            "3" -> return TYPING
        }
        return -1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)
        viewHolder = when (viewType) {
            YOU -> {
                val v2 = inflater.inflate(R.layout.layout_holder_you, viewGroup, false)
                val imageView = v2.findViewById<ImageView>(R.id.recipient_img)
                val imgFile = File(recipientImagePath)
                if (imgFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    imageView.setImageBitmap(myBitmap)
                }
                HolderMessageView(v2)
            }
            ME -> {
                val v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false)
                HolderMessageView(v)
            }
            TYPING -> {
                val v4 = inflater.inflate(R.layout.layout_holder_typing, viewGroup, false)
                HolderMessageView(v4)
            }
            else -> {
                val v4 = inflater.inflate(R.layout.layout_holder_typing, viewGroup, false)
                HolderMessageView(v4)
            }
        }
        return viewHolder
    }

    fun addItem(item: List<Message>?) {
        items?.addAll(item!!)
        notifyDataSetChanged()
    }

    fun delItem(message: Message) {
        items?.remove(message)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            YOU -> {
                val vh2 = viewHolder as HolderMessageView
                configureViewHolderYou(vh2, position)
            }
            ME -> {
                val vh = viewHolder as HolderMessageView
                configureViewHolderMe(vh, position)
            }
            else -> {
                val vh3 = viewHolder as HolderMessageView
                configureViewHolderTyping(vh3, position)
            }
        }
    }

    private fun configureViewHolderMe(vh1: HolderMessageView, position: Int) {
        vh1.chatText.text = items?.get(position)?.text ?: ""
    }

    private fun configureViewHolderYou(vh1: HolderMessageView, position: Int) {
        val sb = SpannableStringBuilder(items?.get(position)?.text)
        val fcs = ForegroundColorSpan(Color.rgb(158, 158, 158))
        val colorLength = items?.get(position)?.initialLength
        sb.setSpan(fcs, 0, colorLength!!, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        vh1.chatText.text = sb
    }

    private fun configureViewHolderTyping(vh1: HolderMessageView, position: Int) {
        vh1.chatText.text = items?.get(position)?.text ?: ""
    }

}