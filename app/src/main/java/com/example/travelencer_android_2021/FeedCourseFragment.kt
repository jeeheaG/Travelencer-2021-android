package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedCourseAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedCourseBinding
import com.example.travelencer_android_2021.model.ModelFeedPhoto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "mmm"

// 여행 피드 - 코스 탭
class FeedCourseFragment(val keyword : String) : Fragment() {
    private var _binding : FragmentFeedCourseBinding? = null
    private val binding get() = _binding!!

    lateinit var feedCourseAdapter : FeedCourseAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedCourseBinding.inflate(inflater, container, false)

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(activity)
        binding.rcFeedCourse.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        feedCourseAdapter = activity?.let { FeedCourseAdapter(it) }!!
        binding.rcFeedCourse.adapter = feedCourseAdapter
        // divider 추가
        binding.rcFeedCourse.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // content 에 keyword 들어가는 게시글 중에서 코스 정보 가져오기
        // 1, content 에 keyword 들어가는 게시글 찾기
        val db = Firebase.firestore
        db.collection("postT")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        // content에 keyword 들어가는지 확인
                        val content : String = map["content"] as String
                        // 2, keyword 들어가면 해당 postId의 코스 데이터 가져오기
                        if (content.contains(keyword)) {
                            // 해당 postId의 코스 데이터 가져오기(시간 오름차순으로)
                            val postId: String = map["postId"] as String
                            // uid도 가져오기
                            val uid : String = map["uid"] as String
                            getCourse(postId, uid)
                        }
                        else continue
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "FeedPhotoFragment2 Error getting documents: ", exception)
                }

        return binding.root
    }

    // 해당 postId의 코스 데이터 가져오기(시간 오름차순으로)
    private fun getCourse(postId : String, uid : String) {
        val db = Firebase.firestore
        val courseNameList = ArrayList<String>()

        db.collection("postCourseT")
                .whereEqualTo("postId", postId)
//                .orderBy("sequence") // 이건 색인 작업이 필요한데 어케하는지 모르겟;; 나중에 추가
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
                    Log.w(TAG, "getCourse Error getting documents: ", exception)
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}