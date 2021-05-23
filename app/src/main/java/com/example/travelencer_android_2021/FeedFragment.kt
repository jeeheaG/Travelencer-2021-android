package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*

// 여행 피드 프레그먼트(여행 피드 필터 결과)
// 사진, 코스, 맛집, 관광지 탭
class FeedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        // 어댑터 생성
        val feedAdapter = FeedAdapter((activity as MainActivity).supportFragmentManager)
        // 프레그먼트, 탭 타이틀 넣기
        feedAdapter.addFragment(FeedPhotoFragment(), "사진")
        feedAdapter.addFragment(FeedCourseFragment(), "코스")
        feedAdapter.addFragment(FeedFoodFragment(), "맛집")
        feedAdapter.addFragment(FeedSightsFragment(), "관광지")
        view.feedViewPager.adapter = feedAdapter
        // 탭레이아웃에 뷰 페이저 달기
        view.feedTabLayout.setupWithViewPager(feedViewPager)

        return view
    }

    companion object {
    }
}