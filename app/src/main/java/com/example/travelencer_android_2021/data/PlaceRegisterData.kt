package com.example.travelencer_android_2021.data

import android.net.Uri

data class PlaceRegisterData(
        val plcName: String,
        val plcExplain: String?,
        val plcAddress: String,
        val plcCategory: Int,
        val plcPicture: String?
        //val plcPicture: ArrayList<Uri>? //일단 다 서버 변수명이랑 맞춰둠. 사진은 배열로 바꾸기
)

data class PlaceRegisterResponse(
        val code: Int, //resultCode
        val message: String
)