package com.example.travelencer_android_2021.data

data class RegisterData (
        val uid : String,
        val email : String,             // 이메일
        // val password : String,          // 비민번호
        var proPic : String? = null,    // 프로필 사진
        var name : String? = null,      // 이름
        var info : String? = null,      // 계정 소개글
        )