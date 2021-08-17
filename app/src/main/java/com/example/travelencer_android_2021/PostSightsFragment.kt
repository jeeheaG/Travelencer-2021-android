package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedSightsAdapter
import com.example.travelencer_android_2021.model.ModelCourseSpot

// binding 인 됨...
// 게시물 - 관광지 탭
class PostSightsFragment : Fragment() {

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
        // divider 추가
        rcFeedSights.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        feedSightsAdapter.items.add(ModelCourseSpot("게시물 관광지", "고양시 수원시"))
        feedSightsAdapter.items.add(ModelCourseSpot("개미마을", "어쩌구"))
        feedSightsAdapter.items.add(ModelCourseSpot("어디어디동네", "저꺼고"))
        feedSightsAdapter.items.add(ModelCourseSpot("방바다", "고양시 덕양구"))
        feedSightsAdapter.items.add(ModelCourseSpot("하늘공원", "저러저러"))
        feedSightsAdapter.items.add(ModelCourseSpot("지나가던 마을", "이러쿵"))

        return view
    }

}