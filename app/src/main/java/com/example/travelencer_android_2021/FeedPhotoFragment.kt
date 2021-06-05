package com.example.travelencer_android_2021

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.FeedPhototAdapter
import com.example.travelencer_android_2021.model.ModelFeedPhoto
import kotlinx.android.synthetic.main.fragment_feed_photo.*

// 여행 피드 - 사진 탭
class FeedPhotoFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_feed_photo, container, false)

        var rcFeedPhoto = view.findViewById<RecyclerView>(R.id.rcFeedPhoto)

        rcFeedPhoto.layoutManager = GridLayoutManager(activity, 3)
        val feedPhototAdapter = FeedPhototAdapter()
        rcFeedPhoto.adapter = feedPhototAdapter

        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "1"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "2"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "3"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "4"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "5"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "6"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "7"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "8"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "9"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "10"))
        feedPhototAdapter.items.add(ModelFeedPhoto(R.drawable.mm2, "11"))

        return view
    }

    companion object {
    }
}