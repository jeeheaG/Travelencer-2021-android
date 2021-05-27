package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.travelencer_android_2021.databinding.FragmentPlaceMainBinding
import com.example.travelencer_android_2021.model.ModelPlaceMain

//
class PlaceMainFragment : Fragment() {
    private var mBinding : FragmentPlaceMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //이거 프래그먼트는 아닌 것 같음 setContentView(R.layout.fragment_place_main)

        val placeList = arrayListOf(
                //더미 데이터 목록
                ModelPlaceMain(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시 ", "")
        )
    }


    //뷰 바인딩
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentPlaceMainBinding.inflate(inflater, container, false)
        mBinding = binding

        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}