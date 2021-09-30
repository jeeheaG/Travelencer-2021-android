package com.example.travelencer_android_2021.model

data class ModelPostT (
        var uid: String? = null,
        var postId: String? = null,
        var updateDate: String? = null, // 최초 게시한 날짜(수정x)
        var title: String? = null,
        var startDate: String? = null,
        var EndDate: String? = null,
        var content: String? = null
){}