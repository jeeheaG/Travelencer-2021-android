package com.example.travelencer_android_2021

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PlaceDetailPhotoAdapter
import com.example.travelencer_android_2021.adapter.PlaceDetailRecentPostAdapter
import com.example.travelencer_android_2021.databinding.ActivityPlaceDetailBinding
import com.example.travelencer_android_2021.model.ModelPlaceDetailPhoto
import com.example.travelencer_android_2021.model.ModelPlaceDetailRecentPost

//뷰바인딩 사용

class PlaceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val photoList = arrayListOf(
                ModelPlaceDetailPhoto(R.drawable.dummy_hwasung),
                ModelPlaceDetailPhoto(R.drawable.dummy_haewoojae),
                ModelPlaceDetailPhoto(R.drawable.dummy_haewoojae),
                ModelPlaceDetailPhoto(R.drawable.dummy_hwasung),
                ModelPlaceDetailPhoto(R.drawable.dummy_hwasung)
        )
        val recentPostList = arrayListOf(
                ModelPlaceDetailRecentPost("날 좋은 날 화성 나들이", "jeehea", R.drawable.dummy_haewoojae),
                ModelPlaceDetailRecentPost("수원화성 놀러감", "yoojin", R.drawable.dummy_haewoojae),
                ModelPlaceDetailRecentPost("수원으로 소풍", "yoonkung", R.drawable.dummy_hwasung),
                ModelPlaceDetailRecentPost("수원 놀기 좋은 코스 추천", "sewon", R.drawable.dummy_hwasung),
                ModelPlaceDetailRecentPost("놀러가자 수원", "minyeong", R.drawable.dummy_haewoojae)
        )

        binding.rvPlaceDetailPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlaceDetailRecentPostList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvPlaceDetailPhotoList.setHasFixedSize(true)
        binding.rvPlaceDetailRecentPostList.setHasFixedSize(true)

        binding.rvPlaceDetailPhotoList.adapter = PlaceDetailPhotoAdapter(photoList)
        binding.rvPlaceDetailRecentPostList.adapter = PlaceDetailRecentPostAdapter(recentPostList)

        //TODO:리사이클러뷰에서 현재 포지션 받아와서 출력
        binding.tvPlaceDetailPhotoNumber.text = "1 / ".plus(photoList.size.toString())

        binding.ivPlaceDetailPNCMore.setOnClickListener{
            val intent = Intent(this, PNCActivity::class.java)
            startActivity(intent)
        }


    }
}