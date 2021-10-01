package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostWritePhotoUriAdapter
import com.example.travelencer_android_2021.data.PlaceRegisterData
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

//미러링 테스트
class AddPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"
    //private val codeAddress = "address"

    //var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var latitude = 0f
        var longitude = 0f
        var photoList = arrayListOf<Uri>()

        //auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        //사진 RecyclerView 설정
        binding.rvPlaceRegisterPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlaceRegisterPhotoList.setHasFixedSize(true)

        // 사진 recyclerView adapter 설정
        val photoAdapter = PostWritePhotoUriAdapter(photoList, this)
        binding.rvPlaceRegisterPhotoList.adapter = photoAdapter


        //pnc에서 작업 실행 후 돌아왔을 때 실행되는 launcher
        val pncResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_CODE_MAIN || result.resultCode == RESULT_CODE_WRITE){

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
        // TODO : 처음에 사진 넣는 게 bind가 됐다 안됐다 하는데 이건 뭐람 ㅋㅋㅋㅋㅋㅋ 일단 푸시.. bitmap으로 바꿔보자
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
                    Log.d("로그 addPlaceUri-----","URI : ${uri}")
                }
                //이미지를 여러개 선택했을 경우
                else{
                    //uri 여러 개일 때 꺼내오기 result.data.clipData.getItemAt(i).uri
                    for(i in 0 until clipData.itemCount){
                        val uri = clipData.getItemAt(i).uri
                        photoList.add(uri)
                        Log.d("로그 addPlaceUri-----","URI : ${uri}")
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

        // TODO : <장소등록>으로 버튼 이름 바꾸기
        // <다음으로> 클릭
        binding.btnPlaceRegisterNext.setOnClickListener {
            //카테고리 라디오 버튼 선택에 따라 데이터 만듦
            var categoryChecked: Int = 1
            when(binding.rgPlaceRegisterCategory.checkedRadioButtonId){
                R.id.rbtnPlaceRegisterCategoryFood -> {categoryChecked = 0}
                //R.id.rbtnPlaceRegisterCategorySights -> {radioChecked = 1}
            }

            // TODO : 장소등록 요청
            //데이터들 RequestBody로 변환. 숫자형 데이터들은 다 String으로 바꿈
            val plcName = binding.etPlaceRegisterName.text.toString() ?: ""
            val plcProduce = binding.etPlaceRegisterExplain.text.toString() ?: ""
            val plcAddress = binding.tvPlaceRegisterAddressInput.text.toString() ?: ""
            val plcCategory = categoryChecked
            //val plcPicture = photoList ?: arrayListOf<Uri>()
            val locX = latitude
            val locY = longitude
            val timestamp = SimpleDateFormat("yyMMdd'T'HHmmss.SSS").format(Date())

            val plcId = "${timestamp}_${locX}_${locY}"
            val placeData = PlaceRegisterData(plcName, plcProduce, plcAddress, plcCategory, locX, locY, plcId)

            postAddPlace(placeData)

            //장소명이랑 주소, 장소id, 어디서 왔는지 넘겨주자
            val pncIntent = Intent(this, AddPNCActivity::class.java)
            pncIntent.putExtra("placeName", plcName)
            pncIntent.putExtra("placeAddress", plcAddress)
            pncIntent.putExtra("placeId", plcId)
            pncIntent.putExtra("from", intent.getStringExtra("from"))
            pncResultLauncher.launch(pncIntent)

        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }

    private fun postAddPlace(placeData: PlaceRegisterData){
        firestore?.collection("placeT")?.document()?.set(placeData)
            ?.addOnSuccessListener {
                Log.d("로그-placeRegister 성공-----", "성공!")
            }
            ?.addOnFailureListener {
                    e -> Log.w("로그-placeRegister 실패 . . .", "실 패 . .", e)
            }
    }


}