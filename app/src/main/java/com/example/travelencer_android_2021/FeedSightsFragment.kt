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
class FeedSightsFragment(val keyword : String) : Fragment() {
    private var _binding : FragmentFeedSightsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedSightsBinding.inflate(inflater, container, false)

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        binding.rcFeedSights.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedSightsAdapter = FeedSightsAdapter()
        binding.rcFeedSights.adapter = feedSightsAdapter
        // divider 추가
        binding.rcFeedSights.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        feedSightsAdapter.items.add(ModelCourseSpot("방화수류정", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("화성행궁", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("수원화성", "수원시 장안구"))
        feedSightsAdapter.items.add(ModelCourseSpot("만석공원", "수원시 장안구"))
        feedSightsAdapter.items.add(ModelCourseSpot("창룡문", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("해우재", "수원시 장안구"))
        feedSightsAdapter.items.add(ModelCourseSpot("광교 호수공원", "수원시 영통구"))
        feedSightsAdapter.items.add(ModelCourseSpot("효원공원월화뭔", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("플라잉 수원", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("수원화성박물관", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("장안문", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("수원 시립 아이파크 미술관", "수원시 팔달구"))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}