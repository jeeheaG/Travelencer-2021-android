package com.example.travelencer_android_2021

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedAdapter
import com.example.travelencer_android_2021.adapter.PostBlogAdapter
import com.example.travelencer_android_2021.databinding.FragmentPostBlogBinding
import com.example.travelencer_android_2021.model.ModelPostBlog
import com.example.travelencer_android_2021.model.ModelPostBlogPhoto
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_post_blog.view.*

//뷰바인딩 사용

class PostBlogFragment : Fragment() {
    private var _binding: FragmentPostBlogBinding? = null
    private val binding get() = _binding!!

    private val tabElement = arrayListOf("사진", "코스", "맛집", "관광지")

    private var uid = -1    // uid 값

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPostBlogBinding.inflate(inflater, container, false)
        val view = binding.root

        // uid 불러오기
        uid = activity?.getSharedPreferences("uid", Context.MODE_PRIVATE)!!.getInt("uid", -1)

        val photoList = arrayListOf(
                ModelPostBlogPhoto(R.drawable.dummy_haewoojae),
                ModelPostBlogPhoto(R.drawable.dummy_haewoojae),
                ModelPostBlogPhoto(R.drawable.dummy_hwasung),
                ModelPostBlogPhoto(R.drawable.dummy_haewoojae),
                ModelPostBlogPhoto(R.drawable.dummy_hwasung),
                ModelPostBlogPhoto(R.drawable.dummy_hwasung),
                ModelPostBlogPhoto(R.drawable.dummy_haewoojae),
                ModelPostBlogPhoto(R.drawable.dummy_hwasung),
                ModelPostBlogPhoto(R.drawable.dummy_haewoojae),
                ModelPostBlogPhoto(R.drawable.dummy_hwasung)
        )

        val postList = arrayListOf(
                ModelPostBlog("날 좋은 날 화성 나들이", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("해우재해우재", "2020.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("수원시 탐방", "2021.03.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("용인 옆 수원", "2021.03.40", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("라랄라라라라랄", "2021.01.20", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("수원 왕갈비", "2020.02.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("통닭 여행", "2020.05.14", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("화성 산책", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("용인시 수원시 열기구 체험 나나나나나", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("날 좋은 날 화성 나들이", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("날 좋은 날 화성 나들이", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("날 좋은 날 화성 나들이", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("날 좋은 날 화성 나들이", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList),
                ModelPostBlog("날 좋은 날 화성 나들이", "2021.06.10", R.drawable.ic_location_yellow, "화성행궁","경기도 수원시","오늘은 수원화성에 갔다. 수원 화성의 수원의 대표적인 관광지로 자리 잡고 있는 어쩌구는 이렇게 막 길게 마구잡이로 써도 잘 잘려야 한다.", photoList)
        )

        // 프로필 먼저 보이기
        binding.postViewPager.visibility = View.INVISIBLE
        binding.rvPostBlogPostList.visibility = View.VISIBLE


        binding.rvPostBlogPostList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvPostBlogPostList.setHasFixedSize(true)

        binding.rvPostBlogPostList.adapter = activity?.let { PostBlogAdapter(postList, it) }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}