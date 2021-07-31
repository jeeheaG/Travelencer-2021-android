package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedCourseAdapter

// 여행 피드 - 코스 탭
class FeedCourseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_feed_course, container, false)

        val rcFeedCourse = view.findViewById<RecyclerView>(R.id.rcFeedCourse)
        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        rcFeedCourse.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedCourseAdapter = activity?.let { FeedCourseAdapter(it) }
        rcFeedCourse.adapter = feedCourseAdapter


        val spotNameList1 : ArrayList<String> = arrayListOf("이러쿵지역","저러쿵지역긴지역명도잘라서보여준다", "3지역", "마지막커브지역", "다섯번째지역", "여섯번째지역", "일곱", "888", "아홉번째", "10번", "11번", "12번", "13번", "14번", "15번", "16번", "17번")
        val spotNameList2 : ArrayList<String> = arrayListOf("1","2", "3식당", "4여행지")
        val spotNameList3 : ArrayList<String> = arrayListOf("111","222", "333", "444", "555")
        val spotNameList4 : ArrayList<String> = arrayListOf("하나","둘")
        val spotNameList5 : ArrayList<String> = arrayListOf("서울","부산", "춘천", "제주", "프랑스", "스위스")
        val spotNameList6 : ArrayList<String> = arrayListOf("여","행", "여여", "행행", "여여여", "행행행", "여행")

        feedCourseAdapter!!.items.add(spotNameList1)
        feedCourseAdapter.items.add(spotNameList2)
        feedCourseAdapter.items.add(spotNameList3)
        feedCourseAdapter.items.add(spotNameList4)
        feedCourseAdapter.items.add(spotNameList5)
        feedCourseAdapter.items.add(spotNameList6)

        return view
    }

}