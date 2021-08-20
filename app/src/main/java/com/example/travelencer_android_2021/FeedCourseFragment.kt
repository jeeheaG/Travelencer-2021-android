package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedCourseAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedCourseBinding

// 여행 피드 - 코스 탭
class FeedCourseFragment : Fragment() {
    private var _binding : FragmentFeedCourseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedCourseBinding.inflate(inflater, container, false)

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        binding.rcFeedCourse.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedCourseAdapter = activity?.let { FeedCourseAdapter(it) }
        binding.rcFeedCourse.adapter = feedCourseAdapter
        // divider 추가
        binding.rcFeedCourse.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val spotNameList1 : ArrayList<String> = arrayListOf("연무대", "화성여차", "서장대", "화성행궁", "공방거리", "재래시장", "화성 박물관", "통닭거리", "플라잉 수원")
        val spotNameList2 : ArrayList<String> = arrayListOf("광교 호수공원", "수원화성", "화성 행궁", "공방거리")
        val spotNameList3 : ArrayList<String> = arrayListOf("팔달마을", "수원화성", "행굴동 벽화마을", "지동 벽화마을", "방화 수류청")
        val spotNameList4 : ArrayList<String> = arrayListOf("노을빛 전망대", "통닭거리", "지동 시장", "수원 화성 박물관", "연무대")
        val spotNameList5 : ArrayList<String> = arrayListOf("수원 화성 박물관", "아이파크 미술관", "르본 수원 실크로드 호텔", "광교 호수공원")
        val spotNameList6 : ArrayList<String> = arrayListOf("해우재", "회서문", "화성 행궁", "장안문", "화홍문", "연무대", "수원화성 박물관")

        feedCourseAdapter!!.items.add(spotNameList1)
        feedCourseAdapter.items.add(spotNameList2)
        feedCourseAdapter.items.add(spotNameList3)
        feedCourseAdapter.items.add(spotNameList4)
        feedCourseAdapter.items.add(spotNameList5)
        feedCourseAdapter.items.add(spotNameList6)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}