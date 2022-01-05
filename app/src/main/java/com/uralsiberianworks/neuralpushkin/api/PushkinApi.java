package com.uralsiberianworks.neuralpushkin.api;

import com.uralsiberianworks.neuralpushkin.database.Message;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PushkinApi {

    @GET("http://46.17.97.44:5000/push/")
    Call<PushkinResponse> getPushkinExcerption(@Query("text") String text, @Query("temp") double temp, @Query("length") int length);

    @POST("http://46.17.97.44:5000/waifu/")
    Call<BotResponse> getBotResponse(@Body BotRequestBody botRequestBody);
}
