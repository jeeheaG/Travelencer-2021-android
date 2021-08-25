package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

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
}