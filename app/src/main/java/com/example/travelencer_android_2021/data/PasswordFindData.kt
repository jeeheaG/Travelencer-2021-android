package com.example.travelencer_android_2021.data

// 비밀번호 찾기

// 보낼 데이터
data class PasswordFindData (val email : String, val password : String)
// 받을 데이터
data class PasswordFindResponse (val code : Int)