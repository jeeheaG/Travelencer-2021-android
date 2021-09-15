package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.travelencer_android_2021.api.RetrofitClientPlace
import com.example.travelencer_android_2021.data.PlaceRegisterData
import com.example.travelencer_android_2021.data.PlaceRegisterResponse
import com.example.travelencer_android_2021.databinding.ActivityAddPNCBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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
/*                    val placeData = PlaceRegisterData(
                            plcName = intent.getStringExtra("placeName") ?: "",
                            plcProduce = intent.getStringExtra("placeExplain") ?: "",
                            plcAddress = intent.getStringExtra("placeAddress") ?: "",
                            plcCategory = intent.getIntExtra("placeCategory", 1),
                            plcPicture = intent.getSerializableExtra("placeImage") as ArrayList<Uri>? ?: arrayListOf<Uri>(),
                            plcGood = binding.etPlaceRegisterCons.text.toString(),
                            plcBad = binding.etPlaceRegisterPros.text.toString(),
                            locX = intent.getFloatExtra("placeLatitude", 0f),
                            locY = intent.getFloatExtra("placeLongitude", 0f)
                            //plcPicture = photoList
                    )*/
                    //extra로 넘어온 데이터들 RequestBody로 변환해서 받음. 숫자형 데이터들은 다 String으로 바꿈
                    val plcName = (intent.getStringExtra("placeName") ?: "").toRequestBody("text/plain".toMediaType())
                    val plcProduce = (intent.getStringExtra("placeExplain") ?: "").toRequestBody("text/plain".toMediaType())
                    val plcAddress = (intent.getStringExtra("placeAddress") ?: "").toRequestBody("text/plain".toMediaType())
                    val plcCategory = (intent.getIntExtra("placeCategory", 1)).toString().toRequestBody("text/plain".toMediaType())
                    val plcPicture = intent.getSerializableExtra("placeImage") as ArrayList<Uri>? ?: arrayListOf<Uri>()
                    val plcGood = (binding.etPlaceRegisterCons.text.toString()).toRequestBody("text/plain".toMediaType())
                    val plcBad = (binding.etPlaceRegisterPros.text.toString()).toRequestBody("text/plain".toMediaType())
                    val locX = (intent.getFloatExtra("placeLatitude", 0f)).toString().toRequestBody("text/plain".toMediaType())
                    val locY = (intent.getFloatExtra("placeLongitude", 0f)).toString().toRequestBody("text/plain".toMediaType())

                    //이미지 데이터 제외하고 모두 HashMap에 넣음. 이 때 키값이 서버 변수값이랑 똑같아야 함.
                    val placeData: HashMap<String, RequestBody> = hashMapOf()
                    placeData["plcName"] = plcName
                    placeData["plcProduce"] = plcProduce
                    placeData["plcAddress"] = plcAddress
                    placeData["plcCategory"] = plcCategory
                    placeData["plcGood"] = plcGood
                    placeData["plcBad"] = plcBad
                    placeData["locX"] = locX
                    placeData["locY"] = locY

                    //이미지 uri들로 이미지를 서버에 보낼 수 있는 형태인 MultipartBody.Part 로 만듦
                    // TODO : 이미지 여러장 전송하려면 MultipartBody.Part를 담는 ArrayList로 보내야 한대.. 일단 한장만 보내보고 서버도 잘 되면 코드 바꿀까?
                    var multiBody: MultipartBody.Part? = null
                    for(uri in plcPicture){
                        Log.d("로그 addPNC multi uri----", "URI : ${uri}")

                        val bitmap = BitmapFactory.decodeFile(uri.toString()) //이미지 bitmap 데이터 가져오기
                        val bos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) //확장자를 PNG로 하고 압축 정도를 0으로 해 ByteArrayOutputStream에 넣음
                        val byteArray = bos.toByteArray() //byteArray로 변환

                        val file = File(applicationContext.filesDir, applicationContext.filesDir.name+".png") //file 객체 생성
                        val fos = FileOutputStream(file) // file의 경로을 가진 FileOutputStream 생성
                        fos.write(byteArray) // FileOutputStream에 byteArray데이터 담음
                        fos.flush() // file의 경로에 byteArray데이터 저장
                        fos.close() // FileOutputStream 종료

                        val reqBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                        multiBody = MultipartBody.Part.createFormData("upload", file.name+".png", reqBody)
                    }


                    // 서버에 장소등록 요청 보냄(<다음으로> 버튼을 눌러 placeData 데이터가 만들어진 상태여야 함)
                    postAddPlace(multiBody, placeData)
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
                            plcGood = binding.etPlaceRegisterCons.text.toString(),
                            plcBad = binding.etPlaceRegisterPros.text.toString(),
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

    private fun postAddPlace(image: MultipartBody.Part?, data: HashMap<String, RequestBody>){
        if (!NetworkManager(applicationContext).checkNetworkState()) {
            Toast.makeText(applicationContext, "네트워트 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClientPlace.serviceApiPlace.placeRegister(image, data)
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