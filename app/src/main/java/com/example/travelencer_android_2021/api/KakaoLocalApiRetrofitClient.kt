package com.example.travelencer_android_2021.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoLocalApiRetrofitClient {
    private val kakaoRetrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(KakaoLocalApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val kakaoApiService: KakaoApiInterface by lazy {
        kakaoRetrofit
            .build()
            .create(KakaoApiInterface::class.java)
    }
}