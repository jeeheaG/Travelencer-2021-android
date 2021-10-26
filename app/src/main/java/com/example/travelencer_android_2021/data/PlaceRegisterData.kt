package com.example.travelencer_android_2021.data

data class PlaceRegisterData(
        val plcName: String,
        val plcProduce: String, //Explain
        val plcAddress: String,
        val plcCategory: Int,
        val locX: Float,
        val locY: Float,
        val plcId: String,
        val area1: String,
        val area2: String,
        val area1Code: Int,
        val area2Code: Int
)
