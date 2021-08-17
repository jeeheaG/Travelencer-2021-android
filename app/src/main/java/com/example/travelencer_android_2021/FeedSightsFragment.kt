package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedSightsAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedSightsBinding
import com.example.travelencer_android_2021.model.ModelCourseSpot

// 여행 피드 - 관광지 탭
class FeedSightsFragment : Fragment() {
    private lateinit var binding : FragmentFeedSightsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedSightsBinding.inflate(inflater, container, false)

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        binding.rcFeedSights.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedSightsAdapter = FeedSightsAdapter()
        binding.rcFeedSights.adapter = feedSightsAdapter
        // divider 추가
        binding.rcFeedSights.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        feedSightsAdapter.items.add(ModelCourseSpot("수원 화성", "고양시 수원시"))
        feedSightsAdapter.items.add(ModelCourseSpot("어디어디 마을", "어쩌구"))
        feedSightsAdapter.items.add(ModelCourseSpot("이런이런 공원", "저꺼고"))
        feedSightsAdapter.items.add(ModelCourseSpot("호수공원", "고양시 덕양구"))
        feedSightsAdapter.items.add(ModelCourseSpot("산길 공원", "저러저러"))
        feedSightsAdapter.items.add(ModelCourseSpot("밤가시마을", "이러쿵"))
        feedSightsAdapter.items.add(ModelCourseSpot("한강공원", "저러쿵"))

        return binding.root
    }

}