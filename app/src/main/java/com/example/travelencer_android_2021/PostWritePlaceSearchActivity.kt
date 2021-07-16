package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPostWritePlaceSearchBinding

class PostWritePlaceSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWritePlaceSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWritePlaceSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}