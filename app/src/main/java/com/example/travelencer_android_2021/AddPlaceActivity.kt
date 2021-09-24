package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.travelencer_android_2021.api.RetrofitClientPlace
import com.example.travelencer_android_2021.data.PlaceRegisterResponse
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

//미러링 테스트
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

        var latitude = 0f
        var longitude = 0f
        var photoList = arrayListOf<Uri>()
        var photoRealPathList = arrayListOf<String>()


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

                //이미지 몇 장을 선택했든 실행되는 코드!!!!!
                //uri정보가 들어있는 photoList를 이용해 이미지들의 절대 경로 구함!!!!!!!
                //갤러리DB정보인 uri로부터 이미지의 실제 경로 가져오기
                for(i in 0 until photoList.size){
                    photoRealPathList.add(getRealPath(photoList[i], this) ?: "") //null일 경우 빈 문자열 반환
                }

                Log.d("로그 AddPlace uri to 경로--", "all번쩨 : ${photoRealPathList}")
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
            val plcName = (binding.etPlaceRegisterName.text.toString() ?: "").toRequestBody("text/plain".toMediaType())
            val plcProduce = (binding.etPlaceRegisterExplain.text.toString() ?: "").toRequestBody("text/plain".toMediaType())
            val plcAddress = (binding.tvPlaceRegisterAddressInput.text.toString() ?: "").toRequestBody("text/plain".toMediaType())
            val plcCategory = (categoryChecked).toString().toRequestBody("text/plain".toMediaType())
            val plcPicture = photoRealPathList ?: arrayListOf<String>()
            val locX = (latitude).toString().toRequestBody("text/plain".toMediaType())
            val locY = (longitude).toString().toRequestBody("text/plain".toMediaType())

            //이미지 데이터 제외하고 모두 HashMap에 넣음. 이 때 키값이 서버 변수값이랑 똑같아야 함.
            val placeData: HashMap<String, RequestBody> = hashMapOf()
            placeData["plcName"] = plcName
            placeData["plcProduce"] = plcProduce
            placeData["plcAddress"] = plcAddress
            placeData["plcCategory"] = plcCategory
            placeData["locX"] = locX
            placeData["locY"] = locY

            //이미지 uri들로 이미지를 서버에 보낼 수 있는 형태인 MultipartBody.Part 로 만듦
            // TODO : 이미지 여러장 전송하려면 MultipartBody.Part를 담는 ArrayList로 보내야 한대.. 일단 한장만 보내보고 서버도 잘 되면 코드 바꿀까?
            var multiBody: MultipartBody.Part? = null
            for(realPath in plcPicture){ //plcPicture 뭐가 들었어? -절대경로
                //Log.d("로그 addPNC multi uri----", "URI : ${uri}")
//                D/로그 addPlaceUri-----: URI : content://media/external/images/media/84
//                D/로그 AddPlace uri to 경로--: all번쩨 : [/data/user/0/com.example.travelencer_android_2021/1631794432818]

                val bitmap = BitmapFactory.decodeFile(realPath) //이미지 bitmap 데이터 가져오기

                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) //확장자를 PNG로 하고 압축 정도를 0으로 해 ByteArrayOutputStream에 넣음
                val byteArray = bos.toByteArray() //byteArray로 변환

                val file = File(applicationContext.filesDir, applicationContext.filesDir.name+System.currentTimeMillis()+"_forByteArray.png") //file 객체 생성
                Log.d("로그 AddPlace File--", "file 경로")
                val fos = FileOutputStream(file) // file의 경로을 가진 FileOutputStream 생성
                fos.write(byteArray) // FileOutputStream에 byteArray데이터 담음
                fos.flush() // file의 경로에 byteArray데이터 저장
                fos.close() // FileOutputStream 종료

                val reqBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                multiBody = MultipartBody.Part.createFormData("file", file.name, reqBody)
                Log.d("로그 AddPlace 장소등록Post요청", "filename : ${file.name}")


                //val reqBody = file.asRequestBody("image/*".toMediaTypeOrNull())
/*                val f = File(realPath)
                val reqBody = f.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                multiBody = MultipartBody.Part.createFormData("file", f.name, reqBody)
                Log.d("로그 AddPlace 장소등록Post요청", "filename : ${f.name}")*/
            }

            // 서버에 장소등록 요청 보냄. 사진, 텍스트 데이터들 묶음
            postAddPlace(multiBody, placeData)

            //코드 뒤집기.. 일단 장소명이랑 주소, 어디서 왔는지만 넘겨주자 나중에 손보기
            val pncIntent = Intent(this, AddPNCActivity::class.java)
            pncIntent.putExtra("placeName", binding.etPlaceRegisterName.text.toString())
            pncIntent.putExtra("placeAddress", binding.tvPlaceRegisterAddressInput.text.toString())
            pncIntent.putExtra("from", intent.getStringExtra("from"))
            pncResultLauncher.launch(pncIntent)

        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }

    //갤러리DB정보인 uri를 이용해 이미지의 실제 절대 경로 가져오는 함수 : 새 경로를 만들어 이미지를 복사해 새 경로에 복사본을 만들고 목사본의 경로를 가져옴
    private fun getRealPath(uri: Uri, context: Context): String? {
        //새 경로 만들기
        val filePath = context.applicationInfo.dataDir + File.separator + System.currentTimeMillis()
        val file = File(filePath)

        try{
            //uri로 이미지 복사에 필요한 데이터 가져옴
            val contentResolver = context.contentResolver ?: return null
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            if (inputStream != null) {
                //가져온 데이터를 새로 만든 경로로 내보냄
                val outputStream = FileOutputStream(file)
                val buf = ByteArray(1024)
                var len: Int
                while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
                outputStream.close()
                inputStream.close()
            }else{
                Log.d("로그--AddPlace path", "inputStream == null")
                return null
            }
        } catch (e: IOException){
            Log.d("로그--AddPlace ERROR path", "${e.printStackTrace()}")
            return null
        }
        return file.absolutePath
    }


    private fun postAddPlace(image: MultipartBody.Part?, data: HashMap<String, RequestBody>){
        if (!NetworkManager(applicationContext).checkNetworkState()) {
            Toast.makeText(applicationContext, "네트워트 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        //val call = RetrofitClientPlace.serviceApiPlace.placeRegister(image, data)
        val call = RetrofitClientPlace.serviceApiPlace.placeRegister(image)
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