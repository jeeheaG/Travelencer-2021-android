package com.example.travelencer_android_2021

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.adapter.PostDetailPhotoAdapter
import com.example.travelencer_android_2021.adapter.PostDetailPlaceAdapter
import com.example.travelencer_android_2021.course.CourseMaker
import com.example.travelencer_android_2021.databinding.ActivityPostDetailBinding
import com.example.travelencer_android_2021.model.ModelCasePhotoOnly
import com.example.travelencer_android_2021.model.ModelPostDetailPlace
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_feed_course_detail.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
private const val TAG = "mmm"


//뷰바인딩 사용

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding

    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        storage = Firebase.storage
        storageRef = storage.reference

        val postId = intent.getStringExtra("postId") ?: "오류"
        val uid = intent.getStringExtra("uid").toString()

        if (postId != "오류") {
            //코스 스팟 개수에 따른 코스 뷰 구성
            getCourse(postId)

            // 게시글 정보 가져오기
            getPost(postId)

            // 닉네임 설정
            setName(uid)
            // 프로필 설정
            setProPic(uid)

            // 사진 정보 가져오기
            val postPhotoList = getPlacePhoto(postId)
            Log.d(TAG, postPhotoList.toString())
            // 사진 uri 가져오기
            val postPhotoUri = getPhotoUri(postPhotoList)
            Log.d(TAG, postPhotoUri.toString())
        }

        firestore?.collection("userT").document(auth.currentUser!!.uid).get()
            ?.addOnSuccessListener { doc->
                binding.tvPostDetailNickname.text = doc?.data?.get("name").toString()
            }
        // 이미지 다운로드해서 가져오기
        var storageRef = storage?.reference?.child("user")
            ?.child("proPic_${auth.currentUser!!.uid}")
        storageRef?.downloadUrl
            ?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                    .load(uri)
                    .error(R.drawable.ic_user_gray)                  // 오류 시 이미지
                    .apply(RequestOptions().centerCrop())
                    .into(binding.ivPostDetailProfileImg)
            }
        //TODO: 기타 정보 불러오기
        val placeList = arrayListOf(
                ModelPostDetailPlace(R.drawable.ic_location_yellow, "해우재", "경기도 수원시"),
                ModelPostDetailPlace(R.drawable.ic_food, "삼겹구이", "경기도 용인시"),
                ModelPostDetailPlace(R.drawable.ic_location_yellow, "수원 화성", "경기도 수원시"),
                ModelPostDetailPlace(R.drawable.ic_location_yellow, "해우재", "경기도 수원시")
        )
        val dummyImageUrl = ""
        val photoList = arrayListOf(
                ModelCasePhotoOnly(dummyImageUrl),
                ModelCasePhotoOnly(dummyImageUrl),
                ModelCasePhotoOnly(dummyImageUrl),
                ModelCasePhotoOnly(dummyImageUrl)
        )

        binding.rvPostDetailPlaceList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPostDetailPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvPostDetailPlaceList.setHasFixedSize(true)
        binding.rvPostDetailPhotoList.setHasFixedSize(true)

        binding.rvPostDetailPlaceList.adapter = PostDetailPlaceAdapter(placeList)
        binding.rvPostDetailPhotoList.adapter = PostDetailPhotoAdapter(photoList, this)

        binding.ivPostDetailProfileImg.background = ShapeDrawable(OvalShape())
        binding.ivPostDetailProfileImg.clipToOutline = true //안드로이드 버전 5 롤리팝 이상에서만 적용

        // 코스 클릭하면 상세 코스 보이기
        binding.llPostDetailCourse.setOnClickListener {
            val intent = Intent(applicationContext, FeedCourseDetailActivity::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }

        //TODO : 현재 게시물 정보를 가지고 수정페이지로 이동하도록 만들기
        binding.btnPostDetailEdit.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }

    }

    // 코스 정보 가져오가
    private fun getCourse(postId : String) {
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
                        CourseMaker().makeCourse(courseNameList, binding.llPostDetailCourse, this)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "getCourse Error getting documents: ", exception)
                }
    }

    // 게시글 정보 가져오기
    private fun getPost(postId : String) {
        val db = Firebase.firestore
        db.collection("postT")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        val title : String = map["title"] as String
                        val startDate : String = map["startDate"] as String
                        val endDate : String = map["endDate"] as String
                        var updateDate : String = map["updateDate"] as String
                        val content : String = map["content"] as String
                        updateDate = updateDate.slice(IntRange(0,3))+"."+
                                updateDate.slice(IntRange(4,5))+"."+
                                updateDate.slice(IntRange(6,7))
                        // 제목
                        binding.tvPostDetailTitle.text = title
                        // 여행 일자
                        binding.tvPostDetailPeriod.text = "$startDate ~ $endDate"
                        // 작성일
                        binding.tvPostDetailPostDate.text = "작성일 $updateDate"
                        // 내용
                        binding.tvPostDetailWriting.text = content
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
                        binding.tvPostDetailNickname.text = name
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
                            .into(binding.ivPostDetailProfileImg)
                }
    }

    // 사진 정보 가져오기 : 사진 이름 반환
    private fun getPlacePhoto(postId : String) : ArrayList<String> {
        val postPhotoList = ArrayList<String>()

        Log.d(TAG, postId)

        // 사진 이름 가져오기
        val db = Firebase.firestore
        db.collection("postPhotoT")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        val postPhoto : String = map["postPhoto"] as String
                        Log.d(TAG + "하아", postPhoto)
                        postPhotoList.add(postPhoto)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "getPlacePhoto Error getting documents: ", exception)
                }.apply { return postPhotoList }
    }

    // 사진 uri 가져오기
    private fun getPhotoUri(postPhotoList : ArrayList<String>) : ArrayList<String> {
        val postPhotoUri = ArrayList<String>()

        for (postPhoto in postPhotoList) {
            storageRef.child("post/$postPhoto").downloadUrl
                    .addOnSuccessListener { uri ->
                        postPhotoUri.add(uri.toString())
                    }
        }
        return postPhotoUri
    }
}