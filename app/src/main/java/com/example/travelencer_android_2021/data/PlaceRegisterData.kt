package com.example.travelencer_android_2021.data

import android.net.Uri

data class PlaceRegisterData(
        val plcName: String,
        val plcProduce: String, //Explain
        val plcAddress: String,
        val plcCategory: Int,
        val plcPicture: ArrayList<Uri>,
        val plcGood: String,
        val plcBad: String,
        val locX: Float,
        val locY: Float
        //val plcPicture: ArrayList<Uri>? //일단 다 서버 변수명이랑 맞춰둠. 사진은 배열로 바꾸기
)

data class PlaceRegisterResponse(
        val code: Int, //resultCode
        val message: String
)