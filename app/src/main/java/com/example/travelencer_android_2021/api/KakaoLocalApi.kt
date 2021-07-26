package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.model.ModelKakaoLocalApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class KakaoLocalApi {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK a265349725cffdb6deae08f828846b0c"
    }
}


interface KakaoApiInterface {
    @GET("/v2/local/search/address.json")
    fun getKakaoAddress(
        @Header("Authorization") key: String,
        @Query("query") address: String,
        @Query("query") size: Int
    ): Call<ModelKakaoLocalApi>
}