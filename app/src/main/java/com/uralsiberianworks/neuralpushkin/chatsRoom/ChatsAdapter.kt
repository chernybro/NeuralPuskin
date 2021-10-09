package com.uralsiberianworks.neuralpushkin.chatsRoom

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uralsiberianworks.neuralpushkin.NeuralApp
import com.uralsiberianworks.neuralpushkin.R
import com.uralsiberianworks.neuralpushkin.chatsRoom.ChatsAdapter.ChatViewHolder
import com.uralsiberianworks.neuralpushkin.chatsRoom.ChatsAdapter.ChatViewHolder.ClickListener
import com.uralsiberianworks.neuralpushkin.database.Chat
import com.uralsiberianworks.neuralpushkin.database.Contact
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase
import java.io.File

class ChatsAdapter(context: Context, private val clickListener: ClickListener) : RecyclerView.Adapter<ChatViewHolder>() {
    private var mArrayList: MutableList<Chat?>?
    private val db: NeuralDatabase = (context.applicationContext as NeuralApp).db
    private fun createPushkin() {
        if (initialBotCreated) {
            updateLastMessage()
        } else if (!db.chatDao?.checkPushkinExist("push")!!) {
            val pushkinContact = Contact()
            //String pushkinID = UUID.randomUUID().toString();
            val s = "push"
            pushkinContact.contactID = s
            val imageUri = Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + R.drawable.push6).toString()
            pushkinContact.imagePath = imageUri
            pushkinContact.name = "Александр Пушкин"
            db.contactDao?.insert(pushkinContact)
            val pushkinChat = Chat()
            pushkinChat.chatID = pushkinContact.contactID
            pushkinChat.imagePath = pushkinContact.imagePath
            pushkinChat.lastMessage = pushkinContact.name + ": Привет, милый друг!"
            pushkinChat.sender = pushkinContact.name
            db.chatDao!!.insert(pushkinChat)
            updateLastMessage()
            initialBotCreated = true
        }
    }

    fun updateLastMessage() {
        mArrayList?.clear()
        mArrayList = db.chatDao?.allChats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ChatViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_chat, null)
        return ChatViewHolder(itemLayoutView, clickListener)
    }

    override fun onBindViewHolder(viewHolder: ChatViewHolder, position: Int) {
        viewHolder.tvName.text = mArrayList?.get(position)?.sender ?: "null"
        val recipientImagePath = mArrayList?.get(position)?.imagePath
        if (mArrayList?.get(position)?.chatID != "push") {
            var imgFile: File? = File(recipientImagePath)
            if (imgFile!!.exists()) {
                var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                Log.i("TAGChatsAdapter", "saveImage: " + imgFile.length())
                viewHolder.userPhoto.setImageBitmap(myBitmap)
                myBitmap = null
            }
            imgFile = null
        }
        viewHolder.tvLastChat.text = mArrayList?.get(position)?.lastMessage ?: "last message"
    }

    override fun getItemCount(): Int {
        return mArrayList?.size!!
    }

    class ChatViewHolder(itemLayoutView: View, private val listener: ClickListener?) : RecyclerView.ViewHolder(itemLayoutView), View.OnClickListener, OnLongClickListener {
        val tvName: TextView
        val tvLastChat: TextView
        val userPhoto: ImageView
        override fun onClick(v: View) {
            listener?.onItemClicked(adapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            return listener?.onItemLongClicked(adapterPosition) ?: false
        }

        interface ClickListener {
            fun onItemClicked(position: Int)
            fun onItemLongClicked(position: Int): Boolean
        }

        init {
            tvName = itemLayoutView.findViewById<View>(R.id.tv_user_name) as TextView
            tvLastChat = itemLayoutView.findViewById<View>(R.id.tv_last_chat) as TextView
            userPhoto = itemLayoutView.findViewById<View>(R.id.rl_photo) as ImageView
            itemLayoutView.setOnClickListener(this)
            itemLayoutView.setOnLongClickListener(this)
        }
    }

    companion object {
        private var initialBotCreated = false
    }

    init {
        mArrayList = db.chatDao?.allChats
        notifyDataSetChanged()
        createPushkin()
    }
}