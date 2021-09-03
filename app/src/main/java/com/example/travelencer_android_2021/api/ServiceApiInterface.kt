package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceApiInterface {
    // 로그인
    @POST("/user/login")
    fun userLogin(@Body data: LoginData): Call<LoginResponse>

    // 회원가입
    @POST("/user/join")
    fun userJoin(@Body data: JoinData): Call<JoinResponse>

    // 비밀번호 찾기
    @POST("/user/pwchange")
    fun userPwchange(@Body data: PasswordFindData): Call<PasswordFindResponse>

    //장소 등록
    @POST("/place/register")
    fun placeRegister(@Body data: PlaceRegisterData): Call<PlaceRegisterResponse>

    // 설정
    @POST("/setting/set")
    fun setSetting(@Body data: SettingData): Call<SettingResponse>

    // 설정 변경
    @Multipart  // 프로필 이미지
    @POST("/setting/change")
//    fun changeSetting(@Part photo : MultipartBody.Part?, @PartMap map : HashMap<String, RequestBody>): Call<SettingChangeResponse>
    fun changeSetting(@Part proPic : MultipartBody.Part?,
                      @Part UID : MultipartBody.Part,
                      @Part name : MultipartBody.Part,
                      @Part info : MultipartBody.Part,
                      ): Call<SettingChangeResponse>
//    @POST("/setting/change")
//    // 기타 정보 변경
//    fun changeSetting(@Body data: SettingChangeData): Call<SettingChangeResponse>
}