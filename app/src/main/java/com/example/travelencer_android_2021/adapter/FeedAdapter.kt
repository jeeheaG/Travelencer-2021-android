package com.example.travelencer_android_2021.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// 여행 피드 내 사진, 코스, 맛집, 관광지 탭 관리하는 어댑터
class FeedAdapter(fragment: Fragment) : FragmentStateAdapter(fragment)  {
    // 프레그먼트 배열
    private val fragmentList = ArrayList<Fragment>()

    // 프레그먼트, 탭 타이틀 추가
    fun addFragment(fragment: Fragment) = fragmentList.add(fragment)

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]
}