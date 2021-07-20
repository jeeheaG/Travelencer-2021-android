package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

// 홈 액티비티
class MainActivity : AppCompatActivity() {
    // 뒤로가기 연속 클릭 대기 시간
    var mBackWait : Long = 0

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

    // 뒤로가기 2번 누르면 앱 종료
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >= 2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this@MainActivity,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        }
        else setNavi(-1)
    }

}