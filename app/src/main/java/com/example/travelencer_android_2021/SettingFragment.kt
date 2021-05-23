package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

// 설정 프레그먼트
class SettingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {

        }

        // <서비스 이용 약관> 텍스트뷰 클릭
        view.tvTOS.setOnClickListener {
            // TOSActivity로 이동하기
            var intent = Intent(activity, TOSActivity::class.java)
            startActivity(intent)
        }

        // <개인정보 보호정책> 텍스트뷰 클릭
        view.tvPP.setOnClickListener {
            // PPActivity로 이동하기
            var intent = Intent(activity, PPActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
    }
}