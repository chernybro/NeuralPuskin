package com.uralsiberianworks.neuralpushkin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PushkinApi {

    @GET("push/")
    Call<PushkinResponse> getPushkinResponse(@Query("text") String text,@Query("temp") int temp);
}
