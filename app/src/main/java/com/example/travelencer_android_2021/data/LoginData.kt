package com.example.travelencer_android_2021.data

// 로그인

// 보낼 데이터
data class LoginData (val email : String, val password : String)
// 받을 데이터
data class LoginResponse (val code : Int, val message : String, val uid : Int)