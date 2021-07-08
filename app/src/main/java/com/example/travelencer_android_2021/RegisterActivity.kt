package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*

// 회원가입 액티비티
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

        // 체크박스 눌러야 <가입하기> 버튼 활성화
        checkbox.setOnCheckedChangeListener { compoundButton, b ->
            if (checkbox.isChecked == true) btnRegister.isEnabled = true
            else btnRegister.isEnabled = false
        }

        // <가입하기> 버튼 클릭
        btnRegister.setOnClickListener {
            finish()
        }

    }

}