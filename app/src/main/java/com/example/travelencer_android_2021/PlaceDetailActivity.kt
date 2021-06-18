package com.example.travelencer_android_2021

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.PlaceDetailPhotoAdapter
import com.example.travelencer_android_2021.adapter.PlaceDetailRecentPostAdapter
import com.example.travelencer_android_2021.databinding.ActivityPlaceDetailBinding
import com.example.travelencer_android_2021.model.ModelCasePhotoOnly
import com.example.travelencer_android_2021.model.ModelPlaceDetailRecentPost
import kotlinx.android.synthetic.main.activity_place_detail.*

//뷰바인딩 사용

class PlaceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val photoList = arrayListOf(
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung)
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
        
        //TODO : bookmarked 값 서버에서 데이터 받아와서 쓰기
        var bookmarked = false
        binding.ivPlaceDetailBookmark.setOnClickListener{
            if(!bookmarked){
                ivPlaceDetailBookmark.setImageResource(R.drawable.ic_bookmark_filled)
                bookmarked = true
                Toast.makeText(this, "즐겨찾기 되었습니다", Toast.LENGTH_SHORT).show()
            }
            else if(bookmarked){
                ivPlaceDetailBookmark.setImageResource(R.drawable.ic_bookmark_line)
                bookmarked = false
                Toast.makeText(this, "즐겨찾기 해제 되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 사진 한장씩 넘어감
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvPlaceDetailPhotoList)

        // 사진 번호 표시
        binding.tvPlaceDetailPhotoNumber.text = "1 / ".plus(photoList.size)
        binding.rvPlaceDetailPhotoList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val curPhotoNum = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val photoCounter = "${curPhotoNum+1} / ${photoList.size}"
                binding.tvPlaceDetailPhotoNumber.text = photoCounter
            }
        })

        binding.ivPlaceDetailPNCMore.setOnClickListener{
            val intent = Intent(this, PNCActivity::class.java)
            startActivity(intent)
        }


    }
}