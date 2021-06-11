package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPNCBinding
//뷰바인딩 사용
private lateinit var binding: ActivityPNCBinding

class PNCActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPNCBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}