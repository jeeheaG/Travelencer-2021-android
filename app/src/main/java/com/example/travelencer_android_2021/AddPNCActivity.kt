package com.example.travelencer_android_2021

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityAddPNCBinding

class AddPNCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPNCBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPNCBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnPlaceRegisterDone.setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}