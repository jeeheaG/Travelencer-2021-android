package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.travelencer_android_2021.databinding.FragmentPostBlogBinding

class PostBlogFragment : Fragment() {
    private var mBinding : FragmentPostBlogBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentPostBlogBinding.inflate(inflater, container, false)
        mBinding = binding
        return  mBinding?.root
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}