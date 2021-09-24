package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedFoodAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedFoodBinding
import com.example.travelencer_android_2021.model.ModelCourseSpot

// 여행 피드 - 맛집 탭
class FeedFoodFragment : Fragment() {
    private lateinit var binding : FragmentFeedFoodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedFoodBinding.inflate(inflater, container, false)

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        binding.rcFeedFood.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedFoodAdapter = FeedFoodAdapter()
        binding.rcFeedFood.adapter = feedFoodAdapter
        // divider 추가
        binding.rcFeedFood.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        feedFoodAdapter.items.add(ModelCourseSpot("지아니스나폴리", "수원시 영통구"))
        feedFoodAdapter.items.add(ModelCourseSpot("좋은 소식", "수원시 영통구"))
        feedFoodAdapter.items.add(ModelCourseSpot("이나경 송탄 부대찌개", "수원시 팔달구"))
        feedFoodAdapter.items.add(ModelCourseSpot("평장원", "수원시 팔달구"))
        feedFoodAdapter.items.add(ModelCourseSpot("을라메히꼬", "수원시 팔달구"))
        feedFoodAdapter.items.add(ModelCourseSpot("가보정1관", "수원시 팔달구"))
        feedFoodAdapter.items.add(ModelCourseSpot("슬리핑테이블", "수원시 팔달구"))
        feedFoodAdapter.items.add(ModelCourseSpot("우판 등심", "수원시 영통구"))
        feedFoodAdapter.items.add(ModelCourseSpot("윤가 곰탕", "수원시 영통구"))

        return binding.root
    }

}