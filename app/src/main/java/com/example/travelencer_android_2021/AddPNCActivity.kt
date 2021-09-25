package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.travelencer_android_2021.data.PlaceRegisterData
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

        binding.btnPlaceRegisterDone.setOnClickListener {
            when(from){
                "placeMain" -> {
                    //서버통신

                    setResult(RESULT_CODE_MAIN)
                    finish()
                }
                "search" -> {
                    // postWrite에서 온 "search" 나 "add"일 경우 장소명과 장소주소를 가지고 돌아감
                    val writeIntent = Intent(this, PostWritePlaceSearchActivity::class.java)
                    writeIntent.putExtra(codePlaceName, intent.getStringExtra(codePlaceName))
                    writeIntent.putExtra(codePlaceLoc, intent.getStringExtra(codePlaceLoc))
                    setResult(RESULT_CODE_WRITE, writeIntent)

                    finish()
                }
                "add" -> {
                    // TODO : 코드 고치세유
                    //서버에 넘겨줄 데이터 만듦
                    val placeData = PlaceRegisterData(
                            plcName = intent.getStringExtra("placeName") ?: "",
                            plcProduce = intent.getStringExtra("placeExplain") ?: "",
                            plcAddress = intent.getStringExtra("placeAddress") ?: "",
                            plcCategory = intent.getIntExtra("placeCategory", 1),
                            plcPicture = intent.getSerializableExtra("placeImage") as ArrayList<Uri>? ?: arrayListOf<Uri>(),
//                            plcGood = binding.etPlaceRegisterCons.text.toString(),
//                            plcBad = binding.etPlaceRegisterPros.text.toString(),
                            locX = intent.getFloatExtra("placeLatitude", 0f),
                            locY = intent.getFloatExtra("placeLongitude", 0f)
                            //plcPicture = photoList
                    )
                    // 서버에 장소등록 요청 보냄(<다음으로> 버튼을 눌러 placeData 데이터가 만들어진 상태여야 함)
                    //잠시 주석
                    //postAddPlace(placeData)

                    val writeIntent = Intent(this, PostWritePlaceSearchActivity::class.java)
                    writeIntent.putExtra(codePlaceName, intent.getStringExtra(codePlaceName))
                    writeIntent.putExtra(codePlaceLoc, intent.getStringExtra(codePlaceLoc))
                    setResult(RESULT_CODE_WRITE, writeIntent)

                    finish()
                }
            }
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }

}