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

        val spotNameList1 : ArrayList<String> = arrayListOf("연무대", "화성여차", "서장대", "화성행궁", "공방거리", "재래시장", "화성 박물관", "통닭거리", "플라잉 수원")
        val spotNameList2 : ArrayList<String> = arrayListOf("광교 호수공원", "수원화성", "화성 행궁", "공방거리")
        val spotNameList3 : ArrayList<String> = arrayListOf("팔달마을", "수원화성", "행굴동 벽화마을", "지동 벽화마을", "방화 수류청")
        val spotNameList4 : ArrayList<String> = arrayListOf("노을빛 전망대", "통닭거리", "지동 시장", "수원 화성 박물관", "연무대")

        feedCourseAdapter!!.placeNames.add(spotNameList1)
        feedCourseAdapter.placeNames.add(spotNameList2)
        feedCourseAdapter.placeNames.add(spotNameList3)
        feedCourseAdapter.placeNames.add(spotNameList4)

        return view
    }

}