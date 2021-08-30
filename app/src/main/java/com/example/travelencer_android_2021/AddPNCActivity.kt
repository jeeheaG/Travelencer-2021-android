package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.travelencer_android_2021.api.RetrofitClientPlace
import com.example.travelencer_android_2021.data.PlaceRegisterData
import com.example.travelencer_android_2021.data.PlaceRegisterResponse
import com.example.travelencer_android_2021.databinding.ActivityAddPNCBinding
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response

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

        //interceptor설정과 데이터 요청 함수
        RetrofitClientPlace.interceptor.level = HttpLoggingInterceptor.Level.BODY

        // placeMain의 여행지 등록 / postWrite의 여행지 검색 / 여행지 등록 -> 셋 중 어디서 온 건 지 알아야 함
        val from = intent.getStringExtra("from")

        binding.btnPlaceRegisterDone.setOnClickListener {
            when(from){
                "placeMain" -> {

                    //서버에 넘겨줄 데이터 만듦
                    val placeData = PlaceRegisterData(
                            plcName = intent.getStringExtra("placeName") ?: "",
                            plcProduce = intent.getStringExtra("placeExplain") ?: "",
                            plcAddress = intent.getStringExtra("placeAddress") ?: "",
                            plcCategory = intent.getIntExtra("placeCategory", 1),
                            plcPicture = intent.getStringExtra("placeImage", ) ?: "",
                            plcGood = binding.etPlaceRegisterCons.text.toString(),
                            plcBad = binding.etPlaceRegisterPros.text.toString(),
                            locX = intent.getFloatExtra("placeLatitude", 0f),
                            locY = intent.getFloatExtra("placeLongitude", 0f)
                            //plcPicture = photoList
                    )
                    // 서버에 장소등록 요청 보냄(<다음으로> 버튼을 눌러 placeData 데이터가 만들어진 상태여야 함)
                    postAddPlace(placeData)
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

                    //서버에 넘겨줄 데이터 만듦
                    val placeData = PlaceRegisterData(
                            plcName = intent.getStringExtra("placeName") ?: "",
                            plcProduce = intent.getStringExtra("placeExplain") ?: "",
                            plcAddress = intent.getStringExtra("placeAddress") ?: "",
                            plcCategory = intent.getIntExtra("placeCategory", 1),
                            plcPicture = intent.getStringExtra("placeImage", ) ?: "",
                            plcGood = binding.etPlaceRegisterCons.text.toString(),
                            plcBad = binding.etPlaceRegisterPros.text.toString(),
                            locX = intent.getFloatExtra("placeLatitude", 0f),
                            locY = intent.getFloatExtra("placeLongitude", 0f)
                            //plcPicture = photoList
                    )
                    // 서버에 장소등록 요청 보냄(<다음으로> 버튼을 눌러 placeData 데이터가 만들어진 상태여야 함)
                    postAddPlace(placeData)

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

    private fun postAddPlace(data: PlaceRegisterData){
        val call = RetrofitClientPlace.serviceApiPlace.placeRegister(data)
        call.enqueue(object : retrofit2.Callback<PlaceRegisterResponse> {
            override fun onResponse(call: Call<PlaceRegisterResponse>, response: Response<PlaceRegisterResponse>) {
                if (response.isSuccessful) {
                    val result: PlaceRegisterResponse = response.body()!!
//                    Log.d("로그 postAddPlace code", "${ response.code() }")
//                    Log.d("로그 postAddPlace msg", response.message())

                    Log.d("로그 postAddPlace code", "${result.code}")
                    Log.d("로그 postAddPlace msg", result.message)

                    Log.d("로그 postAddPlace", "${result}")

                }
            }

            override fun onFailure(call: Call<PlaceRegisterResponse>, t: Throwable) {
                Log.d("로그 postAddPlace fail", "실패..")
                t.printStackTrace()

            }
        })
    }
}