package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedFoodAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedFoodBinding
import com.example.travelencer_android_2021.databinding.FragmentFeedSightsBinding
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

        feedFoodAdapter.items.add(ModelCourseSpot("마라마라탕", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("과일가게", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("명동 칼숙수", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("어쩌구 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("저쩌고 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("이러쿵 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("저러쿨 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("이런 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("저런 식당", "고양시 덕양구"))

        return binding.root
    }

}