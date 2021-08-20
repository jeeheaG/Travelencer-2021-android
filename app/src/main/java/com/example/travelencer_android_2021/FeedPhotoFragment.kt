package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelencer_android_2021.adapter.FeedPhototAdapter
import com.example.travelencer_android_2021.databinding.FragmentFeedPhotoBinding
import com.example.travelencer_android_2021.model.ModelFeedPhoto

// 여행 피드 - 사진 탭
class FeedPhotoFragment : Fragment() {
    private var _binding : FragmentFeedPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFeedPhotoBinding.inflate(inflater, container, false)

        // 격자 레이아웃 생성
        binding.rcFeedPhoto.layoutManager = GridLayoutManager(activity, 3)
        // 어댑터 달기
        val feedPhototAdapter = FeedPhototAdapter()
        binding.rcFeedPhoto.adapter = feedPhototAdapter

        feedPhototAdapter.items.add(ModelFeedPhoto("https://img.hankyung.com/photo/202107/AA.26990398.1.jpg","1"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202105/7358283_633225_2052.jpeg","2"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202108/14620636_677435_2257.jpeg","3"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202103/868032_602354_475.jpeg","4"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202107/14611980_664676_4814.jpeg","5"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://san.chosun.com/site/data/img_dir/2021/07/26/2021072602015_1.jpg","6"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://flexible.img.hani.co.kr/flexible/normal/970/647/imgdb/original/2021/0511/8816206599991581.JPG","7"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://www.kyeongin.com/mnt/file/202011/2020111501000547600028341.jpg","8"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://flexible.img.hani.co.kr/flexible/normal/970/647/imgdb/original/2021/0511/7016206599995927.JPG","9"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202104/918165_612232_3214.jpeg","10"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://www.polinews.co.kr/data/photos/20190519/art_15574545894502_edae35.jpg","11"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://flexible.img.hani.co.kr/flexible/normal/970/647/imgdb/original/2021/0511/8916206601916264.JPG","12"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://www.kyeongin.com/mnt/file/202009/2020091301000553600027091.jpg","13"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://flexible.img.hani.co.kr/flexible/normal/970/647/imgdb/original/2021/0511/5016206599994184.JPG","14"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://www.kyeongin.com/mnt/file/202011/2020111501000547600028342.jpg","15"))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}