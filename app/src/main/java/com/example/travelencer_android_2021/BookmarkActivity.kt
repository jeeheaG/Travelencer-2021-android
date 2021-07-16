package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PlaceMainAdapter
import com.example.travelencer_android_2021.databinding.ActivityBookmarkBinding
import com.example.travelencer_android_2021.model.ModelCasePlaceCard

//뷰바인딩 사용

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val placeList = arrayListOf(
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_hwasung, "수원 화성", "경기도 수원시", "화성는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "무슨산", "경기도 수원시", "가가가가가 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "관광지", "경기도 수원시", "관광지는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "뭐라고", "경기도 수원시", "뭐는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "광교산", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "왕갈비", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "무슨호수", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "저기공원", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ")
        )

        binding.rvBookmarkList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBookmarkList.setHasFixedSize(true)

        binding.rvBookmarkList.adapter = PlaceMainAdapter(placeList, this)

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}