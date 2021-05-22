package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 로그인 액티비티와 레이아웃 연결
        setContentView(R.layout.activity_login)

        // 로그인 버튼 누르면
        btnLogin.setOnClickListener {
            // MainActivity로 넘어가기
            var intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}