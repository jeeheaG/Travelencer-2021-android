package com.example.travelencer_android_2021.data

// 회원가입

// 보낼 데이터
data class JoinData (val name : String, val email : String, val password : String)
// 받을 데이터
data class JoinResponse (val code : Int, val message : String)