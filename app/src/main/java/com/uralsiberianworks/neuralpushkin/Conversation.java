package com.uralsiberianworks.neuralpushkin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.api.PushkinApi;
import com.uralsiberianworks.neuralpushkin.recyclerConversation.ChatData;
import com.uralsiberianworks.neuralpushkin.recyclerConversation.ConversationRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Conversation extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ConversationRecyclerView mAdapter;
    private EditText text;

    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "Conversation";
    private PushkinApi pushkinApi;
    private float temp = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_conversation);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationRecyclerView(setChatData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }, 1000);

        text = (EditText) findViewById(R.id.et_message);
        text.setOnClickListener(view -> mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }, 500));
        Button send = (Button) findViewById(R.id.bt_send);
        send.setOnClickListener(view -> {
            if (!text.getText().equals("")){
                List<ChatData> data = new ArrayList<ChatData>();
                ChatData userData = new ChatData();
                userData.setType("2");
                String enteredText = text.getText().toString();
                userData.setText(enteredText);
                data.add(userData);
                mAdapter.addItem(data);
                data.clear();
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() -1);

                configureNetwork();

                Call<PushkinResponse> call = pushkinApi.getPushkinExcerption(enteredText, temp, 200);
                call.enqueue(new Callback<PushkinResponse>() {
                    @Override
                    public void onResponse(Call<PushkinResponse> call, retrofit2.Response<PushkinResponse> response) {
                        ChatData recipientData = new ChatData();
                        recipientData.setType("1");
                        String responseText = response.body().getText();
                        recipientData.setText(responseText);
                        data.add(recipientData);
                        mAdapter.addItem(data);
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() -1);
                        Log.d(TAG, "onResponse: " + response.toString());
                    }

                    @Override
                    public void onFailure(Call<PushkinResponse> call, Throwable t) {
                        Log.d(TAG, "onResponse: " + t.getMessage());
                    }
                });

                text.setText("");
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

    public List<ChatData> setChatData(){
        List<ChatData> data = new ArrayList<>();

        String text[] = {"What's up, bitches?"};
        String type[] = {"1"};

        for (int i=0; i<text.length; i++){
            ChatData item = new ChatData();
            item.setType(type[i]);
            item.setText(text[i]);
            data.add(item);
        }

        return data;
    }

}
