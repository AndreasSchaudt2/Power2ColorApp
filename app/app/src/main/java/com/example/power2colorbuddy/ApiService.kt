package com.example.power2colorbuddy

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/config")
    fun getConfig(): Call<ConfigResponse>

    @POST("/config")
    fun updateConfig(@Body configRequest: ConfigRequest): Call<StatusResponse>

    @POST("/restart")
    fun restart(): Call<StatusResponse>
}