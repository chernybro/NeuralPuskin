package com.uralsiberianworks.neuralpushkin.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PushkinApi {

    @GET("http://**************/push/")
    Call<PushkinResponse> getPushkinExcerption(@Query("text") String text, @Query("temp") double temp, @Query("length") int length);

    @GET("http://**************/stih/")
    Call<PushkinResponse> getPushkinPoem(@Query("name") String name, @Query("temp") double temp, @Query("length") int length);
}
