package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.adapter.FeedPhototAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedPhotoBinding
import com.example.travelencer_android_2021.model.ModelFeedPhoto
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.list_item_feed_photo.view.*

private const val TAG = "mmm"

// 여행 피드 - 사진 탭
class FeedPhotoFragment(val keyword : String) : Fragment() {
    private var _binding : FragmentFeedPhotoBinding? = null
    private val binding get() = _binding!!

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef : StorageReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFeedPhotoBinding.inflate(inflater, container, false)

        storage = Firebase.storage
        storageRef = storage.reference

        // 격자 레이아웃 생성
        binding.rcFeedPhoto.layoutManager = GridLayoutManager(activity, 3)
        // 어댑터 달기
        val feedPhototAdapter = FeedPhototAdapter()
        binding.rcFeedPhoto.adapter = feedPhototAdapter

        // 내용에 검색어 들어가는 게시글 중에서 사진, postId 가져오기
        val db = Firebase.firestore
        db.collection("postPhotoT")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        // content에 keyword 들어가는지 확인
                        val content : String = map["content"] as String
                        // keyword 들어가면 feedPhototAdapter에 추가
                        if (content.contains(keyword)) {
                            val postId : String = map["postId"] as String
                            val postPhoto : String = map["postPhoto"] as String

                            // 사진 uri 가져오기
                            storageRef.child("post/$postPhoto").downloadUrl
                                    .addOnSuccessListener { uri ->
                                        feedPhototAdapter.items.add(ModelFeedPhoto(uri.toString(), postId))
                                        feedPhototAdapter.notifyDataSetChanged()
                                    }.addOnFailureListener { exception ->
                                        Log.d(TAG, "FeedPhotoFragment1 Error getting documents: ", exception)
                                    }
                        }
                        else continue
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "FeedPhotoFragment2 Error getting documents: ", exception)
                }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}