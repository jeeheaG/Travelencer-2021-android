package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PhotoBitmapAdapter
import com.example.travelencer_android_2021.data.PlaceRegisterData
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceBinding
import com.example.travelencer_android_2021.model.FetxhXML
import com.example.travelencer_android_2021.model.ModelPlacePhotoT
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"
    lateinit var spinner : Array<Spinner>   // 스피너 배열
    lateinit var spinnerSetting : FetxhXML
    //private val codeAddress = "address"

    //var auth : FirebaseAuth? = null // 장소등록 시 등록한 사람 정보 안 남음
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //스피너 배열 생성
        spinner = arrayOf(view.findViewById(R.id.spinAddPlaceLarge), view.findViewById(R.id.spinAddPlaceSmall))
        // 스피너 설정
        spinnerSetting = FetxhXML(spinner, applicationContext)
        spinnerSetting.fetchXML("http://api.visitkorea.or.kr/upload/manual/sample/areaCode_sample1.xml", 0)

        var latitude = 0f
        var longitude = 0f
        val timestamp = SimpleDateFormat("yyMMdd'T'HHmmss.SSS").format(Date())
        var photoList = arrayListOf<Uri>()
        var photoBitmapList = arrayListOf<Bitmap>()

        //auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        //사진 RecyclerView 설정
        binding.rvPlaceRegisterPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlaceRegisterPhotoList.setHasFixedSize(true)

        // 사진 recyclerView adapter 설정
        val photoAdapter = PhotoBitmapAdapter(photoBitmapList, this)
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
                    val bitmap = uriToBitmapRotate(uri)
                    uri?.let{ photoBitmapList.add(bitmap) }
                    Log.d("로그 addPlaceUri-----","URI : ${uri}")
                }
                //이미지를 여러개 선택했을 경우
                else{
                    //uri 여러 개일 때 꺼내오기 result.data.clipData.getItemAt(i).uri
                    for(i in 0 until clipData.itemCount){
                        val uri = clipData.getItemAt(i).uri
                        photoList.add(uri)
                        val bitmap = uriToBitmapRotate(uri)
                        uri?.let{ photoBitmapList.add(bitmap) }
                        Log.d("로그 addPlaceUri-----","URI : ${uri}")
                    }
                }

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
            //val photoIntent = Intent(Intent.ACTION_PICK) // 기본 갤러리 앱으로 선택 - 일부 기기에서 사진이 한 장밖에 선택 안 됨
            val photoIntent = Intent(Intent.ACTION_GET_CONTENT) // 구글 갤러리 앱으로 선택
            photoIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI //선택한 사진 uri 를 intent의 data에 저장
//            photoIntent.type = MediaStore.Images.Media.CONTENT_TYPE
            photoIntent.type = "image/*"      // 모든 이미지
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

            val plcId = "${timestamp}_${locX}_${locY}"
            Log.d("로그 addPlace----locXY", "locX : ${locX}, locY : ${locY}")
            Log.d("로그 addPlace----plcId", "plcId : ${plcId}")

            // 지역명, 시군구명 변수
            var area1 : String = ""
            var area2 : String = ""
            var area1Code = -1
            var area2Code = -1
            try {
                area1 = if(spinner[0].selectedItem!=null) spinner[0].selectedItem.toString() else "선택안함"   // 지역명
                area2 = if(spinner[1].selectedItem!=null) spinner[1].selectedItem.toString() else "선택안함"   // 시군구명
                // TODO : 지역 코드 이거 맞나....
                area1Code = spinnerSetting.areaCodeArray[0][(spinner[0].selectedItemPosition)].areaCode // 지역 코드
                area2Code = spinnerSetting.areaCodeArray[1][(spinner[1].selectedItemPosition)].areaCode // 시군구 코드
            }
            catch (e : NullPointerException) {
                Toast.makeText(applicationContext, "지역을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            catch (e : ArrayIndexOutOfBoundsException) {
                Toast.makeText(applicationContext, "잠시만 기다려주세요.", Toast.LENGTH_SHORT).show()
            }

            val placeData = PlaceRegisterData(plcName, plcProduce, plcAddress, plcCategory, locX, locY, plcId, area1, area2, area1Code, area2Code)
            postAddPlace(placeData, photoList, timestamp)

            //장소명이랑 주소, 장소id, 어디서 왔는지 넘겨주자
            val pncIntent = Intent(this, AddPNCActivity::class.java)
//            pncIntent.putExtra("placeName", plcName)
//            pncIntent.putExtra("placeAddress", plcAddress)
            pncIntent.putExtra("placeId", plcId)
            pncIntent.putExtra("from", intent.getStringExtra("from"))
            pncResultLauncher.launch(pncIntent)

        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }

    private fun uriToBitmapRotate(uri: Uri?): Bitmap {
//        val matrix =  Matrix()
//        matrix.postRotate(90f) //사진이 90도 돌아서 들어가는 현상이 있어 정방향으로 돌려줌
//        val rowBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//        val bitmap = Bitmap.createBitmap(rowBitmap, 0,0, rowBitmap.width, rowBitmap.height, matrix, true)

        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        return bitmap
    }

    private fun postAddPlace(placeData: PlaceRegisterData, photoList: ArrayList<Uri>, timeStamp: String){
        firestore?.collection("placeT")?.document()?.set(placeData)
            ?.addOnSuccessListener {
                Log.d("로그-placeRegister 성공-----", "성공!")
            }
            ?.addOnFailureListener {
                    e -> Log.w("로그-placeRegister 실패 . . .", "실 패 . .", e)
            }
        photoUpload(photoList, placeData.plcId, timeStamp)

    }

    //placePhotoT에 사진 목록 만들고 이미지파일을 storage에 업로드
    private fun photoUpload(photoList: ArrayList<Uri>, placeId: String, timeStamp: String){
        for ((imgCount, uri) in photoList.withIndex()){ //photoList 각 데이터에 인덱스번호를 붙여서 imgCount라는 이름으로 사용
            val imgFileName = "${placeId}_${imgCount}.jpg"
            val storage = FirebaseStorage.getInstance().getReference("/place/$imgFileName")
            storage.putFile(uri).addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    val modelPlacePhotoT = ModelPlacePhotoT(placeId, imgFileName)
                    firestore?.collection("placePhotoT")?.document()?.set(modelPlacePhotoT)
                }
            }
        }
    }


}