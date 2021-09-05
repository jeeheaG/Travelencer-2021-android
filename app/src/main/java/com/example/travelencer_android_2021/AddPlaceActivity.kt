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
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceBinding

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

        //미리 선언해둬야하는 변수들
//        var placeName: String
//        var placeExplain: String
//        var placeAddress: String
//        var placeLatitude: Float
//        var placeLongitude: Float
//        var placeCategory: String
//        var placeImageList: ArrayList<Uri>
/*        var placeData: PlaceRegisterData? = null*/
        var latitude = 0f
        var longitude = 0f
        var photoList = arrayListOf<Uri>()


        //사진 RecyclerView 설정
        binding.rvPlaceRegisterPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlaceRegisterPhotoList.setHasFixedSize(true)

        // 사진 recyclerView adapter 설정
        val photoAdapter = PostWritePhotoUriAdapter(photoList, this)
        binding.rvPlaceRegisterPhotoList.adapter = photoAdapter


        //pnc에서 작업 실행 후 돌아왔을 때 실행되는 launcher
        val pncResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_CODE_MAIN || result.resultCode == RESULT_CODE_WRITE){
/*                //interceptor설정과 데이터 요청 함수
                RetrofitClientPlace.interceptor.level = HttpLoggingInterceptor.Level.BODY

                // 서버에 장소등록 요청 보냄(<다음으로> 버튼을 눌러 placeData 데이터가 만들어진 상태여야 함)
                placeData?.let { postAddPlace(it) }*/

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


        //주소 입력 후 돌아왔을 때 실행되는 launcher
        val addressResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                //Log.d("로그data", "${data}")
                if (data != null) {
                    binding.tvPlaceRegisterAddressInput.text = data.getStringExtra("name")
                    binding.tvPlaceRegisterAddressInput.setTextColor(ContextCompat.getColor(this, R.color.black))

                    latitude = data.getFloatExtra("latitude", 0f)
                    longitude = data.getFloatExtra("longitude", 0f)

                    //Log.d("로그name", "name= ${data.getStringExtra("name")} // bind= ${binding.tvPlaceRegisterAddressInput.text}")
                    //Log.d("로그lat long", "lat= ${latitude} // long= ${longitude}")
                }
            }
        }


        // 사진 선택 후 돌아왔을 때 실행되는 launcher. 사진 목록 리스트 photoList에 uri값들을 추가함
        // TODO : 처음에 사진 넣는 게 bind가 됐다 안됐다 하는데 이건 뭐람 ㅋㅋㅋㅋㅋㅋ 일단 푸시.. 내 폰 성능 문제일수도
        //  -> uri말고 비트맵으로 하면 되려나? 나중에 고쳐보자
        val addPhotoResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            // reult.data에는 선택한 사진들의 uri 가 들어있음
            val imageData = result.data

            // 이미지를 한 개라도 선택했을 경우
            if(result.resultCode == Activity.RESULT_OK && imageData != null){
                val clipData = imageData.clipData

                // 이미지를 한 개만 선택했을 경우
                if(clipData == null){
                    //uri 한 개 꺼내오기
                    val uri = imageData.data
                    uri?.let{ photoList.add(uri) }
                }
                //이미지를 여러개 선택했을 경우
                else{
                    //uri 여러 개일 때 꺼내오기 result.data.clipData.getItemAt(i).uri
                    for(i in 0 until clipData.itemCount){
                        val uri = clipData.getItemAt(i).uri
                        photoList.add(uri)
                    }
                }

                //TODO : 이거 호출하는 부분이 문제인가? 음..
                photoAdapter.notifyDataSetChanged()
            }

            //아무 이미지도 선택하지 않고 돌아왔을 경우
            else{
                Log.d("로그 place ------", "아무 사진도 선택하지 않았습니다.")
            }
        }

        // <주소 찾기> 클릭
        binding.btnPlaceRegisterAddressSearch.setOnClickListener {
            val addressIntent = Intent(this, AddPlaceSearchAddressActivity::class.java)
            addressResultLauncher.launch(addressIntent)
        }

        // <사진 추가> 버튼 클릭
        binding.btnPlaceRegisterAddPhoto.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI //선택한 사진 uri 를 intent의 data에 저장
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

            // AddPlace화면에서 입력한 정보들 다 넘겨줌. 서버로 보내는 건 AddPNC에서 함.
            val pncIntent = Intent(this, AddPNCActivity::class.java)
            pncIntent.putExtra("placeName", binding.etPlaceRegisterName.text.toString())
            pncIntent.putExtra("placeExplain", binding.etPlaceRegisterExplain.text.toString())
            pncIntent.putExtra("placeAddress", binding.tvPlaceRegisterAddressInput.text.toString())
            pncIntent.putExtra("placeCategory", categoryChecked)
            //pncIntent.putExtra("placeImage", photoList)
            // **photoList는 이미지들의 uri가 담긴 ArrayLsit<Uri> 임.
            //원래는 intent로 arrayList전달 시 arrayList가 담는 자료형에 Precelable 를 추가로 구현해 줘야 하지만 Uri는 이미 구현이 되어있어서 나는 그냥 쓸 수 있었음!
            pncIntent.putParcelableArrayListExtra("placeImage", photoList)
            pncIntent.putExtra("placeLatitude", latitude)
            pncIntent.putExtra("placeLongitude", longitude)
            pncIntent.putExtra("from", intent.getStringExtra("from"))
            pncResultLauncher.launch(pncIntent)

        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }

    /*private fun postAddPlace(data: PlaceRegisterData){
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
    }*/
}