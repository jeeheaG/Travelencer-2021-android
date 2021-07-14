package com.example.travelencer_android_2021

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostDetailPhotoAdapter
import com.example.travelencer_android_2021.adapter.PostDetailPlaceAdapter
import com.example.travelencer_android_2021.course.CourseMaker
import com.example.travelencer_android_2021.course.CoursePiece
import com.example.travelencer_android_2021.databinding.ActivityPostDetailBinding
import com.example.travelencer_android_2021.model.ModelCasePhotoOnly
import com.example.travelencer_android_2021.model.ModelPostDetailPlace

//뷰바인딩 사용

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val placeList = arrayListOf(
                ModelPostDetailPlace(R.drawable.ic_location_yellow, "해우재", "경기도 수원시"),
                ModelPostDetailPlace(R.drawable.ic_food, "삼겹구이", "경기도 용인시"),
                ModelPostDetailPlace(R.drawable.ic_location_yellow, "수원 화성", "경기도 수원시"),
                ModelPostDetailPlace(R.drawable.ic_location_yellow, "해우재", "경기도 수원시")
        )
        val photoList = arrayListOf(
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae)
        )

        binding.rvPostDetailPlaceList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPostDetailPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvPostDetailPlaceList.setHasFixedSize(true)
        binding.rvPostDetailPhotoList.setHasFixedSize(true)

        binding.rvPostDetailPlaceList.adapter = PostDetailPlaceAdapter(placeList)
        binding.rvPostDetailPhotoList.adapter = PostDetailPhotoAdapter(photoList)

        binding.ivPostDetailProfileImg.background = ShapeDrawable(OvalShape())
        binding.ivPostDetailProfileImg.clipToOutline = true //안드로이드 버전 5 롤리팝 이상에서만 적용


        //코스 스팟 개수에 따른 코스 뷰 구성
        //TODO : 서버데이터연결 spotNameList
        val spotNameList = arrayListOf("이러쿵지역","저러쿵지역긴지역명도잘라서보여준다", "3지역", "마지막커브지역", "다섯번째지역", "여섯번째지역", "일곱", "888", "아홉번째", "10번", "11번", "12번", "13번", "14번", "15번", "16번", "17번")

        CourseMaker().makeCourse(spotNameList, binding.llPostDetailCourse, this)
        // 코스 클릭하면 상세 코스 보이기
        binding.llPostDetailCourse.setOnClickListener {
            val intent = Intent(applicationContext, FeedCourseDetailActivity::class.java)
            intent.putExtra("course", spotNameList)
            startActivity(intent)
        }

        //TODO : 현재 게시물 정보를 가지고 수정페이지로 이동하도록 만들기
        binding.btnPostDetailEdit.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
        }

//코스 출력 테스트용 코드
//        for(i in 0..15){
//            CourseMaker().makeCourse(ArrayList(spotNameList.subList(0,i+2)), binding.llPostDetailCourse, this)
//        }
    }
}