package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedPhototAdapter
import com.example.travelencer_android_2021.model.ModelFeedPhoto

// 게시물 - 사진 탭
class PostPhotoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_feed_photo, container, false)

        val rcFeedPhoto = view.findViewById<RecyclerView>(R.id.rcFeedPhoto)

        // 격자 레이아웃 생성
        rcFeedPhoto.layoutManager = GridLayoutManager(activity, 3)
        // 어댑터 달기
        val feedPhototAdapter = FeedPhototAdapter()
        rcFeedPhoto.adapter = feedPhototAdapter

        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202108/14620636_677435_2257.jpeg","1"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202103/868032_602354_475.jpeg","2"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://cdn.topstarnews.net/news/photo/202107/14611980_664676_4814.jpeg","3"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://san.chosun.com/site/data/img_dir/2021/07/26/2021072602015_1.jpg","4"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://flexible.img.hani.co.kr/flexible/normal/970/647/imgdb/original/2021/0511/8816206599991581.JPG","5"))
        feedPhototAdapter.items.add(ModelFeedPhoto("http://www.kyeongin.com/mnt/file/202011/2020111501000547600028341.jpg","6"))

        return view
    }
}