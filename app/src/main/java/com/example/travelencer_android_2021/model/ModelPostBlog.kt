package com.example.travelencer_android_2021.model

class ModelPostBlog(
        val title: String,
        val date: String,
        val icon: Int,
        val placeName: String,
        val location: String,
        val writing: String,
        val photoList: ArrayList<ModelPostBlogPhoto>)