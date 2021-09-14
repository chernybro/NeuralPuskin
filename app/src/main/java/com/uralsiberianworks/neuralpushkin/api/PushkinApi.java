package com.uralsiberianworks.neuralpushkin.api;

import com.uralsiberianworks.neuralpushkin.PushkinResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PushkinApi {

    @GET("http://46.17.97.44:5000/push/")
    Call<PushkinResponse> getPushkinExcerption(@Query("text") String text, @Query("temp") double temp, @Query("length") int length);

    @GET("http://46.17.97.44:5001/stih/")
    Call<PushkinResponse> getPushkinPoem(@Query("name") String name, @Query("temp") double temp, @Query("length") int length);
}