package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedCourseAdapter

// 게시물 - 코스 탭
class PostCourseFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_course, container, false)

        val rcFeedCourse = view.findViewById<RecyclerView>(R.id.rcFeedCourse)
        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        rcFeedCourse.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedCourseAdapter = activity?.let { FeedCourseAdapter(it) }
        rcFeedCourse.adapter = feedCourseAdapter
        // divider 추가
        rcFeedCourse.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val spotNameList1 : ArrayList<String> = arrayListOf("가", "나", "다", "라", "미", "비", "사",  "아",  "자")
        val spotNameList2 : ArrayList<String> = arrayListOf("게", "시", "물")
        val spotNameList3 : ArrayList<String> = arrayListOf("1번", "2번", "3번", "4번")
        val spotNameList4 : ArrayList<String> = arrayListOf("하나","둘", "셋", "넷", "다섯")

        feedCourseAdapter!!.items.add(spotNameList1)
        feedCourseAdapter.items.add(spotNameList2)
        feedCourseAdapter.items.add(spotNameList3)
        feedCourseAdapter.items.add(spotNameList4)

        return view
    }

}