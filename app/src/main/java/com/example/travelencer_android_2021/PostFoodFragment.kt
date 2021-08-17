package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedFoodAdapter
import com.example.travelencer_android_2021.model.ModelCourseSpot

// 게시물 - 맛집 탭
class PostFoodFragment : Fragment() {
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
        // divider 추가
        rcFeedFood.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        feedFoodAdapter.items.add(ModelCourseSpot("게시물 맛집", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("맛지1", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("맛집2", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("맛집3", "고양시 덕양구"))
        feedFoodAdapter.items.add(ModelCourseSpot("맛있겠다", "고양시 덕양구"))

        return view
    }

}