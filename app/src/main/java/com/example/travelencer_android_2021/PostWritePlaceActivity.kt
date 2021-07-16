package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPostWritePlaceBinding

class PostWritePlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWritePlaceBinding
    //lateinit var _PostWritePlaceActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //_PostWritePlaceActivity = this
        binding = ActivityPostWritePlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnPlaceRegister.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivity(intent)
        }

        binding.btnPlaceSearch.setOnClickListener {
            val intent = Intent(this, PostWritePlaceSearchActivity::class.java)
            startActivity(intent)

        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}