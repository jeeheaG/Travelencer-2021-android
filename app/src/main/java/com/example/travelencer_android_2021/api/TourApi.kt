package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.model.ModelKakaoLocalApi
import com.example.travelencer_android_2021.model.ModelTourApiKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class TourApi {
    companion object {
        const val BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/"
        //크롬에서는 이게 작동
        const val API_KEY_E = "mCY8ro3DDTG7aLCe50M0L643WiA7NY5Sn56fQUUnuUNtKRlyzTvtjj2Jg3LMWiGJJ%2B0rVimLj%2Bq8bEaCAAxz1A%3D%3D"
        //안드로이드에서는 이게 작동
        const val API_KEY_D = "mCY8ro3DDTG7aLCe50M0L643WiA7NY5Sn56fQUUnuUNtKRlyzTvtjj2Jg3LMWiGJJ+0rVimLj+q8bEaCAAxz1A=="

        const val OS = "AND" //안드로이드
        const val APP_NAME = "Travelencer" //서비스명
        const val DATA_TYPE = "json"
    }
}


interface TourApiInterface {
    @GET("searchKeyword")
    fun getTourData(
            @Query("ServiceKey") serviceKey: String = TourApi.API_KEY_D,
            @Query("keyword") keyword: String,
            @Query("MobileOS") os: String = TourApi.OS,
            @Query("MobileApp") appName: String = TourApi.APP_NAME,
            @Query("_type") type: String = TourApi.DATA_TYPE,
            @Query("areaCode") area: String,
            @Query("sigunguCode") sigungu: String
    ): Call<ModelTourApiKeyword>
}