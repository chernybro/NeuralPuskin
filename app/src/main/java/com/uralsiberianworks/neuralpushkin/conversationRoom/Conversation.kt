package com.uralsiberianworks.neuralpushkin.conversationRoom

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uralsiberianworks.neuralpushkin.NeuralApp
import com.uralsiberianworks.neuralpushkin.R
import com.uralsiberianworks.neuralpushkin.api.PushkinApi
import com.uralsiberianworks.neuralpushkin.api.PushkinResponse
import com.uralsiberianworks.neuralpushkin.database.Message
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*

class Conversation : AppCompatActivity() {
    private var mRecyclerView: RecyclerView = findViewById(R.id.recyclerView)
    private var mAdapter: ConversationAdapter? = null
    private var mText: EditText? = null
    private var db: NeuralDatabase = (application as NeuralApp).db
    private var pushkinApi: PushkinApi? = null
    private var send: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_conversation)
        val arguments = intent.extras
        val position = arguments!!.getInt("position")
        val id = db.chatDao!!.allChats!![position]!!.chatID
        //id = arguments.get("chat_id").toString();
        val contact = db.contactDao!!.getContact(id)
        val recipientImagePath = contact!!.imagePath
        val nameTab = findViewById<TextView>(R.id.name_tab)
        nameTab.text = contact.name
        val imgTab = findViewById<ImageView>(R.id.img_tab)
        val imgFile = File(recipientImagePath)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imgTab.setImageBitmap(myBitmap)
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager.stackFromEnd = true
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(layoutManager)
        mAdapter = ConversationAdapter(setChatData(id), recipientImagePath!!)
        mRecyclerView.setAdapter(mAdapter)
        mRecyclerView.postDelayed(Runnable { if (mRecyclerView.getAdapter()!!.itemCount >= 1) mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter()!!.itemCount - 1) }, 1)
        mText = findViewById<View>(R.id.et_message) as EditText
        mText!!.setOnClickListener { view: View? -> mRecyclerView.postDelayed(Runnable { if (mRecyclerView.getAdapter()!!.itemCount >= 1) mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter()!!.itemCount - 1) }, 1) }
        send = findViewById<View>(R.id.bt_send) as Button
        send!!.setOnClickListener { view: View? ->
            if (mText!!.text.toString() != "") {
                val data: MutableList<Message> = ArrayList()
                val enteredText = setUserMessage(data, id)
                configureNetwork()
                val message = setTypingMessage(data, id)
                send!!.isEnabled = false
                setBotMessage(data, id, enteredText, message)
            }
        }
    }

    private fun setBotMessage(data: MutableList<Message>, id: String?, enteredText: String, typingMessage: Message) {
        val call = pushkinApi!!.getPushkinExcerption(enteredText, 1.0, 120)
        call!!.enqueue(object : Callback<PushkinResponse?> {
            override fun onResponse(call: Call<PushkinResponse?>, response: Response<PushkinResponse?>) {
                if (response.message() == "OK") {
                    val message1 = Message()
                    message1.type = "1"
                    val responseText = response.body()!!.text
                    message1.text = responseText
                    message1.chatID = id
                    message1.messageID = UUID.randomUUID().toString()
                    message1.initialLength = enteredText.length
                    data.add(message1)
                    val chat = db!!.chatDao!!.getChatFromID(id)
                    chat!!.lastMessage = db!!.contactDao!!.getContact(id)!!.name.toString() + ": " + message1.text
                    db!!.chatDao!!.update(chat)
                    mAdapter!!.addItem(data)
                    db!!.messageDao!!.insert(message1)
                    mAdapter!!.delItem(typingMessage)
                    db!!.messageDao!!.delete(typingMessage)
                    mRecyclerView!!.smoothScrollToPosition(mRecyclerView!!.adapter!!.itemCount - 1)
                }
                Log.d(TAG, "onResponse: $response")
                send!!.isEnabled = true
            }

            override fun onFailure(call: Call<PushkinResponse?>, t: Throwable) {
                /*Message message1 = new Message();
                            message1.setType("1");
                            String responseText = "Привет! Это мой дефолтный ответ";
                            message1.setText(responseText);
                            message1.setChatID(id);
                            message1.setMessageID(String.valueOf(UUID.randomUUID()));
                            message1.setInitialLength(enteredText.length());
                            data.add(message1);
                            Chat chat = chatDao.getChatFromID(id);
                            chat.setLastMessage(message1.getText());
                            chatDao.update(chat);
                            mAdapter.addItem(data);
                            messageDao.insert(message1);
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);*/
                mAdapter!!.delItem(typingMessage)
                db!!.messageDao!!.delete(typingMessage)
                Log.d(TAG, "onResponse: " + t.message)
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                send!!.isEnabled = true
            }

        })
        mText!!.setText("")
    }

    private fun setUserMessage(data: MutableList<Message>, id: String?): String {
        val message = Message()
        message.type = "2"
        message.chatID = id
        message.messageID = UUID.randomUUID().toString()
        val enteredText = mText!!.text.toString()
        if (!TextUtils.isEmpty(enteredText)) {
            message.text = enteredText
            message.initialLength = enteredText.length
            data.add(message)
            mAdapter!!.addItem(data)
            db!!.messageDao!!.insert(message)
            val chat = db!!.chatDao!!.getChatFromID(id)
            chat!!.lastMessage = "Вы: " + message.text
            db!!.chatDao!!.update(chat)
            data.clear()
            mRecyclerView!!.smoothScrollToPosition(mRecyclerView!!.adapter!!.itemCount - 1)
            return enteredText
        }
        return ""
    }

    private fun setTypingMessage(data: MutableList<Message>, id: String?): Message {
        val message = Message()
        message.type = "3"
        message.chatID = id
        val typingMessageID = UUID.randomUUID().toString()
        message.messageID = typingMessageID
        message.text = db!!.contactDao!!.getContact(id)!!.name.toString() + " печатает..."
        message.initialLength = 0
        data.add(message)
        mAdapter!!.addItem(data)
        db!!.messageDao!!.insert(message)
        data.clear()
        mRecyclerView!!.smoothScrollToPosition(mRecyclerView!!.adapter!!.itemCount - 1)
        return message
    }

    override fun onStart() {
        super.onStart()
        //mAdapter.updateLastMessage(messageDao.getAllMessages(id));
    }

    private fun configureNetwork() {
        val client = OkHttpClient.Builder()
                .addInterceptor( Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request().newBuilder()
                            .addHeader("Accept", "Application/JSON")
                            .build()
                    chain.proceed(request)
                }).build()
        val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        pushkinApi = retrofit.create(PushkinApi::class.java)
    }

    //public List<Message> setChatData(String id){return messageDao.getAllMessages(id).;}
    override fun onBackPressed() {
        finish()
    }

    fun setChatData(id: String?): MutableList<Message?>? {
        return db!!.messageDao!!.getAllMessages(id)
    }

    companion object {
        const val URL = "http://46.17.97.44:5000"
        private const val TAG = "Conversation"
    }
}