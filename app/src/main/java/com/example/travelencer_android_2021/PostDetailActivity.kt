package com.example.travelencer_android_2021

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostDetailPhotoAdapter
import com.example.travelencer_android_2021.adapter.PostDetailPlaceAdapter
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

        val courseCount: Int = 4

        val courseLayout1 = LinearLayout(this)
        courseLayout1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL)

        val coursePiece1 = CoursePiece(this, "start", "이러쿵지역")
        val coursePiece2 = CoursePiece(this, "middle", "저러쿵지역")
        val coursePiece3 = CoursePiece(this, "middle", "지역")
        courseLayout1.addView(coursePiece1)
        courseLayout1.addView(coursePiece2)
        courseLayout1.addView(coursePiece3)


        val coursePiece4 = CoursePiece(this, "end", "마지막지역")
        courseLayout1.addView(coursePiece4)
        binding.llPostDetailCourse.addView(courseLayout1)

    }
}