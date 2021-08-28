package com.example.travelencer_android_2021.data

// 설정

// 보낼 데이터
data class SettingData (val UID : Int)
// 받을 데이터
data class SettingResponse (val code : Int, val message : String, val proPic : String?, val name : String, val info : String?)