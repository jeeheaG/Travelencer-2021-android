package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_password_find.*

// 비밀번호 찾기 액티비티
class PasswordFindActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_find)

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

    }

}