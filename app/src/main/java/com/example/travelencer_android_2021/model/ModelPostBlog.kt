package com.example.travelencer_android_2021.model

class ModelPostBlog(
        var postId: String? = null,
        var uid: String? = null,
        var title: String? = null,
        var date: String? = null,
        var icon: Int? = null,
        var placeName: String? = null,
        var location: String? = null,
        var writing: String? = null,
        var photoList: ArrayList<ModelPostBlogPhoto>? = null){}