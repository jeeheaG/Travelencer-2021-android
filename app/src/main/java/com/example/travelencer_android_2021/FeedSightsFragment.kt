package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedSightsAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedSightsBinding
import com.example.travelencer_android_2021.model.ModelCourseSpot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "mmm"  //추가

// 여행 피드 - 관광지 탭
class FeedSightsFragment(val keyword : String) : Fragment() {
    private var _binding : FragmentFeedSightsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedSightsBinding.inflate(inflater, container, false)

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        binding.rcFeedSights.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedSightsAdapter = FeedSightsAdapter()
        binding.rcFeedSights.adapter = feedSightsAdapter
        // divider 추가
        binding.rcFeedSights.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        //추가
        var db = Firebase.firestore
        db.collection("postPlaceT")
            .whereEqualTo("placeCategory", "1") //plcCategory가 1(관광지)인
            .get()//모든 다큐먼트를 가져와라.
            .addOnSuccessListener { result ->
                for (document in result) {
                    //장소명, 주소 띄우기 - plcName, plcAddress
                    val map = document.data as HashMap<String, Any>
                    // content에 keyword 들어가는지 확인
                    val content1 : String = map["placeName"] as String
                    val content2 : String = map["placeLoc"] as String
                    // keyword 들어가면 feedSightsAdapter에 추가
                    if (content1.contains(keyword) || content2.contains(keyword)) {
                        val placeName : String = map["placeName"] as String
                        val placeLoc : String = map["placeLoc"] as String

                        if(!placeName.isEmpty()) {
                            if (!placeLoc.isEmpty()) {
                                feedSightsAdapter.items.add(ModelCourseSpot(placeName, placeLoc))
                                feedSightsAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                    else continue
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "FeedSightsFragment Error getting documents: ", exception)
            }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}