package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.travelencer_android_2021.databinding.FragmentFeedFilterBinding

private const val TAG_FEED_FILTER = "feed_filter_fragment"
private const val TAG_FEED = "feed_fragment"

private const val SP_FEED_FILTERED: String = "feedFiltered"

// 여행 피드 - 필터
class FeedFilterFragment : Fragment() {
    private var _binding: FragmentFeedFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFeedFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        //이 화면으로 오면 필터 설정이 해제됨
        val pref = activity?.getSharedPreferences("pref", 0)
        val edit = pref?.edit()
        edit?.putBoolean(SP_FEED_FILTERED, false)
        edit?.apply()
        Log.d("로그 -feedFiltered-1--", "feedFiltered : ${pref?.getBoolean(SP_FEED_FILTERED, true)}")


        // 검색 버튼 눌렀을 때
        // sharedPreferences 의 SF_FEED_FILTERED값 true로 변경
        // preantFragmentManager에 접근해서 현재 feedFilter 프래그먼트 remove, feed 프래그먼트 add
        // <피드 보기> 버튼 클릭
        binding.btnShowFeed.setOnClickListener {
            val keyword = binding.etSearchKeyword.text.toString()
            if (keyword == "") {
                Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            edit?.putBoolean(SP_FEED_FILTERED, true)
            edit?.apply()
            Log.d("로그 -filtered-2--", "feedFiltered : ${pref?.getBoolean(SP_FEED_FILTERED, false)}")

            val parentManager: FragmentManager = parentFragmentManager
            val pft: FragmentTransaction = parentManager.beginTransaction()

            // 키워드 전달하기
            val feedFrag = FeedFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)    //검색어
            feedFrag.arguments = bundle
            pft.add(R.id.flContainer, feedFrag, TAG_FEED)

            val feedFilter = parentManager.findFragmentByTag(TAG_FEED_FILTER)

            feedFilter?.let {pft.remove(it)}

            pft.commitAllowingStateLoss()
        }

        return view
    }

}