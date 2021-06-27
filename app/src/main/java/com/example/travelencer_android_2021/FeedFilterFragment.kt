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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val view = inflater.inflate(R.layout.fragment_feed_filter, container, false)
        _binding = FragmentFeedFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        spinner = arrayOf(view.findViewById(R.id.spinPlaceLarge), view.findViewById(R.id.spinPlaceSmall))

        // 스피너 설정
        FetxhXML(spinner, context as NaviActivity).fetchXML("http://api.visitkorea.or.kr/upload/manual/sample/areaCode_sample1.xml", 0)

        //지역 아이템 목록 더미 데이터
        val placeLargeItemList: Array<String> = resources.getStringArray(R.array.place_large_item_list)
        val placeSmallItemList: Array<String> = resources.getStringArray(R.array.place_small_item_list)

        //두 스피너 어댑터. 프래그먼트이므로 context가져올 때 activity가 null인지 아닌지 확인
        val placeLargeAdapter = activity?.let{ ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, placeLargeItemList) }
        val placeSmallAdapter = activity?.let{ ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, placeSmallItemList) }

        binding.spinPlaceLarge.adapter = placeLargeAdapter
        binding.spinPlaceLarge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    Toast.makeText(activity, placeLargeItemList[position], Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinPlaceSmall.adapter = placeSmallAdapter
        binding.spinPlaceSmall.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    Toast.makeText(activity, placeSmallItemList[position], Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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
            edit?.putBoolean(SP_FEED_FILTERED, true)
            edit?.apply()
            Log.d("로그 -filtered-2--", "feedFiltered : ${pref?.getBoolean(SP_FEED_FILTERED, false)}")


            val parentManager: FragmentManager = parentFragmentManager
            val pft: FragmentTransaction = parentManager.beginTransaction()

            pft.add(R.id.flContainer, FeedFragment(), TAG_FEED)

            val feedFilter = parentManager.findFragmentByTag(TAG_FEED_FILTER)

            feedFilter?.let {pft.remove(it)}

            pft.commitAllowingStateLoss()
        }

        return view
    }

}