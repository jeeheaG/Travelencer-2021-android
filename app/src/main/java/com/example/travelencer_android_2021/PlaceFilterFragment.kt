package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.travelencer_android_2021.databinding.FragmentPlaceFilterBinding
//TODO : 스피너 목록도 관광API로 가져올 수 있지 않았나?
//뷰바인딩 사용

private const val TAG_PLACE_FILTER = "place_filter_fragment"
private const val TAG_PLACE_MAIN = "place_main_fragment"

class PlaceFilterFragment : Fragment() {
    private var _binding: FragmentPlaceFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaceFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        //이 화면으로 오면 필터 설정이 해제됨
        val pref = activity?.getSharedPreferences("pref", 0)
        val edit = pref?.edit()
        edit?.putBoolean("filtered", false)
        edit?.apply()
        Log.d("로그 -filtered-1--", "filtered : ${pref?.getBoolean("filtered", true)}")

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

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //("Not yet implemented")
            }
        }

        binding.spinPlaceSmall.adapter = PlaceSmallAdapter
        binding.spinPlaceSmall.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    Toast.makeText(activity, placeSmallItemList[position], Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //("Not yet implemented")
            }

        }

        // 검색 버튼 눌렀을 때
        // sharedPreferences 의 filtered값 true로 변경
        // preantFragmentManager에 접근해서 현재 placeFilter 프래그먼트 remove, placeMain 프래그먼트 add
        // TODO : 이후 지역정보도 sharedPreferences로 넘기면 될 듯

        binding.btnSearch.setOnClickListener {
            Toast.makeText(activity, "btnSearch onClicked", Toast.LENGTH_SHORT).show()

            edit?.putBoolean("filtered", true)
            edit?.apply()
            Log.d("로그 -filtered-2--", "filtered : ${pref?.getBoolean("filtered", false)}")


            val parentManager: FragmentManager = parentFragmentManager
            val pft: FragmentTransaction = parentManager.beginTransaction()

            pft.add(R.id.flContainer, PlaceMainFragment(), TAG_PLACE_MAIN)

            val placeFilter = parentManager.findFragmentByTag(TAG_PLACE_FILTER)
            val placeMain = parentManager.findFragmentByTag(TAG_PLACE_MAIN)

            Log.d("로그 at filter---", "parentManager에서 tag로 찾은 것들 값 확인. NULL인 건 반영x \n" +
                    "feed : ${parentManager.findFragmentByTag("feed_fragment")}\n" +
                    "placeFilter : ${placeFilter}\n" +
                    "placeMain : ${placeMain}\n" +
                    "postBlog : ${parentManager.findFragmentByTag("post_blog_fragment")}\n" +
                    "setting : ${parentManager.findFragmentByTag("setting_fragment")}")

            placeFilter?.let {pft.remove(it)}

            pft.commitAllowingStateLoss()
        }

        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }
}