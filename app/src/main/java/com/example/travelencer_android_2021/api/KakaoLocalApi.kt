package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.model.ModelKakaoLocalApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class KakaoLocalApi {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK ee0e5d4942b211e7b487dfba4d4910da"
    }
}


interface KakaoApiInterface {
    @GET("/v2/local/search/address.json")
    fun getKakaoAddress(
        @Header("Authorization") key: String,
        @Query("query") address: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<ModelKakaoLocalApi>
}