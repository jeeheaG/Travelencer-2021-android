package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedFoodAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedFoodBinding
import com.example.travelencer_android_2021.model.ModelCourseSpot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "mmm"  //추가

// 여행 피드 - 맛집 탭
class FeedFoodFragment(val keyword : String) : Fragment() {

    private var _binding : FragmentFeedFoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedFoodBinding.inflate(inflater, container, false)

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        binding.rcFeedFood.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedFoodAdapter = FeedFoodAdapter()
        binding.rcFeedFood.adapter = feedFoodAdapter
        // divider 추가
        binding.rcFeedFood.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        //추가
        var db = Firebase.firestore
        db.collection("postPlaceT")
            .whereEqualTo("placeCategory", "0") //plcCategory가 0(맛집)인
            .get()//모든 다큐먼트를 가져와라.
            .addOnSuccessListener { result ->
                for (document in result) {
                    //장소명, 주소 띄우기 - placeName, placeLoc
                    val map = document.data as HashMap<String, Any>
                    // placeName에 keyword 들어가는지 확인
                    val content : String = map["placeName"] as String
                    // keyword 들어가면 feedFoodAdapter에 추가
                    if (content.contains(keyword)) {
                        val placeName : String = map["placeName"] as String
                        val placeLoc : String = map["placeLoc"] as String
                        //음식점 데이터가 있다면
                        if(!placeName.isEmpty()) {
                            if (!placeLoc.isEmpty()) {
                                feedFoodAdapter.items.add(ModelCourseSpot(placeName, placeLoc))
                                feedFoodAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                    else continue
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "FeedFoodFragment Error getting documents: ", exception)
            }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}