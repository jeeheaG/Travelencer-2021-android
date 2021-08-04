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
import com.example.travelencer_android_2021.model.FetxhXML
import kotlinx.android.synthetic.main.fragment_place_filter.*

//뷰바인딩 사용

private const val TAG_PLACE_FILTER = "place_filter_fragment"
private const val TAG_PLACE_MAIN = "place_main_fragment"

private const val SP_PLACE_FILTERED: String = "placeFiltered"

class PlaceFilterFragment : Fragment() {
    private var _binding: FragmentPlaceFilterBinding? = null
    private val binding get() = _binding!!

    lateinit var spinner : Array<Spinner>   // 스피너 배열

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaceFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        //스피너 입력받은 값 가져오기
        spinner = arrayOf(view.findViewById(R.id.spinPlaceLarge), view.findViewById(R.id.spinPlaceSmall))

        // 스피너 설정
        FetxhXML(spinner, context as NaviActivity).fetchXML("http://api.visitkorea.or.kr/upload/manual/sample/areaCode_sample1.xml", 0)

        //이 화면으로 오면 필터 설정이 해제됨
        val pref = activity?.getSharedPreferences("pref", 0)
        val edit = pref?.edit()
        edit?.putBoolean(SP_PLACE_FILTERED, false)
        edit?.apply()
        Log.d("로그 -placeFiltered-1--", "placeFiltered : ${pref?.getBoolean(SP_PLACE_FILTERED, true)}")


        // 검색 버튼 눌렀을 때
        // sharedPreferences 의 SF_PLACE_FILTERED값 true로 변경
        // preantFragmentManager에 접근해서 현재 placeFilter 프래그먼트 remove, placeMain 프래그먼트 add

        binding.btnSearch.setOnClickListener {
            Toast.makeText(activity, "btnSearch onClicked", Toast.LENGTH_SHORT).show()

            edit?.putBoolean(SP_PLACE_FILTERED, true)
            edit?.apply()
//            Log.d("로그 -filtered-2--", "placeFiltered : ${pref?.getBoolean(SP_PLACE_FILTERED, false)}")

            val parentManager: FragmentManager = parentFragmentManager
            val pft: FragmentTransaction = parentManager.beginTransaction()

            // 지역명 전달하기
            val placeMainFrag = PlaceMainFragment()
            val bundle = Bundle()

            try {

                val keyword = binding.etSearchKeyword.text.toString()
                val area1: String? = if(spinner[0].selectedItem!=null) spinner[0].selectedItem.toString() else "선택안함"
                val area2: String? = if(spinner[1].selectedItem!=null) spinner[1].selectedItem.toString() else "선택안함"

                bundle.putString("keyword", keyword)    //검색어
                bundle.putString("area1", area1)    // 지역명
                bundle.putString("area2", area2)    // 시군구명

                placeMainFrag.arguments = bundle
                pft.add(R.id.flContainer, placeMainFrag, TAG_PLACE_MAIN)

                val placeFilter = parentManager.findFragmentByTag(TAG_PLACE_FILTER)
//            val placeMain = parentManager.findFragmentByTag(TAG_PLACE_MAIN)
//
//            Log.d("로그 at filter---", "parentManager에서 tag로 찾은 것들 값 확인. NULL인 건 반영x \n" +
//                    "feed : ${parentManager.findFragmentByTag("feed_fragment")}\n" +
//                    "placeFilter : ${placeFilter}\n" +
//                    "placeMain : ${placeMain}\n" +
//                    "postBlog : ${parentManager.findFragmentByTag("post_blog_fragment")}\n" +
//                    "setting : ${parentManager.findFragmentByTag("setting_fragment")}")

                placeFilter?.let {pft.remove(it)}

                pft.commitAllowingStateLoss()
            }
            catch (e : NullPointerException) {
                Toast.makeText(context, "지역을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}