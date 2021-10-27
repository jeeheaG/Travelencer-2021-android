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
import com.example.travelencer_android_2021.adapter.FeedCourseAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "mmm"

// 게시물 - 코스 탭
class PostCourseFragment : Fragment() {
    lateinit var feedCourseAdapter : FeedCourseAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_course, container, false)

        val rcFeedCourse = view.findViewById<RecyclerView>(R.id.rcFeedCourse)
        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        rcFeedCourse.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        feedCourseAdapter = activity?.let { FeedCourseAdapter(it) }!!
        rcFeedCourse.adapter = feedCourseAdapter
        // divider 추가
        rcFeedCourse.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // 사용자 정보 받아와서 설정하기
        val uid : String = (Firebase.auth.uid ?: activity?.getSharedPreferences("uid", Context.MODE_PRIVATE)!!.getString("uid", "-1")) as String
        if (uid != "-1") {
            // postId 에 uid 들어가는 게시글 중에서 코스 정보 가져오기
            // 1, postId 에 uid 들어가는 게시글 찾기
            val db = Firebase.firestore
            db.collection("postT")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val map = document.data as HashMap<String, Any>
                            // postId uid 들어가는지 확인
                            val postId : String = map["postId"] as String
                            // 2, keyword 들어가면 해당 postId의 코스 데이터 가져오기
                            if (postId.contains(uid)) {
                                getCourse(postId, uid)
                            }
                            else continue
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "PostCourseFragment1 Error getting documents: ", exception)
                    }
        }

        return view
    }

    // 해당 postId의 코스 데이터 가져오기(시간 오름차순으로)
    private fun getCourse(postId : String, uid : String) {
        val db = Firebase.firestore
        val courseNameList = ArrayList<String>()

        db.collection("postCourseT")
                .whereEqualTo("postId", postId)
                .orderBy("sequence")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        val coursePlaceName: String = map["coursePlaceName"] as String
                        courseNameList.add(coursePlaceName)
                    }
                    // 코스 데이터가 있다면 추가~~
                    if (!courseNameList.isEmpty()) {
                        feedCourseAdapter.postIds.add(postId)
                        feedCourseAdapter.placeNames.add(courseNameList)
                        feedCourseAdapter.uids.add(uid)
                        feedCourseAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "PostCourseFragment2 getCourse Error getting documents: ", exception)
                }
    }

}