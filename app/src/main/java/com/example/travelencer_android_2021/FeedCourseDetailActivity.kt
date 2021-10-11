package com.example.travelencer_android_2021

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.adapter.FeedCourseDetailAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_feed_course_detail.*

private const val TAG = "mmm"

class FeedCourseDetailActivity : AppCompatActivity() {
    lateinit var feedCourseDetailAdapter : FeedCourseDetailAdapter

    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_course_detail)

        storage = Firebase.storage
        storageRef = storage.reference

        // 코스 id
        val postId = intent.getStringExtra("postId").toString()
        // 닉네임 가져오기
        val uid = intent.getStringExtra("uid").toString()


        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(this@FeedCourseDetailActivity)
        rcFeedCourseDetail.layoutManager = layoutManager
        // 리사이클러뷰에 어댑터 달기
        feedCourseDetailAdapter = FeedCourseDetailAdapter()
        rcFeedCourseDetail.adapter = feedCourseDetailAdapter

        // 받아온 postId 로 코스 이름, 위치, 날짜 배열 얻어와서 설정
        getCourse(postId)

        // 닉네임 설정
        setName(uid)
        // 프로필 설정
        setProPic(uid)
        // 제목 설정
        setPostTitle(postId)

        // 닉네임, 프로필 사진 누르면 게시글로 아동
        tvCourseDetailNickname.setOnClickListener {
            val intent = Intent(this@FeedCourseDetailActivity, PostDetailActivity::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }
        imgCourseDetailProfileImg.setOnClickListener {
            tvCourseDetailNickname.callOnClick()
        }

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

        // 동그란 프로틸 사진
        imgCourseDetailProfileImg.background = ShapeDrawable(OvalShape())
        imgCourseDetailProfileImg.clipToOutline = true //안드로이드 버전 5 롤리팝 이상에서만 적용
    }

    // 해당 postId의 코스 데이터 가져오기(시간 오름차순으로)
    private fun getCourse(postId : String) {
        val db = Firebase.firestore
        val courseNameList = ArrayList<String>()
        val courseDateList = ArrayList<String>()

        db.collection("postCourseT")
                .whereEqualTo("postId", postId)
//                .orderBy("sequence") // 이건 색인 작업이 필요한데 어케하는지 모르겟;; 나중에 추가
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        val coursePlaceName: String = map["coursePlaceName"] as String
                        val courseDate: String = map["courseDate"] as String
                        courseNameList.add(coursePlaceName)
                        courseDateList.add(courseDate)
                    }
                    // 코스 데이터가 있다면 추가~~
                    if (!courseNameList.isEmpty()) {
                        feedCourseDetailAdapter.courseName = courseNameList
                        feedCourseDetailAdapter.courseDate = courseDateList
                        feedCourseDetailAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "getCourse Error getting documents: ", exception)
                }
    }

    // 닉네임 설정
    private fun setName(uid : String) {
        val db = Firebase.firestore
        val docRef = db.collection("userT").document(uid)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val map = document.data as HashMap<String, Any>
                        val name : String = map["name"] as String
                        tvCourseDetailNickname.text = name
                    }
                }
    }

    // 프로틸 설정
    private fun setProPic(uid : String) {
        storageRef.child("user/proPic_$uid").downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(applicationContext!!)
                            .load(uri)
                            .error(R.drawable.ic_user_gray)                  // 오류 시 이미지
                            .apply(RequestOptions().centerCrop())
                            .into(imgCourseDetailProfileImg)
                }
    }

    // 제목 설정
    private fun setPostTitle(postId : String) {
        val db = Firebase.firestore
        val docRef = db.collection("postT").document(postId)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val map = document.data as HashMap<String, Any>
                        val title : String = map["title"] as String
                        tvCourseDetailTitle.text = title
                    }
                }
    }
}