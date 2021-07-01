package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

// 홈 액티비티
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // <로그인 & 회원가입> 버튼 클릭
        btnLoingAndRegister.setOnClickListener {
            // LoginActivity로 이동하기
            var intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // 설정 이미지 클릭
        imgSetting.setOnClickListener {
            setNavi(R.id.settingFragment)
        }

        // <여행지 검색> 클릭
        btnSearchPlace.setOnClickListener {
            setNavi(R.id.placeMainFragment)
        }

        // <여행 피드> 클릭
        btnFeed.setOnClickListener {
            setNavi(R.id.feedFragment)
        }

        // <나의 여행 일지> 클릭
        btnMyPost.setOnClickListener {
            setNavi(R.id.postBlogFragment)
        }

    }

    fun setNavi(fregmentId : Int) {
        val outIntent = Intent(applicationContext, NaviActivity::class.java)
        outIntent.putExtra("select", fregmentId)
        setResult(Activity.RESULT_OK, outIntent)
        finish()
    }


}