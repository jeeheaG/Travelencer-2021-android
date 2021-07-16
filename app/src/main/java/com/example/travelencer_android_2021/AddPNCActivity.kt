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

        //val _PostWritePlaceActivity: PostWritePlaceActivity = PostWritePlaceActivity._PostWritePlaceActivity

        binding.btnPlaceRegisterDone.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}