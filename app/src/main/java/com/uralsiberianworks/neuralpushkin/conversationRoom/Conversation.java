package com.uralsiberianworks.neuralpushkin.conversationRoom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.NeuralApp;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.api.BotRequestBody;
import com.uralsiberianworks.neuralpushkin.api.BotResponse;
import com.uralsiberianworks.neuralpushkin.api.PushkinApi;
import com.uralsiberianworks.neuralpushkin.database.Chat;
import com.uralsiberianworks.neuralpushkin.database.ChatDao;
import com.uralsiberianworks.neuralpushkin.database.Contact;
import com.uralsiberianworks.neuralpushkin.database.ContactDao;
import com.uralsiberianworks.neuralpushkin.database.Message;
import com.uralsiberianworks.neuralpushkin.database.MessageDao;
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.api.PushkinResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Conversation extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ConversationAdapter mAdapter;
    private EditText mText;
    private NeuralDatabase db;
    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "Conversation";
    private PushkinApi pushkinApi;
    private Button send;
    private String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_conversation);
        configureNetwork();
        db = ((NeuralApp) getApplication()).getDb();

        Bundle arguments = getIntent().getExtras();
        int position = arguments.getInt("position");
        chatID = db.getChatDao().getAllChats().get(position).getChatID();
        //id = arguments.get("chat_id").toString();

        Contact contact = db.getContactDao().getContact(chatID);
        String recipientImagePath = contact.getImagePath();

        TextView nameTab = findViewById(R.id.name_tab);
        nameTab.setText(contact.getName());
        ImageView imgTab = findViewById(R.id.img_tab);
        /*File imgFile = new File(recipientImagePath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imgTab.setImageBitmap(myBitmap);
        }*/

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ConversationAdapter(setChatData(chatID), recipientImagePath);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.postDelayed(() -> {
            if(mRecyclerView.getAdapter().getItemCount()>=1)
            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
        }, 1);

        mText = (EditText) findViewById(R.id.et_message);
        mText.setOnClickListener(view -> mRecyclerView.postDelayed(() -> {
            if(mRecyclerView.getAdapter().getItemCount()>=1)
            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
        }, 1));
        send = (Button) findViewById(R.id.bt_send);

        send.setOnClickListener(view -> {
            if (!mText.getText().toString().equals("")) {
                List<Message> data = new ArrayList<>();

                String enteredText = setUserMessage(data, chatID);

                Message message = setTypingMessage(data, chatID);
                send.setEnabled(false);
                setBotMessage(data, chatID, enteredText, message);

            }
        });
    }

    private void setBotMessage(List<Message> data, String id, String enteredText, Message typingMessage) {
        String[] s = db.getMessageDao().getAllHistory(chatID).toArray(new String[0]);
        Log.d(TAG, "setBotMessage: s = " + Arrays.toString(s));
        Call<BotResponse> call = pushkinApi.getBotResponse(new BotRequestBody(s, db.getContactDao().getContact(chatID).getContactFacts(), 10));
        Log.d(TAG, "setBotMessage: " + call.request());
        Log.d(TAG, "setBotMessage: " + call.request().headers().get("candnum"));


        call.enqueue(new Callback<BotResponse>() {
            @Override
            public void onResponse(Call<BotResponse> call, retrofit2.Response<BotResponse> response) {

                if (response.message().equals("OK"))
                {
                    Message message1 = new Message();
                    message1.setType("1");
                    String responseText = response.body().getAnswer();
                    message1.setText(responseText);
                    message1.setChatID(id);
                    message1.setMessageID(String.valueOf(UUID.randomUUID()));
                    message1.setInitialLength(enteredText.length());
                    data.add(message1);
                    Chat chat = db.getChatDao().getChatFromID(id);
                    chat.setLastMessage(db.getContactDao().getContact(id).getName() + ": " + message1.getText());
                    db.getChatDao().update(chat);
                    mAdapter.addItem(data);
                    db.getMessageDao().insert(message1);
                    mAdapter.delItem(typingMessage);
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                }
                Log.d(TAG, "REQUEST: " + call.request());
                Log.d(TAG, "onResponse: " + response.toString());
                send.setEnabled(true);
            }

            @Override
            public void onFailure(Call<BotResponse> call, Throwable t) {
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
                mAdapter.delItem(typingMessage);
                Log.d(TAG, "onFailure: " + call.request());
                Log.d(TAG, "onResponse: " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                send.setEnabled(true);
            }
        });

        mText.setText("");

    }

    private String setUserMessage(List<Message> data, String id) {
        Message message = new Message();
        message.setType("2");
        message.setChatID(id);

        message.setMessageID(String.valueOf(UUID.randomUUID()));
        String enteredText = mText.getText().toString();
        if (!TextUtils.isEmpty(enteredText)) {
            message.setText("Person:" + enteredText);
            message.setInitialLength(enteredText.length() - 7);
            data.add(message);
            mAdapter.addItem(data);
            db.getMessageDao().insert(message);
            Chat chat = db.getChatDao().getChatFromID(id);
            chat.setLastMessage("Вы: " + message.getText());
            db.getChatDao().update(chat);
            data.clear();
            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            return enteredText;
        }
        return "";
    }

    private Message setTypingMessage(List<Message> data, String id) {
        Message message = new Message();
        message.setType("3");
        message.setChatID(id);
        String typingMessageID = String.valueOf(UUID.randomUUID());
        message.setMessageID(typingMessageID);

        message.setText(db.getContactDao().getContact(id).getName() + " typing...");
        message.setInitialLength(0);
        data.add(message);
        mAdapter.addItem(data);
        data.clear();
        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
        return message;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAdapter.updateLastMessage(messageDao.getAllMessages(id));
    }

    private void configureNetwork() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", "Application/JSON")
                            .build();
                    return chain.proceed(request);
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pushkinApi = retrofit.create(PushkinApi.class);
    }

    //public List<Message> setChatData(String id){return messageDao.getAllMessages(id).;}

    @Override
    public void onBackPressed() {
        finish();
    }

    public List<Message> setChatData(String id){
        return db.getMessageDao().getAllMessages(id);
    }
}
