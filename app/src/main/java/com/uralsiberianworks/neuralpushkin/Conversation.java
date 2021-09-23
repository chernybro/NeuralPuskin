package com.uralsiberianworks.neuralpushkin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.api.PushkinApi;
import com.uralsiberianworks.neuralpushkin.db.Chat;
import com.uralsiberianworks.neuralpushkin.db.ChatDao;
import com.uralsiberianworks.neuralpushkin.db.Contact;
import com.uralsiberianworks.neuralpushkin.db.ContactDao;
import com.uralsiberianworks.neuralpushkin.db.Message;
import com.uralsiberianworks.neuralpushkin.db.MessageDao;
import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.recyclerConversation.ChatData;
import com.uralsiberianworks.neuralpushkin.recyclerConversation.ConversationRecyclerView;
import com.uralsiberianworks.neuralpushkin.recyclerviewChats.PushkinResponse;

import java.util.ArrayList;
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
    private ConversationRecyclerView mAdapter;
    private EditText mText;
    private NeuralDatabase db;
    private MessageDao messageDao;
    private ContactDao contactDao;
    private ChatDao chatDao;
    Bundle arguments;

    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "Conversation";
    private PushkinApi pushkinApi;
    private float temp = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_conversation);
        db = ((NeuralApp)getApplication()).getDb();
        messageDao = db.getMessageDao();
        contactDao = db.getContactDao();
        chatDao = db.getChatDao();

        Bundle arguments = getIntent().getExtras();
        String id = arguments.get("chat_id").toString();

        Contact contact = contactDao.getContact(id);
        int recipientImage = contact.getImage();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationRecyclerView(setChatData(id), recipientImage);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mRecyclerView.getAdapter().getItemCount()>=1)
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }, 1000);

        mText = (EditText) findViewById(R.id.et_message);
        mText.setOnClickListener(view -> mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mRecyclerView.getAdapter().getItemCount()>=1)
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }, 500));
        Button send = (Button) findViewById(R.id.bt_send);
        send.setOnClickListener(view -> {
            if (!mText.getText().equals("")) {
                List<Message> data = new ArrayList<>();
                Message message = new Message();
                message.setType("2");
                message.setChatID(id);
                message.setMessageID(String.valueOf(UUID.randomUUID()));
                String enteredText = mText.getText().toString();
                if (!TextUtils.isEmpty(enteredText)) {
                    message.setText(enteredText);

                    data.add(message);
                    mAdapter.addItem(data);
                    messageDao.insert(message);
                    Chat chat = chatDao.getChatFromID(id);
                    chat.setLastMessage(message.getText());
                    chatDao.update(chat);
                    data.clear();
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);

                    configureNetwork();

                    Call<PushkinResponse> call = pushkinApi.getPushkinExcerption(enteredText, temp, 120);
                    call.enqueue(new Callback<PushkinResponse>() {
                        @Override
                        public void onResponse(Call<PushkinResponse> call, retrofit2.Response<PushkinResponse> response) {
                            Message message1 = new Message();
                            message1.setType("1");
                            String responseText = response.body().getText();
                            message1.setText(responseText);
                            message1.setChatID(id);
                            message1.setMessageID(String.valueOf(UUID.randomUUID()));
                            data.add(message1);
                            Chat chat = chatDao.getChatFromID(id);
                            chat.setLastMessage(message1.getText());
                            chatDao.update(chat);
                            mAdapter.addItem(data);
                            messageDao.insert(message1);
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                            Log.d(TAG, "onResponse: " + response.toString());

                        }

                        @Override
                        public void onFailure(Call<PushkinResponse> call, Throwable t) {
                            Log.d(TAG, "onResponse: " + t.getMessage());
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    mText.setText("");
                }
            }
        });
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

    public List<Message> setChatData(String id){
        List<Message> databaseMessages = messageDao.getAllMessages(id);
        return databaseMessages;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
