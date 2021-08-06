package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.data.JoinData
import com.example.travelencer_android_2021.data.JoinResponse
import com.example.travelencer_android_2021.data.LoginData
import com.example.travelencer_android_2021.data.LoginResponse
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
}