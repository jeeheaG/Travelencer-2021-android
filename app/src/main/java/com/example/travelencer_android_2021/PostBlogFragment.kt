package com.example.travelencer_android_2021

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostBlogAdapter
import com.example.travelencer_android_2021.databinding.FragmentPostBlogBinding
import com.example.travelencer_android_2021.model.ModelPostBlog
import com.example.travelencer_android_2021.model.ModelPostBlogPhoto

//뷰바인딩 사용

class PostBlogFragment : Fragment() {
    private var _binding: FragmentPostBlogBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPostBlogBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.ivPostBlogProfile.background = ShapeDrawable(OvalShape())
        binding.ivPostBlogProfile.clipToOutline = true //안드로이드 버전 5(롤리팝)이상에서만 적용

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

        binding.rvPostBlogPostList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvPostBlogPostList.setHasFixedSize(true)

        binding.rvPostBlogPostList.adapter = activity?.let { PostBlogAdapter(postList) }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}