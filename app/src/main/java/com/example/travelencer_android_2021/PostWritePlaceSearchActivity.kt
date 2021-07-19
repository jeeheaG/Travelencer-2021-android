package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostWritePlaceSearchAdapter
import com.example.travelencer_android_2021.databinding.ActivityPostWritePlaceSearchBinding
import com.example.travelencer_android_2021.model.ModelCasePlaceCard

class PostWritePlaceSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWritePlaceSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWritePlaceSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //장소 검색 더미데이터
        val placeList = arrayListOf(
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "해우재", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_hwasung, "수원 화성", "경기도 수원시", "화성는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "무슨산 공원", "경기도 수원시", "가가가가가 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "수원 관광지", "경기도 수원시", "관광지는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "뭐라고갈비", "경기도 수원시", "뭐는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "광교산", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "수원 왕갈비", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "무슨호수", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ"),
                ModelCasePlaceCard(R.drawable.dummy_haewoojae, "저기호수공원", "경기도 수원시", "해우재는 경기도 수원의 특색있는 관광지로 주목받고 있는 어쩌구 라랄라라라ㅏ")
        )

        binding.rvPlaceSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlaceSearch.setHasFixedSize(true)

        //장소 검색 후 원하는 장소 선택하여 PNC입력 후 돌아왔을 때 현재 액티비티 종료하고 선택한 장소 데이터를 PostWriteActivity로 전달
        val postWritePlaceSearchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val intent = Intent(this, PostWriteActivity::class.java)
                val data = result.data
                intent.putExtra("placeName", data?.getStringExtra("placeName"))
                intent.putExtra("placeLoc", data?.getStringExtra("placeLoc"))
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        binding.btnPlaceSearch.setOnClickListener {
            val keyword = binding.etPlaceSearchKeyword.text
            val resultList = arrayListOf<ModelCasePlaceCard>()
            for(p in placeList){
                if(p.name.contains(keyword)){
                    resultList.add(p)
                }
            }

            binding.rvPlaceSearch.adapter = PostWritePlaceSearchAdapter(resultList, this, postWritePlaceSearchLauncher)

            //키보드 내림
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }



        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}