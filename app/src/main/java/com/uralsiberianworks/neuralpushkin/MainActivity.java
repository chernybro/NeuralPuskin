package com.uralsiberianworks.neuralpushkin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://46.17.97.44:5000/";
    private static final String TAG = "MainActivity";
    private PushkinApi pushkinApi;
    private String enteredText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton = findViewById(R.id.btn);
        EditText mInputTextView = findViewById(R.id.etText);
        TextView mPushkinResponseTextView = findViewById(R.id.responseText);

        mInputTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enteredText = editable.toString();
                mButton.setEnabled(!TextUtils.isEmpty(enteredText));
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Accept", "Application/JSON")
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pushkinApi = retrofit.create(PushkinApi.class);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredText =  mInputTextView.getText().toString();
                Call<PushkinResponse> call = pushkinApi.getPushkinResponse(enteredText, 1);
                call.enqueue(new Callback<PushkinResponse>() {
                    @Override
                    public void onResponse(Call<PushkinResponse> call, retrofit2.Response<PushkinResponse> response) {
                        mPushkinResponseTextView.setText(response.body().getText());
                    }

                    @Override
                    public void onFailure(Call<PushkinResponse> call, Throwable t) {
                        Log.d(TAG, "onResponse: " + t.getMessage());
                    }
                });
            }
        });

    }
}