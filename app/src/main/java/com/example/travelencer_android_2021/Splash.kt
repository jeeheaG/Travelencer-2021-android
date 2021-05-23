package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// 스플래시
class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MainActivity로 넘어가기
        var intent = Intent(this@Splash, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
