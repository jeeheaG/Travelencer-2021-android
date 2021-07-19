package com.example.travelencer_android_2021

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.travelencer_android_2021.databinding.FragmentSettingBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val PICK_FROM_ALBUM = 0
private const val IMAGE_CAPTURE = 1

// 설정 프레그먼트
class SettingFragment : Fragment() {
    private var mBinding : FragmentSettingBinding? = null
    var uri : Uri? = null   // 이미지 파일 경로

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
            //getFromAlbum()
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
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE)
            }
        }
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
                    val imageBitmap = data?.extras!!.get("data") as Bitmap
                    if (imageBitmap != null) {
                        // 비트맵을 uri 형태로 바꾸기
                        val bytes = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        val path = MediaStore.Images.Media.insertImage(context?.getContentResolver(), imageBitmap, "temp", null)
                        uri = Uri.parse(path)
                    }
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

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}