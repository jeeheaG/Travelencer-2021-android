package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPostWriteCourseBinding

class PostWriteCourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWriteCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteCourseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}