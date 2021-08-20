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

        feedSightsAdapter.items.add(ModelCourseSpot("수원화성", "수원시 장안구"))
        feedSightsAdapter.items.add(ModelCourseSpot("만석공원", "수원시 장안구"))
        feedSightsAdapter.items.add(ModelCourseSpot("창룡문", "수원시 팔달구"))
        feedSightsAdapter.items.add(ModelCourseSpot("해우재", "수원시 장안구"))
        feedSightsAdapter.items.add(ModelCourseSpot("광교 호수공원", "수원시 영통구"))

        return view
    }

}