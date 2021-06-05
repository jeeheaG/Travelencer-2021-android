package com.example.travelencer_android_2021.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

// 여행 피드 내 사진, 코스, 맛집, 관광지 탭 관리하는 어댑터
class FeedAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager) {
    // 프레그먼트 배열
    val fragmentList = ArrayList<Fragment>()
    // 탭 타이틀 배열
    val titleList = ArrayList<String>()

    // 선택한 프레그먼트 리턴
    override fun getItem(position: Int): Fragment = fragmentList[position]

    // 프레그먼트 갯수 리턴
    override fun getCount(): Int = fragmentList.size

    // 탭 타이틀 리턴
    override fun getPageTitle(position: Int): CharSequence = titleList[position]

    // 프레그먼트, 탭 타이틀 추가
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}