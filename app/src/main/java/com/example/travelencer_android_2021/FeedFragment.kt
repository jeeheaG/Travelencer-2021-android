package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelencer_android_2021.adapter.FeedAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*


// 여행 피드 프레그먼트(여행 피드 필터 결과)
// 사진, 코스, 맛집, 관광지 탭
class FeedFragment : Fragment() {
    private var _binding : FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val tabElement = arrayListOf("사진", "코스", "맛집", "관광지")

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)

        // 전달받은 필터 선택된 값으로 텍스트뷰 변경
        val bundle = arguments
        // 검색어
        val keyword = bundle?.getString("keyword")?.toString() ?: "오류"
        binding.tvKeyword.text = keyword

        // 어댑터 생성
        val feedAdapter = FeedAdapter(this@FeedFragment)
        // 프레그먼트, 탭 타이틀 넣기
        feedAdapter.addFragment(FeedPhotoFragment(keyword))
        feedAdapter.addFragment(FeedCourseFragment(keyword))
        feedAdapter.addFragment(FeedFoodFragment(keyword))
        feedAdapter.addFragment(FeedSightsFragment(keyword))
        binding.feedViewPager.adapter = feedAdapter
        // 탭레이아웃에 뷰 페이저 달기
        TabLayoutMediator(binding.feedTabLayout, binding.feedViewPager) { tab, position ->
            tab.text = tabElement[position]
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}