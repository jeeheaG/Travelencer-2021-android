package com.example.travelencer_android_2021

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedSightsAdapter
import com.example.travelencer_android_2021.model.ModelCourseSpot
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// binding 인 됨...
// 게시물 - 관광지 탭
class PostSightsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_feed_sights, container, false)

        val rcFeedSights = view.findViewById<RecyclerView>(R.id.rcFeedSights)
        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        rcFeedSights.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val feedSightsAdapter = FeedSightsAdapter()
        rcFeedSights.adapter = feedSightsAdapter
        // divider 추가
        rcFeedSights.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val uid : String = (Firebase.auth.uid ?: activity?.getSharedPreferences("uid", Context.MODE_PRIVATE)!!.getString("uid", "-1")) as String

        val db = Firebase.firestore
        db.collection("postPlaceT")
                .whereEqualTo("placeCategory", "1") // 관굉지 : 1
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        // postId에 uid 들어가면 feedSightsAdapter 추가
                        val postId : String = map["postId"] as String
                        if (postId.contains(uid)) {
                            val placeName : String = map["placeName"] as String
                            val placeLoc : String = map["placeLoc"] as String

                            if(!placeName.isEmpty() && !placeLoc.isEmpty()) {
                                feedSightsAdapter.items.add(ModelCourseSpot(placeName, placeLoc))
                                feedSightsAdapter.notifyDataSetChanged()
                            }
                        }
                        else continue
                    }
                }

        return view
    }

}