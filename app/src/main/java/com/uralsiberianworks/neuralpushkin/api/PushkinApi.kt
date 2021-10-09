package com.uralsiberianworks.neuralpushkin.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PushkinApi {
    @GET("http://46.17.97.44:5000/push/")
    fun getPushkinExcerption(@Query("text") text: String?, @Query("temp") temp: Double, @Query("length") length: Int): Call<PushkinResponse?>?

    @GET("http://46.17.97.44:5001/stih/")
    fun getPushkinPoem(@Query("name") name: String?, @Query("temp") temp: Double, @Query("length") length: Int): Call<PushkinResponse?>?
}