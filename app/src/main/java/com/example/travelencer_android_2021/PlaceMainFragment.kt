package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PlaceMainAdapter
import com.example.travelencer_android_2021.databinding.FragmentPlaceMainBinding
import com.example.travelencer_android_2021.model.ModelCasePlaceCard
//뷰바인딩 사용

class PlaceMainFragment : Fragment() {
    private var _binding: FragmentPlaceMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val view = inflater.inflate(R.layout.fragment_place_main, container, false) //setContentView행위를 프래그먼트버전으로 한 것
        _binding = FragmentPlaceMainBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnPlaceMainAddPlace.setOnClickListener {
            val intent = Intent(activity, AddPlaceActivity::class.java)
            intent.putExtra("from", "placeMain")
            startActivity(intent)
        }

        //더미 데이터 목록
        val placeList = arrayListOf(
            ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_hwasung, "수원 화성", "경기도 수원시", "화성는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "무슨산", "경기도 수원시", "가가가가가 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "관광지", "경기도 수원시", "관광지는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "뭐라고", "경기도 수원시", "뭐는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "광교산", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "왕갈비", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "무슨호수", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "저기공원", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "호수공원", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "여러가지", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "라라랄", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "더미더미", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "왕릉", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "마지막", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ")
                )

        binding.rvPlaceMain.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false) //프래그먼트이므로 this대신 activity
        binding.rvPlaceMain.setHasFixedSize(true)

        binding.rvPlaceMain.adapter = activity?.let { PlaceMainAdapter(placeList, it) }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}