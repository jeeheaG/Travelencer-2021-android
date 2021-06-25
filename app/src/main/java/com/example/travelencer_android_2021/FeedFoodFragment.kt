package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedFoodAdapter
import com.example.travelencer_android_2021.model.ModelFeedFood

// 여행 피드 - 맛집 탭
class FeedFoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_feed_food, container, false)

        val rcFeedFood = view.findViewById<RecyclerView>(R.id.rcFeedFood)
        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        rcFeedFood.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedFoodAdapter = FeedFoodAdapter()
        rcFeedFood.adapter = feedFoodAdapter

        feedFoodAdapter.items.add(ModelFeedFood("마라마라탕", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("과일가게", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("명동 칼숙수", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("어쩌구 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("저쩌고 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("이러쿵 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("저러쿨 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("이런 식당", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelFeedFood("저런 식당", "고양시 덕양구"))

        return view
    }

}