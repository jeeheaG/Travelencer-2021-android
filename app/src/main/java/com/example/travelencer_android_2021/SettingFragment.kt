package com.example.travelencer_android_2021

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.travelencer_android_2021.api.RetrofitClient
import com.example.travelencer_android_2021.data.SettingData
import com.example.travelencer_android_2021.data.SettingResponse
import com.example.travelencer_android_2021.data.UserRewiteData
import com.example.travelencer_android_2021.data.UserRewiteResponse
import com.example.travelencer_android_2021.databinding.FragmentSettingBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val PICK_FROM_ALBUM = 0
private const val IMAGE_CAPTURE = 1

// 설정 프레그먼트
class SettingFragment : Fragment() {
    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private var uri : Uri? = null                   // 이미지 파일 경로
    private lateinit var currentPhotoPath : String  //
    private var uid = -1                            // uid 값
    private var laseSelect = "변경 없음"             // 마지막으로 프로필 설정한 방법
    private lateinit var profileBitmap : Bitmap     // 서버에 저장할 사진

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        // uid 받기
        val bundle = arguments
        if (bundle != null) uid = bundle.getInt("uid", -1)
        // 사용자 정보 받아와서 설정하기
        if (uid != -1) startSetting(SettingData(uid))

        // 동그란 이미지
        binding.imgProfile.background = ShapeDrawable(OvalShape()).apply {
            paint.color = Color.WHITE
        }
        binding.imgProfile.clipToOutline = true //안드로이드 버전 5(롤리팝)이상에서만 적용

        // <프로필 사진 변경> 버튼 클릭하면 갤러리에서 사진 가져오기
        binding.btnChangeProPic.setOnClickListener {
            val typeArr = arrayOf("카메라", "갤러리", "삭제")
            AlertDialog.Builder(context).setItems(typeArr) { _, position ->
                when (typeArr[position]) {
                    "카메라" -> takePhoto()
                    "갤러리" -> getFromAlbum()
                    "삭제" -> imgProfile.setImageResource(R.drawable.ic_user_gray)
                }
                laseSelect = typeArr[position]
            }.show()
        }

        // <수정 완료> 버튼 클릭
        binding.btnSetting.setOnClickListener {
            // 이름, 소개글
            val name = binding.editName.text.toString()
            val info = binding.editInfo.text.toString()

            // 프로필 변경 확인
            when (laseSelect) {
                "카메라", "갤러리" -> {
                    // 크롭한 프로필 사진 갤러리에 저장하기
                    createImageFile()           // 파일 만들고
                    savePhoto(profileBitmap)    // 갤러리에 저장
                    // 서버에 보낼 프로필 사진
                    val file = File(currentPhotoPath)
                    val photoBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
                    val proPic = MultipartBody.Part.createFormData("proPic", file.name, photoBody)
                    // 서버에 보내기
                    changeSetting(UserRewiteData(uid, null, name, info))
                }
                "삭제" -> {
                    // 서버에 보내기
                    changeSetting(UserRewiteData(uid, null, name, info))
                }
                // 변경 없음
                else -> {
                    // 서버에 보내기
                    changeSetting(UserRewiteData(uid, null, name, info))
                }
            }
        }

        // <로그아웃> 버튼 클릭
        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        // <챗봇 연결> 버튼 클릭
        binding.btnChatBot.setOnClickListener {
            val chatBotIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bot.dialogflow.com/b403c3aa-6dbe-4c9a-83a5-6ce39e60148c"))
            startActivity(chatBotIntent)
        }

        // 뒤로가기 이미지 클릭
        binding.imgBack.setOnClickListener {

        }

        // <서비스 이용 약관> 텍스트뷰 클릭
        binding.tvTOS.setOnClickListener {
            // TOSActivity로 이동하기
            val intent = Intent(activity, TOSActivity::class.java)
            startActivity(intent)
        }

        // <개인정보 보호정책> 텍스트뷰 클릭
        binding.tvPP.setOnClickListener {
            // PPActivity로 이동하기
            val intent = Intent(activity, PPActivity::class.java)
            startActivity(intent)
        }

        // <회원 탈퇴> 텍스트뷰 클릭
        binding.tvWithDraw.setOnClickListener {
            AlertDialog.Builder(context)
                    .setMessage("정말 탈퇴하시겠습니까?")
                    .setPositiveButton("네") { _, _ ->
                        Toast.makeText(context, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("아니오") { _, _ ->
                        Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .show()
        }
        return binding.root
    }

    // 설정 DB 연결(사용자 설정 정보 가져오기)
    private fun startSetting(data : SettingData) {
        val call = RetrofitClient.serviceApiSetting.setSetting(data)
        call.enqueue(object : retrofit2.Callback<SettingResponse> {
            // 응답 성공 시
            override fun onResponse(call: Call<SettingResponse>, response: Response<SettingResponse>) {
                if (response.isSuccessful) {
                    val result : SettingResponse = response.body()!!
                    // 설정 변경하기
                    if (result.code == 200) {
                        setting(result.proPic, result.name, result.info ?: "안녕하세요")
                    }
                }
            }
            // 응답 실패 시
            override fun onFailure(call: Call<SettingResponse>, t: Throwable) {
                Toast.makeText(context, "설정 에러 발생 ${t.message}", Toast.LENGTH_LONG).show()
                Log.d("mmm 설정 fail", t.message.toString())
            }
        })
    }

    // 설정 변경 DB 연결(사용자가 수정한 정보 보냐기)
    // proPic : MultipartBody.Part, UID : MultipartBody.Part, name : MultipartBody.Part, info : MultipartBody.Part, data: HashMap<String, RequestBody>
    private fun changeSetting(data : UserRewiteData) {
        val call = RetrofitClient.serviceApiUser.userRewrite(data)
        call.enqueue(object : retrofit2.Callback<UserRewiteResponse> {
            // 응답 성공 시
            override fun onResponse(call: Call<UserRewiteResponse>, response: Response<UserRewiteResponse>) {
                if (response.isSuccessful) {
                    val result : UserRewiteResponse = response.body()!!
                    // 설정 변경하기
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
            // 응답 실패 시
            override fun onFailure(call: Call<UserRewiteResponse>, t: Throwable) {
                Toast.makeText(context, "설정 변경 에러 발생 ${t.message}", Toast.LENGTH_LONG).show()
                Log.d("mmm 설정 변경 fail", t.message.toString())
            }
        })
    }

    private fun exByte(list: ArrayList<Double>): ByteArray {
        val list2: MutableList<Byte> = mutableListOf()
        for (i in 0 until list.size) {
            list2.add(list[i].toInt().toByte())
        }
        return list2.toByteArray()
    }
    private fun convertBitmap(input: ArrayList<Double>): Bitmap {
        val arr = exByte(input)
        try {
            return BitmapFactory.decodeByteArray(arr, 0, arr.size)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    // 설정하기
    private fun setting(proPic: ArrayList<Double>?, name: String, info: String) {
        // 프로필 사진
        if (proPic == null) imgProfile.setImageResource(R.drawable.ic_user_gray)
        else {
            val bitmap = convertBitmap(proPic)
            imgProfile.setImageBitmap(bitmap)
        }
        // 닉네임
        binding.editName.setText(name)
        // 계정 소개글
        binding.editInfo.setText(info)
    }

    // 카메라(사진 찍기)
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                val photoFile : File? = try {
                    createImageFile()
                } catch (e : IOException) { null }
                photoFile?.also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                            context as NaviActivity, "com.example.travelencer_android_2021.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, IMAGE_CAPTURE)
                }
            }
        }
    }

    // 갤러리 이미지 선택해서 가져오기
    private fun getFromAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"     // 모든 이미지
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    // 이미지 파일 생성
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())        // 이미지 파일 이름
        val storeageDir : File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)     // 스트리지 디렉토리 경로

        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storeageDir).apply { currentPhotoPath = absolutePath}
    }

    // 사진 크롭하기
    private fun cropImage(uri: Uri?) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)  // 크롭 위한 가이드 열어서 크롭할 이미지 받아오기
                .setCropShape(CropImageView.CropShape.RECTANGLE)            // 사각형으로 자르기
                .start(activity as NaviActivity, this@SettingFragment)
    }

    // 갤러리에 저장하는 메소드
    private fun savePhoto(bitmap: Bitmap) {
        // 사진 폴더로 저장하기 위한 경로 선언
        //val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Travelencer/"  // /storage/emulated/0/Pictures/Travelencer/
        val folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Travelencer/"
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 이미지 파일 이름
        val fileName = "Travelencer_profile_${timestamp}.jpeg"
        val folder = File(folderPath)
        // 해당 경로의 폴더가 존재하지 않으면 해당 경로에 폴더 생성
        if (!folder.isDirectory) folder.mkdir()

        // 최종 저장
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)   // 비트맵 압축
    }

    // 읍답 받은 액티비티
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        when(requestCode) {
            // 갤러리
            PICK_FROM_ALBUM -> {
                if (resultCode == Activity.RESULT_OK) { // && Build.VERSION.SDK_INT >= 19
                    uri = data?.data    // 선택한 이미지의 주소
                    // 사용자가 이미지를 선택했으면(null이 아니면) 크롭하기
                    if (uri != null) cropImage(uri)
                }
                else if (requestCode == Activity.RESULT_CANCELED) laseSelect = "변경 없음"

            }
            // 카메라(사진 찍기)
            IMAGE_CAPTURE -> {
                // 올바르게 저장됐다면
                if (resultCode == Activity.RESULT_OK) {
                    // 사진 받아오기
                    val file = File(currentPhotoPath)   // createImageFile 실행 이후라 값이 들어와 있는 상태

                    // 버전에 따라 다른 비트맵
                    // 안드로이드 파이 버전보다 낮은 경우 (getBitmap)
                    profileBitmap = if (Build.VERSION.SDK_INT < 28)
                        MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.fromFile(file))
                    // 안드로이드 파이 버전보다 높은 경우 (ImageDecoder)
                    else {
                        val decode = ImageDecoder.createSource(activity?.contentResolver!!, Uri.fromFile(file))
                        ImageDecoder.decodeBitmap(decode)
                    }
                    uri = Uri.fromFile(file)
                    // uri가 null이 아니면 크롭하기
                    if (uri != null) cropImage(uri)
                }
                else if (resultCode == Activity.RESULT_CANCELED) laseSelect = "변경 없음"
            }
            // 크롭해서 프로필 사진 설정하기
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        // 이미지 파일 읽어와서 설정하기
                        profileBitmap = BitmapFactory.decodeStream(activity?.contentResolver!!.openInputStream(result.uri!!))
                        imgProfile.setImageBitmap(profileBitmap)
                    }
                }
                else if (result == null) laseSelect = "변경 없음"
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}