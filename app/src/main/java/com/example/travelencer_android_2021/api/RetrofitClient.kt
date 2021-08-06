package com.example.travelencer_android_2021.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 서버 연결
private val retrofit = Retrofit.Builder()
        .baseUrl("http://152.70.95.197:3000")
        .addConverterFactory(GsonConverterFactory.create())

object RetrofitClient {
    val serviceApi : ServiceApiInterface by lazy {
        retrofit.build().create(ServiceApiInterface::class.java)
    }
}