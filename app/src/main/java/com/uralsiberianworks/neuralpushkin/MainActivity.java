package com.uralsiberianworks.neuralpushkin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.uralsiberianworks.neuralpushkin.api.PushkinApi;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://46.17.97.44:5000";
    private static final String TAG = "MainActivity";
    private PushkinApi pushkinApi;
    private String enteredText;
    private float temp = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        FragmentTransaction ft;
        FragmentHolder fragmentHome = new FragmentHolder();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout, fragmentHome).commit();





        /*Button mButton = findViewById(R.id.btn);
        EditText mInputTextView = findViewById(R.id.etText);
        TextView mPushkinResponseTextView = findViewById(R.id.responseText);
        mPushkinResponseTextView.setMovementMethod(new ScrollingMovementMethod());
        Slider mTempSlider = findViewById(R.id.temp_slider);
        mTempSlider.addOnChangeListener((slider, value, fromUser) -> temp = value);

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

        configureNetwork();

        mButton.setOnClickListener(view -> {
            enteredText =  mInputTextView.getText().toString();
            Call<PushkinResponse> call = pushkinApi.getPushkinExcerption(enteredText, temp, 200);
            call.enqueue(new Callback<PushkinResponse>() {
                @Override
                public void onResponse(Call<PushkinResponse> call, retrofit2.Response<PushkinResponse> response) {
                    mPushkinResponseTextView.setText(response.body().getText());
                    Log.d(TAG, "onResponse: " + response.toString());
                }

                @Override
                public void onFailure(Call<PushkinResponse> call, Throwable t) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                }
            });
        });

    }

    private void configureNetwork() {
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

        pushkinApi = retrofit.create(PushkinApi.class); */
    }
}