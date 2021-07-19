package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedSightsAdapter
import com.example.travelencer_android_2021.model.ModelCourseSpot

// 여행 피드 - 관광지 탭
class FeedSightsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_feed_sights, container, false)

        val rcFeedSights = view.findViewById<RecyclerView>(R.id.rcFeedSights)
        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        rcFeedSights.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedSightsAdapter = FeedSightsAdapter()
        rcFeedSights.adapter = feedSightsAdapter

        feedSightsAdapter.items.add(ModelCourseSpot("수원 화성", "고양시 수원시"))
        feedSightsAdapter.items.add(ModelCourseSpot("어디어디 마을", "어쩌구"))
        feedSightsAdapter.items.add(ModelCourseSpot("이런이런 공원", "저꺼고"))
        feedSightsAdapter.items.add(ModelCourseSpot("호수공원", "고양시 덕양구"))
        feedSightsAdapter.items.add(ModelCourseSpot("산길 공원", "저러저러"))
        feedSightsAdapter.items.add(ModelCourseSpot("밤가시마을", "이러쿵"))
        feedSightsAdapter.items.add(ModelCourseSpot("한강공원", "저러쿵"))

        return view
    }

}