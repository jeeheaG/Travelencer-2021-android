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

        feedPhototAdapter.items.add(ModelFeedPhoto("http://img.etoday.co.kr/pto_db/2020/11/20201124102548_1544383_710_340.jpg", "2"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://pbs.twimg.com/profile_images/1107050488476819457/zn7BJ9Q6.jpg", "3"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbWtUpuVhoPd4eSzs1Q6eXT7SkrtQ6eo93Dw&usqp=CAU", "4"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbWtUpuVhoPd4eSzs1Q6eXT7SkrtQ6eo93Dw&usqp=CAU", "5"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbWtUpuVhoPd4eSzs1Q6eXT7SkrtQ6eo93Dw&usqp=CAU", "6"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://yt3.ggpht.com/ytc/AAUvwniH02-F6A92emPJzoDG633EB7ss_Q6kvP7Vo7sFlw=s900-c-k-c0x00ffffff-no-r", "7"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://yt3.ggpht.com/ytc/AAUvwniH02-F6A92emPJzoDG633EB7ss_Q6kvP7Vo7sFlw=s900-c-k-c0x00ffffff-no-r", "8"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://yt3.ggpht.com/ytc/AAUvwniH02-F6A92emPJzoDG633EB7ss_Q6kvP7Vo7sFlw=s900-c-k-c0x00ffffff-no-r", "9"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://yt3.ggpht.com/ytc/AAUvwniH02-F6A92emPJzoDG633EB7ss_Q6kvP7Vo7sFlw=s900-c-k-c0x00ffffff-no-r", "10"))
        feedPhototAdapter.items.add(ModelFeedPhoto("https://yt3.ggpht.com/ytc/AAUvwniH02-F6A92emPJzoDG633EB7ss_Q6kvP7Vo7sFlw=s900-c-k-c0x00ffffff-no-rj", "11"))

        return view
    }
}