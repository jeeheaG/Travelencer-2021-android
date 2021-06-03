package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.travelencer_android_2021.model.ModelPlaceMain

class PlaceMainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_place_main, container, false) //setContentView행위를 프래그먼트버전으로 한 것

        val placeList = arrayListOf(
            //더미 데이터 목록
            ModelPlaceMain(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시 ", "")
        )

        return view
    }

}