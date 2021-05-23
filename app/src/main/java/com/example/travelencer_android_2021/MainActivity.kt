package com.example.travelencer_android_2021

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

// 메인 액티비티
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // <로그인 & 회원가입> 버튼 클릭
        btnLoingAndRegister.setOnClickListener {
            // LoginActivity 넘어가기
            var intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // 설정 이미지 클릭
        imgSetting.setOnClickListener {

        }

    }

}