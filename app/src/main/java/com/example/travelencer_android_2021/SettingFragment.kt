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
import com.example.travelencer_android_2021.databinding.FragmentSettingBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val PICK_FROM_ALBUM = 0
private const val IMAGE_CAPTURE = 1

// 설정 프레그먼트
class SettingFragment : Fragment() {
    private var mBinding : FragmentSettingBinding? = null
    var uri : Uri? = null   // 이미지 파일 경로
    lateinit var currentPhotoPath : String // 사진 경로 값

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentSettingBinding.inflate(inflater, container, false)
        mBinding = binding

        // 동그란 이미지
        val imgProfile = binding.imgProfile
        imgProfile.background = ShapeDrawable(OvalShape()).apply {
            paint.color = Color.WHITE
        }

        imgProfile.clipToOutline = true //안드로이드 버전 5(롤리팝)이상에서만 적용

        // <프로필 사진 변경> 버튼 클릭하면 갤러리에서 사진 가져오기
        binding.btnLoingAndRegister.setOnClickListener {
            val typeArr = arrayOf("사진 찍기", "기존 사진 선택")
            AlertDialog.Builder(context).setItems(typeArr) { dialog, position ->
                when (typeArr[position]) {
                    "사진 찍기" -> takePhoto()
                    "기존 사진 선택" -> getFromAlbum()
                }
            }.show()
        }

        // <수정 완료> 버튼 클릭
        binding.btnSetting.setOnClickListener {
            Toast.makeText(context, "수정 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // <로그아웃> 버튼 클릭
        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // <로그아웃> 버튼 클릭
        binding.btnChatBot.setOnClickListener {
            Toast.makeText(context, "챗봇 준비중", Toast.LENGTH_SHORT).show()
        }

        // 뒤로가기 이미지 클릭
        binding.imgBack.setOnClickListener {

        }

        // <서비스 이용 약관> 텍스트뷰 클릭
        binding.tvTOS.setOnClickListener {
            // TOSActivity로 이동하기
            var intent = Intent(activity, TOSActivity::class.java)
            startActivity(intent)
        }

        // <개인정보 보호정책> 텍스트뷰 클릭
        binding.tvPP.setOnClickListener {
            // PPActivity로 이동하기
            var intent = Intent(activity, PPActivity::class.java)
            startActivity(intent)
        }

        // <회원 탈퇴> 텍스트뷰 클릭
        binding.tvWithDraw.setOnClickListener {
            AlertDialog.Builder(context)
                    .setMessage("정말 탈퇴하시겠습니까?")
                    .setPositiveButton("네") { dialog, which ->
                        Toast.makeText(context, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("아니오") { dialog, which ->
                        Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .show()
        }

        return mBinding?.root
    }

    // 사진 찍기
    fun takePhoto() {
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

    // 이미지 파일 생성
    private fun createImageFile(): File? {
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 이미지 파일 이름
        val storeageDir : File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)  // 스트리지 디렉토리 경로

        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storeageDir).apply { currentPhotoPath = absolutePath}
    }

    // 갤러리 이미지 선택해서 가져오기
    fun getFromAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"     // 모든 이미지
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    // 사진 크롭하기
    private fun cropImage(uri: Uri?) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)  // 크롭 위한 가이드 열어서 크롭할 이미지 받아오기
                .setCropShape(CropImageView.CropShape.RECTANGLE)            // 사각형으로 자르기
                .start(activity as NaviActivity, this@SettingFragment)
    }

    // 읍답 받은 액티비티
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        when(requestCode) {
            // 기존 사진 선택
            PICK_FROM_ALBUM -> {
                if (resultCode == Activity.RESULT_OK && Build.VERSION.SDK_INT >= 19) {
                    uri = data?.data    // 선택한 이미지의 주소
                    // 사용자가 이미지를 선택했으면(null이 아니면) 크롭하기
                    if (uri != null) cropImage(uri)
                }
            }
            // 사진 찍기
            IMAGE_CAPTURE -> {
                // 올바르게 저장됐다면
                if (resultCode == Activity.RESULT_OK) {
                    // 사진 받아오기
                    val bitmap : Bitmap
                    val file = File(currentPhotoPath)   // createImageFile 실행 이후라 값이 들어와 있는 상태

                    // 버전에 따라 다른 비트맵
                    // 안드로이드 파이 버전보다 낮은 경우 (getBitmap)
                    if (Build.VERSION.SDK_INT < 28)
                        bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.fromFile(file))
                    // 안드로이드 파이 버전보다 높은 경우 (ImageDecoder)
                    else {
                        val decode = ImageDecoder.createSource(
                                activity?.contentResolver!!,
                                Uri.fromFile(file)
                        )
                        bitmap = ImageDecoder.decodeBitmap(decode)
                    }
                    // 갤러리에 저장
                    savePhoto(bitmap)
                    uri = Uri.fromFile(file)
                    // uri가 null이 아니면 크롭하기
                    if (uri != null) cropImage(uri)
                }
            }
            // 크롭해서 프로필 사진 설정하기
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK){
                    result.uri?.let {
                        // 이미지 파일 읽어와서 설정하기
                        val bitmap = BitmapFactory.decodeStream(activity?.contentResolver!!.openInputStream(result.uri!!))
                        imgProfile.setImageBitmap(bitmap)

                    }
                }
            }
        }
    }

    // 갤러리에 저장하는 메소드(갤러리에서 확인하는데까지 시간 좀 걸림)
    private fun savePhoto(bitmap: Bitmap) {
        // 사진 폴더로 저장하기 위한 경로 선언
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Travelencer/"  // /storage/emulated/0/Pictures/Travelencer/
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 이미지 파일 이름
        val fileName = "Travelencer_profile_${timestamp}.jpeg"
        val folder = File(folderPath)
        Log.d("mmm", folderPath.toString())
        if (!folder.isDirectory) {  // 해당 경로의 폴더가 존재하지 않으면
            folder.mkdir()          // 해당 경로에 폴더 생성
        }

        // 최종 저장
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)   // 비트맵 압축
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}