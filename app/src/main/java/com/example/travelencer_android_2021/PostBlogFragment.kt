package com.example.travelencer_android_2021

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.adapter.FeedAdapter
import com.example.travelencer_android_2021.adapter.PostBlogAdapter
import com.example.travelencer_android_2021.api.URLtoBitmapTask
import com.example.travelencer_android_2021.databinding.FragmentPostBlogBinding
import com.example.travelencer_android_2021.model.ModelPostBlog
import com.example.travelencer_android_2021.model.ModelPostBlogPhoto
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_post_blog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL

//뷰바인딩 사용

class PostBlogFragment : Fragment() {
    private var _binding: FragmentPostBlogBinding? = null
    private val binding get() = _binding!!
    //var ModelPostBlogPhoto = ModelPostBlogPhoto()
    var auth = FirebaseAuth.getInstance()
    var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var storage : FirebaseStorage? = FirebaseStorage.getInstance()
    var photoList = arrayListOf<ModelPostBlogPhoto>()
    var postList = arrayListOf<ModelPostBlog>()
    private val tabElement = arrayListOf("사진", "코스", "맛집", "관광지")
    private lateinit var postAdapter : PostBlogAdapter
    private lateinit var storageRef : StorageReference

    lateinit var naviActivity: NaviActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 2. Context를 액티비티로 형변환해서 할당
        naviActivity = context as NaviActivity
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPostBlogBinding.inflate(inflater, container, false)
        val view = binding.root
        postAdapter = PostBlogAdapter(postList, activity?.applicationContext!!)
        storageRef = storage!!.reference


//        showLoadingDialog(naviActivity)
        // 로딩 시작
        val dialog = LoadingDialog(naviActivity)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
        }

        firestore.collection("userT").document(auth.currentUser!!.uid).get()
                .addOnSuccessListener { doc->
                    binding.tvPostBlogNickname.text = doc.data?.get("name").toString()
                    val info = doc.data?.get("info")
                    if(info != null){
                        binding.tvPostBlogBio.text = info.toString()
                    }
                }
        // 프로필 이미지 다운로드해서 가져오기
        val storageRef = storage?.reference?.child("user")
                ?.child("proPic_${auth.currentUser!!.uid}")
        storageRef?.downloadUrl
                ?.addOnSuccessListener { uri ->
                    Glide.with(activity?.applicationContext!!)
                            .load(uri)
                            .error(R.drawable.ic_user_gray)                  // 오류 시 이미지
                            .apply(RequestOptions().centerCrop())
                            .into(binding.ivPostBlogProfile)
                }
        //TODO 어뎁터 선언후 밑에서 notify 어쩌구 해주기

        //uid별 작성 게시글 불러오기
        var model : ModelPostBlog
        firestore.collection("postT").whereEqualTo("uid",auth.currentUser!!.uid).get()
                .addOnSuccessListener { docs->
                    for(doc in docs){
                        val uid = auth.currentUser!!.uid // 일단은 현재 유저만 보여줌?
                        val title = doc.data.get("title").toString()
                        val temp = doc.data.get("updateDate").toString()
                        val updateDate = temp.slice(IntRange(0,3))+"."+
                                temp.slice(IntRange(4,5))+"."+
                                temp.slice(IntRange(6,7))
                        val icon = R.drawable.ic_location_yellow
                        val writing = doc.data.get("content").toString()
                        val postId = doc.data.get("postId").toString()
                        var placeName = ""
                        var location = ""
                        firestore.collection("postPlaceT").whereEqualTo("postId",postId).get()
                                .addOnSuccessListener { docs2->
                                    for(doc2 in docs2){
                                        placeName = doc2.data.get("placeName").toString()
                                        location = doc2.data.get("placeLoc").toString()
                                        break
                                    }
                                    // 사진 정보 가져오기 + 설정
                                    photoList = getPlacePhoto(postId)
                                    model = ModelPostBlog(postId, uid, title, updateDate, icon, placeName, location, writing, photoList)
                                    postList.add(model)
                                    postAdapter.notifyDataSetChanged()

                                    //TODO: 사진 안뜨는 거 고치기
                                }

                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000)
                        dialog.dismiss()
                    }
                }

        // 프로필 먼저 보이기
        binding.postViewPager.visibility = View.INVISIBLE
        binding.rvPostBlogPostList.visibility = View.VISIBLE


        binding.rvPostBlogPostList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvPostBlogPostList.setHasFixedSize(true)

        binding.rvPostBlogPostList.adapter = postAdapter
        // divider 추가
        binding.rvPostBlogPostList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.ivPostBlogProfile.background = ShapeDrawable(OvalShape())
        binding.ivPostBlogProfile.clipToOutline = true //안드로이드 버전 5(롤리팝)이상에서만 적용

        binding.btnBookmarkList.setOnClickListener {
            val intent = Intent(activity, BookmarkActivity::class.java)
            startActivity(intent)
        }

        binding.btnPostBlogWrite.setOnClickListener {
            val intent = Intent(activity, PostWriteActivity::class.java)
            startActivity(intent)
        }

        // 게시쿨 어댑터 생성
        val feedAdapter = FeedAdapter(this@PostBlogFragment)
        // 프레그먼트, 탭 타이틀 넣기
        feedAdapter.addFragment(PostPhotoFragment())
        feedAdapter.addFragment(PostCourseFragment())
        feedAdapter.addFragment(PostFoodFragment())
        feedAdapter.addFragment(PostSightsFragment())
        binding.postViewPager.adapter = feedAdapter
        // 탭레이아웃에 뷰 페이저 달기
        TabLayoutMediator(binding.postTabLayout, binding.postViewPager) { tab, position ->
            tab.text = tabElement[position]
        }.attach()

        // 토글 버튼
        val btnToggle = binding.btnPostBlogToggle
        btnToggle.setOnClickListener {
            // 게시물
            if (btnToggle.text == "프로필") {
                btnToggle.text = "게시물"
                btnToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_home_samll, 0)
                binding.postViewPager.visibility = View.VISIBLE
                binding.rvPostBlogPostList.visibility = View.INVISIBLE

                binding.postTabLayout.visibility = View.VISIBLE
            }
            // 프로필
            else {
                btnToggle.text = "프로필"
                btnToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_squares_small, 0)
                binding.postViewPager.visibility = View.INVISIBLE
                binding.rvPostBlogPostList.visibility = View.VISIBLE

                binding.postTabLayout.visibility = View.GONE
            }
        }

        return view
    }

    // 사진 정보 가져오기 : 사진 이름 반환
    private fun getPlacePhoto(postId : String) : ArrayList<ModelPostBlogPhoto> {
        val photoList = ArrayList<ModelPostBlogPhoto>()

        // 사진 이름 가져오기
        val db = Firebase.firestore
        db.collection("postPhotoT")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val map = document.data as HashMap<String, Any>
                        val postPhoto : String = map["postPhoto"] as String
                        storageRef.child("post/$postPhoto").downloadUrl
                                .addOnSuccessListener { url ->
                                    Log.d("로그--", "uri ${url}")
//                                    val bitmapTask = URLtoBitmapTask().apply {
//                                        taskUrl = URL("${url}")
//                                    }
//                                    var bitmap: Bitmap = bitmapTask.execute().get()
//                                    photoList.add(ModelPostBlogPhoto(bitmap))
                                    photoList.add(ModelPostBlogPhoto(url))
                                    postAdapter.notifyDataSetChanged()
                                }
                    }
                }.apply { return photoList }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showLoadingDialog(context: Context) {
        val dialog = LoadingDialog(context)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(3000)
            dialog.dismiss()
        }
    }

}
