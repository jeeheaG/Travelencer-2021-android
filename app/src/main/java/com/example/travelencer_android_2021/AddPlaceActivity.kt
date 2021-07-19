package com.example.travelencer_android_2021

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceBinding

class AddPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnPlaceRegisterNext.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}