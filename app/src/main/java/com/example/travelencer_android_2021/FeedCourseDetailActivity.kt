package com.example.travelencer_android_2021

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedCourseDetailAdapter
import kotlinx.android.synthetic.main.activity_feed_course_detail.*

class FeedCourseDetailActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_course_detail)

        // 코스 배열
        // TODO : 서버 연결 되면 코스 ID 받아오기
        val course = intent.getStringArrayListExtra("course")
        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(this@FeedCourseDetailActivity)
        rcFeedCourseDetail.layoutManager = layoutManager
        // 리사이클러뷰에 어댑터 달기
        val feedCourseDetailAdapter = FeedCourseDetailAdapter()
        // TODO : 서버 연결 되면 코스 ID로 코스 이름, 위치, 날짜 배열 얻어와서 설정
        feedCourseDetailAdapter.courseName = course!!           // 코스 배열 설정
        rcFeedCourseDetail.adapter = feedCourseDetailAdapter

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

        // 동그란 프로틸 사진
        imgCourseDetailProfileImg.background = ShapeDrawable(OvalShape())
        imgCourseDetailProfileImg.clipToOutline = true //안드로이드 버전 5 롤리팝 이상에서만 적용
    }
}