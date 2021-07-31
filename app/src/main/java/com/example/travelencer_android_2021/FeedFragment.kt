package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelencer_android_2021.adapter.FeedAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*

// 여행 피드 프레그먼트(여행 피드 필터 결과)
// 사진, 코스, 맛집, 관광지 탭
class FeedFragment : Fragment() {
    private val tabElement = arrayListOf("사진", "코스", "맛집", "관광지")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        // 전달받은 필터 선택된 값으로 텍스트뷰 변경
        val bundle = arguments
        if (bundle != null) {
            val area1 = bundle.getString("area1").toString()    // 지역명
            val area2 = bundle.getString("area2").toString()    // 시군구명
            view.tvArea.text = area2
            view.tvArea2.text = area1
        }

        // 어댑터 생성
        val feedAdapter = FeedAdapter(this@FeedFragment)
        // 프레그먼트, 탭 타이틀 넣기
        feedAdapter.addFragment(FeedPhotoFragment())
        feedAdapter.addFragment(FeedCourseFragment())
        feedAdapter.addFragment(FeedFoodFragment())
        feedAdapter.addFragment(FeedSightsFragment())
        view.feedViewPager.adapter = feedAdapter
        // 탭레이아웃에 뷰 페이저 달기
        TabLayoutMediator(view.feedTabLayout, view.feedViewPager) { tab, position ->
            tab.text = tabElement[position]
        }.attach()

        return view
    }

}
