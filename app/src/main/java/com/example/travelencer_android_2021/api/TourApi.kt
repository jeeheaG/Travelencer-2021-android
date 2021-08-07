package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.model.modelTourApiDetailCommon.ModelTourApiDetailCommon
import com.example.travelencer_android_2021.model.modelTourApiKeyword.ModelTourApiKeyword
import retrofit2.Call
import retrofit2.http.GET
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

    // 키워드 검색
    @GET("searchKeyword")
    fun getTourKeywordData(
            @Query("keyword") keyword: String,
            @Query("areaCode") area: String,
            @Query("sigunguCode") sigungu: String,

            @Query("ServiceKey") serviceKey: String = TourApi.API_KEY_D,
            @Query("MobileOS") os: String = TourApi.OS,
            @Query("MobileApp") appName: String = TourApi.APP_NAME,
            @Query("_type") type: String = TourApi.DATA_TYPE
    ): Call<ModelTourApiKeyword>

    // 공통 정보 조회(장소 상세)
    @GET("detailCommon")
    fun getTourDetailCommonData(
            //응답 중 homepage 값에 큰따옴표가 문자열 안에 그냥 섞여있어서 문제될 것 같은데....

            @Query("contentId") contentId: String, //dummtdata : "126508"

            @Query("defaultYN") defaultYN: String = "Y", //공통 정보 조회여부
            @Query("firstImageYN") firstImageYN: String = "Y", //대표 이미지 조회여부
            @Query("overviewYN") overviewYN: String = "Y", //개요(줄글) 정보 조회여부
            @Query("addrinfoYN") addrinfoYN: String = "Y", //주소 정보 조회여부
            @Query("mapinfoYN") mapinfoYN: String = "Y", //좌표 정보 조회여부
            @Query("areacodeYN") areacodeYN: String = "Y", //지역코드 정보 조회여부 -> 필요없는데 혹시 몰라서 Y
            @Query("ServiceKey") serviceKey: String = TourApi.API_KEY_D,
            @Query("MobileOS") os: String = TourApi.OS,
            @Query("MobileApp") appName: String = TourApi.APP_NAME,
            @Query("_type") type: String = TourApi.DATA_TYPE
    ): Call<ModelTourApiDetailCommon>
}



/*
// 공통 정보 조회(장소 상세)
interface TourApiDetailCommonInterface {
    @GET("detailCommon")
    fun getTourDetailCommonData( //응답 중 homepage 값에 큰따옴표가 문자열 안에 그냥 섞여있어서 문제될 것 같은데....
            @Query("contentId") contentId: String, //dummtdata : "126508"

            @Query("defaultYN") defaultYN: String = "Y", //공통 정보 조회여부
            @Query("firstImageYN") firstImageYN: String = "Y", //대표 이미지 조회여부
            @Query("overviewYN") overviewYN: String = "Y", //개요(줄글) 정보 조회여부
            @Query("addrinfoYN") addrinfoYN: String = "Y", //주소 정보 조회여부
            @Query("mapinfoYN") mapinfoYN: String = "Y", //좌표 정보 조회여부
            @Query("areacodeYN") areacodeYN: String = "Y", //지역코드 정보 조회여부 -> 필요없는데 혹시 몰라서 Y
            @Query("ServiceKey") serviceKey: String = TourApi.API_KEY_D,
            @Query("MobileOS") os: String = TourApi.OS,
            @Query("MobileApp") appName: String = TourApi.APP_NAME,
            @Query("_type") type: String = TourApi.DATA_TYPE
    ): Call<ModelTourApiDetailCommon>
}*/
