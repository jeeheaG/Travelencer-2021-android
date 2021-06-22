package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.travelencer_android_2021.databinding.FragmentPlaceFilterBinding
import kotlinx.android.synthetic.main.fragment_feed_filter.view.*

// 여행 피드 - 필터
class FeedFilterFragment : Fragment() {
    private var _binding: FragmentPlaceFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val view = inflater.inflate(R.layout.fragment_feed_filter, container, false)
        _binding = FragmentPlaceFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        //지역 아이템 목록 더미 데이터
        val placeLargeItemList: Array<String> = resources.getStringArray(R.array.place_large_item_list)
        val placeSmallItemList: Array<String> = resources.getStringArray(R.array.place_small_item_list)

        //어댑터. 프래그먼트이므로 context가져올 때 activity가 null인지 아닌지 확인
        val PlaceLargeAdapter = activity?.let{ ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, placeLargeItemList) }
        val PlaceSmallAdapter = activity?.let{ ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, placeSmallItemList) }

        binding.spinPlaceLarge.adapter = PlaceLargeAdapter
        binding.spinPlaceLarge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    Toast.makeText(activity, placeLargeItemList[position], Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinPlaceSmall.adapter = PlaceSmallAdapter
        binding.spinPlaceSmall.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    Toast.makeText(activity, placeSmallItemList[position], Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 검색 버튼 눌렀을 때
//        binding.btnSearch.setOnClickListener {
//            val parentManager: FragmentManager = parentFragmentManager
//            val pft: FragmentTransaction = parentManager.beginTransaction()
//
//            pft.add(R.id.flContainer, PlaceMainFragment(), TAG_PLACE_MAIN)
//
//            val placeFilter = parentManager.findFragmentByTag(TAG_PLACE_FILTER)
//            val placeMain = parentManager.findFragmentByTag(TAG_PLACE_MAIN)
//
//            placeFilter?.let {pft.remove(it)}
//
//            pft.commitAllowingStateLoss()
//        }
//
        return view
    }

}