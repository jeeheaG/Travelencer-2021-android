package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PNCAdapter
import com.example.travelencer_android_2021.databinding.ActivityPNCBinding
import com.example.travelencer_android_2021.model.ModelPNC

//뷰바인딩 사용

class PNCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPNCBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPNCBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pncList = arrayListOf(
                ModelPNC(R.drawable.ic_thumb_up, "수원화성의 대표적인 관광지"),
                ModelPNC(R.drawable.ic_thumb_down, "모기가 너무 많음"),
                ModelPNC(R.drawable.ic_thumb_up, "맛집 천지"),
                ModelPNC(R.drawable.ic_thumb_up, "맛집 천지"),
                ModelPNC(R.drawable.ic_thumb_down, "모기가 너무 많음"),
                ModelPNC(R.drawable.ic_thumb_up, "수원화성의 대표적인 관광지")
        )

        binding.rvPNCList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPNCList.setHasFixedSize(true)

        binding.rvPNCList.adapter = PNCAdapter(pncList)

    }
}