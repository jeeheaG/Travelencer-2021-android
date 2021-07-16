package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPostWritePlaceBinding

class PostWritePlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWritePlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWritePlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}