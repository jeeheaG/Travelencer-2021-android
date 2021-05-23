package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pp.*

// 개인정보 보호정책 액티비티
class PPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pp)

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

    }
}