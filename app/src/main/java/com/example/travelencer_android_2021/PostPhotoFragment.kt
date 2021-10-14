package com.example.travelencer_android_2021

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedPhototAdapter
import com.example.travelencer_android_2021.model.ModelFeedPhoto
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

private const val TAG = "mmm"

// 게시물 - 사진 탭
class PostPhotoFragment : Fragment() {
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef : StorageReference
    lateinit var feedPhototAdapter : FeedPhototAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_feed_photo, container, false)
        storage = Firebase.storage
        storageRef = storage.reference

        val rcFeedPhoto = view.findViewById<RecyclerView>(R.id.rcFeedPhoto)

        // 격자 레이아웃 생성
        rcFeedPhoto.layoutManager = GridLayoutManager(activity, 3)
        // 어댑터 달기
        feedPhototAdapter = FeedPhototAdapter()
        rcFeedPhoto.adapter = feedPhototAdapter

        // 사용자 정보 받아와서 설정하기
        val uid : String = (Firebase.auth.uid ?: activity?.getSharedPreferences("uid", Context.MODE_PRIVATE)!!.getString("uid", "-1")) as String
        if (uid != "-1") {
            getPhoto(uid)
        }

        return view
    }

    // 사용자 게시글의 사진 가져오기
    private fun getPhoto(uid : String) {
        val db = Firebase.firestore
        db.collection("postPhotoT")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        // postId에 uid 포함되는지 확인
                        val postId : String = map["postId"] as String
                        // postId 들어가면 feedPhototAdapter애 추가
                        if (postId.contains(uid)) {
                            val postPhoto : String = map["postPhoto"] as String

                            // 사진 uri 가져오기
                            storageRef.child("post/$postPhoto").downloadUrl
                                    .addOnSuccessListener { uri ->
                                        feedPhototAdapter.items.add(ModelFeedPhoto(uri.toString(), postId))
                                        feedPhototAdapter.notifyDataSetChanged()
                                    }.addOnFailureListener { exception ->
                                        Log.d(TAG, "PostPhotoFragment1 Error getting documents: ", exception)
                                    }
                        }
                        else continue
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "PostPhotoFragment2 Error getting documents: ", exception)
                }
    }
}