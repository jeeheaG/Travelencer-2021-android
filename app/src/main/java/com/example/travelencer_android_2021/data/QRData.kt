package com.example.travelencer_android_2021.data

// QR 코드

// 보낼 데이터
data class QRData (val uid : Int)
// 받을 데이터
data class QRResponse (val email : String, val code : Int, val message : String)