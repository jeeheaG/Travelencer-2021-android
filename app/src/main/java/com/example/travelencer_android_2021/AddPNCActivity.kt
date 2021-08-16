package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityAddPNCBinding

// Main에서 왔는지 Write에서 왔는지 resultCode로 구분
const val RESULT_CODE_MAIN = Activity.RESULT_FIRST_USER + 0 //1
const val RESULT_CODE_WRITE = Activity.RESULT_FIRST_USER + 1 //2

class AddPNCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPNCBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPNCBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // placeMain의 여행지 등록 / postWrite의 여행지 검색 / 여행지 등록 -> 셋 중 어디서 온 건 지 알아야 함
        val from = intent.getStringExtra("from")
        val placeName = intent.getStringExtra(codePlaceName)
        val placeLoc = intent.getStringExtra(codePlaceLoc)

        binding.btnPlaceRegisterDone.setOnClickListener {
            // TODO : DB : DB로 PNC 데이터 전송

            when(from){
                "placeMain" -> {
                    setResult(RESULT_CODE_MAIN)
                    finish()
                }
                else -> { // postWrite에서 온 "search" 나 "add"일 경우 장소명과 장소주소를 가지고 돌아감
                    val resultIntent = Intent(this, PostWritePlaceSearchActivity::class.java)
                    resultIntent.putExtra(codePlaceName, placeName)
                    resultIntent.putExtra(codePlaceLoc, placeLoc)
                    setResult(RESULT_CODE_WRITE, resultIntent)

                    finish()
                }
            }
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}