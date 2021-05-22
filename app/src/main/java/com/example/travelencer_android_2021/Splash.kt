package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// 스플래시 화면
class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 메인 액티비티로 이동
        val intent = Intent(this@Splash, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}