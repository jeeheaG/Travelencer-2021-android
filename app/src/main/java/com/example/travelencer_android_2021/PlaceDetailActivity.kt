package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.travelencer_android_2021.databinding.FragmentPostBlogBinding
//뷰바인딩 사용
private lateinit var binding: FragmentPostBlogBinding

class PlaceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPostBlogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}