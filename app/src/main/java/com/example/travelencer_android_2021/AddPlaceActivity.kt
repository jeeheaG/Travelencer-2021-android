package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostWritePhotoUriAdapter
import com.example.travelencer_android_2021.api.RetrofitClientPlace
import com.example.travelencer_android_2021.data.PlaceRegisterData
import com.example.travelencer_android_2021.data.PlaceRegisterResponse
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceBinding
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response

class AddPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"
    //private val codeAddress = "address"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        var placeName: String
//        var placeExplain: String
//        var placeAddress: String
//        var placeLatitude: Float
//        var placeLongitude: Float
//        var placeCategory: String
//        var placeImageList: ArrayList<Uri>
        var placeData: PlaceRegisterData? = null
        var photoList = arrayListOf<Uri>()

        //사진 목록 RecyclerView 설정
        binding.rvPlaceRegisterPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlaceRegisterPhotoList.setHasFixedSize(true)

        //pnc에서 작업 실행 후 돌아왔을 때 실행되는 함수
        val pncResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_CODE_MAIN || result.resultCode == RESULT_CODE_WRITE){
                //interceptor설정과 데이터 요청 함수
                RetrofitClientPlace.interceptor.level = HttpLoggingInterceptor.Level.BODY

                // 서버에 장소등록 요청 보냄(<다음으로> 버튼을 눌러 placeData 데이터가 만들어진 상태여야 함)
                placeData?.let { postAddPlace(it) }

                when(result.resultCode){
                    //placeMain에서 온 경우
                    RESULT_CODE_MAIN -> {
                        finish()
                    }

                    // postWrite에서 온 "search" 나 "add"일 경우 장소명과 장소주소를 가지고 PostWriteActivity로 돌아감
                    RESULT_CODE_WRITE -> {
                        val resultIntent = Intent(this, PostWriteActivity::class.java)
                        val placeName = result.data?.getStringExtra(codePlaceName)
                        val placeLoc = result.data?.getStringExtra(codePlaceLoc)
                        resultIntent.putExtra(codePlaceName, placeName)
                        resultIntent.putExtra(codePlaceLoc, placeLoc)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }//when
            }
        }

        val addressResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                //Log.d("로그data", "${data}")
                if (data != null) {
                    binding.tvPlaceRegisterAddressInput.text = data.getStringExtra("name")
                    binding.tvPlaceRegisterAddressInput.setTextColor(ContextCompat.getColor(this, R.color.black))

                    val latitude = data.getFloatExtra("latitude", 0f)
                    val longitude = data.getFloatExtra("longitude", 0f)

                    //Log.d("로그name", "name= ${data.getStringExtra("name")} // bind= ${binding.tvPlaceRegisterAddressInput.text}")
                    //Log.d("로그lat long", "lat= ${latitude} // long= ${longitude}")
                }
            }
        }

        // 사진 추가 launcher. 사진 목록 리스트 photoList에 추가함
        // TODO : 처음에 사진 여러장 넣는 게 bind가 됐다 안됐다 하는데 이건 뭐람 ㅋㅋㅋㅋㅋㅋ 일단 푸시.. 내 폰 성능 문제일수도
        val photoAdapter = PostWritePhotoUriAdapter(photoList, this)
        binding.rvPlaceRegisterPhotoList.adapter = photoAdapter

        val addPhotoResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            val imageData = result.data
            // 이미지를 한 개라도 선택했을 경우
            if(result.resultCode == Activity.RESULT_OK && imageData != null){
                val clipData = imageData.clipData
                // 이미지를 한 개만 선택했을 경우
                if(clipData == null){
                    val uri = imageData.data
                    uri?.let{ photoList.add(uri) }
                }
                //이미지를 여러개 선택했을 경우
                else{
                    for(i in 0 until clipData.itemCount){
                        val uri = clipData.getItemAt(i).uri
                        photoList.add(uri)
                    }
                }

                photoAdapter.notifyDataSetChanged()
            }
        }

        // <주소 찾기> 클릭
        binding.btnPlaceRegisterAddressSearch.setOnClickListener {
            val intent = Intent(this, AddPlaceSearchAddressActivity::class.java)
            addressResultLauncher.launch(intent)
        }

        // <사진 추가> 버튼 클릭
        binding.btnPlaceRegisterAddPhoto.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            photoIntent.type = MediaStore.Images.Media.CONTENT_TYPE
            photoIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addPhotoResultLauncher.launch(photoIntent)
        }

        // <다음으로> 클릭
        binding.btnPlaceRegisterNext.setOnClickListener {
            var categoryChecked: Int = 1
            when(binding.rgPlaceRegisterCategory.checkedRadioButtonId){
                R.id.rbtnPlaceRegisterCategoryFood -> {categoryChecked = 0}
                //R.id.rbtnPlaceRegisterCategorySights -> {radioChecked = 1}
            }

            //서버에 넘겨줄 데이터 만들어둠
            placeData = PlaceRegisterData(
                    plcName = binding.etPlaceRegisterName.text.toString(),
                    plcExplain = binding.etPlaceRegisterExplain.text.toString(),
                    plcAddress = binding.tvPlaceRegisterAddressInput.text.toString(),
                    plcCategory = categoryChecked,
                    plcPicture = "ThisIsDummyStringAnything"
                    //plcPicture = photoList

            )

            val intent = Intent(this, AddPNCActivity::class.java)
            intent.putExtra(codePlaceName, binding.etPlaceRegisterName.text.toString())
            intent.putExtra(codePlaceLoc, binding.tvPlaceRegisterAddressInput.text.toString())
            intent.putExtra("placeCategory", categoryChecked)
            intent.putExtra("from", "add")
            pncResultLauncher.launch(intent)
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