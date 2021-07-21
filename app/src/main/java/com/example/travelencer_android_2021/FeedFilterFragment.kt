package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.travelencer_android_2021.databinding.FragmentFeedFilterBinding
import com.example.travelencer_android_2021.model.FetxhXML

private const val TAG_FEED_FILTER = "feed_filter_fragment"
private const val TAG_FEED = "feed_fragment"

private const val SP_FEED_FILTERED: String = "feedFiltered"

// 여행 피드 - 필터
class FeedFilterFragment : Fragment() {
    private var _binding: FragmentFeedFilterBinding? = null
    private val binding get() = _binding!!

    lateinit var spinner : Array<Spinner>   // 스피너 배열

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFeedFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        spinner = arrayOf(view.findViewById(R.id.spinPlaceLarge), view.findViewById(R.id.spinPlaceSmall))

        // 스피너 설정
        FetxhXML(spinner, context as NaviActivity).fetchXML("http://api.visitkorea.or.kr/upload/manual/sample/areaCode_sample1.xml", 0)

        //이 화면으로 오면 필터 설정이 해제됨
        val pref = activity?.getSharedPreferences("pref", 0)
        val edit = pref?.edit()
        edit?.putBoolean(SP_FEED_FILTERED, false)
        edit?.apply()
        Log.d("로그 -feedFiltered-1--", "feedFiltered : ${pref?.getBoolean(SP_FEED_FILTERED, true)}")


        // 검색 버튼 눌렀을 때
        // sharedPreferences 의 SF_FEED_FILTERED값 true로 변경
        // preantFragmentManager에 접근해서 현재 feedFilter 프래그먼트 remove, feed 프래그먼트 add

        binding.btnShowFeed.setOnClickListener {
            if (spinner[1]?.selectedItem.toString() != null) {
                edit?.putBoolean(SP_FEED_FILTERED, true)
                edit?.apply()
                Log.d("로그 -filtered-2--", "feedFiltered : ${pref?.getBoolean(SP_FEED_FILTERED, false)}")

                val parentManager: FragmentManager = parentFragmentManager
                val pft: FragmentTransaction = parentManager.beginTransaction()

                // 지역명 전달하기
                val feedFrag = FeedFragment()
                val bundle = Bundle()
                bundle.putString("area1", spinner[0].selectedItem.toString())    // 지역명
                bundle.putString("area2", spinner[1].selectedItem.toString())    // 시군구명
                if (feedFrag != null) feedFrag!!.setArguments(bundle)
                pft.add(R.id.flContainer, feedFrag, TAG_FEED)

                val feedFilter = parentManager.findFragmentByTag(TAG_FEED_FILTER)

                feedFilter?.let {pft.remove(it)}

                pft.commitAllowingStateLoss()
            }
        }

        return view
    }

}