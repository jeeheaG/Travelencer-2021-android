package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tos.*

// 서비스 이용 약관 액티비티
class TOSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tos)

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

        }
}