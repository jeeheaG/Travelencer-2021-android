package com.example.travelencer_android_2021

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private const val PICK_FROM_ALBUM = 0
private const val IMAGE_CAPTURE = 1
private const val TAG = "mmm"
private const val NO_LOGIN = "X"

// 설정 프레그먼트
class SettingFragment : Fragment() {
    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private var uri : Uri? = null                   // 이미지 파일 경로
    private lateinit var currentPhotoPath : String  // 사진 절대경로
    private var laseSelect = "변경 없음"             // 마지막으로 프로필 설정한 방법
    private lateinit var profileBitmap : Bitmap     // 서버에 저장할 사진

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef : StorageReference

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        storage = Firebase.storage
        storageRef = storage.reference

        // TODO : 사용자 정보 받아와서 설정하기
        val uid : String = (Firebase.auth.uid ?: activity?.getSharedPreferences("uid", Context.MODE_PRIVATE)!!.getString("uid", "-1")) as String
        if (uid != "-1") {
            getSetting(uid)
            getSettingProPic(uid)
        }

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
                    val savedFileName = savePhoto(profileBitmap)    // 갤러리에 저장, 저장한 파일 이름 리턴받기
                    // 서버에 보낼 프로필 사진
                    val proPicFile = File(savedFileName)
                    // 수정하기
                    setSettingProPic(uid, proPicFile)
                    setSetting(uid, name, info)
                }
                "삭제" -> {
                    // 수정하기
                    setDeleteProPic(uid)
                    setSetting(uid, name, info)
                }
                // 변경 없음
                else -> {
                    // 수정하기
                    setSetting(uid, name, info)
                }
            }
        }

        // <로그아웃> 버튼 클릭
        binding.btnLogout.setOnClickListener {
            // uid 삭제
            val pref = context?.getSharedPreferences("login", Context.MODE_PRIVATE)!!
            val edit = pref.edit()
            edit.putString("login", NO_LOGIN).apply()

            FirebaseAuth.getInstance().signOut()

            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            // MainActivity로 이동
            val intent = Intent(context, MainActivity::class.java)
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

    // 설정 정보 가져오기
    private fun getSetting(uid : String) {
        val db = Firebase.firestore
        val docRef = db.collection("userT").document(uid)

        // 데이터 가져오기(프로필 사진 제외)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val map = document.data as HashMap<String, Any>
//                        val proPic : ArrayList<Double>? = map["proPic"] as ArrayList<Double>?
                        val name : String = map["name"] as String
                        val info : String = (map["info"] ?: "안녕하세요") as String

                        // 설정하기
//                        // 프로필 사진
//                        if (proPic == null) imgProfile.setImageResource(R.drawable.ic_user_gray)
//                        else {
//                            val bitmap = convertBitmap(proPic)
//                            imgProfile.setImageBitmap(bitmap)
//                        }
                        // 닉네임
                        binding.editName.setText(name)
                        // 계정 소개글
                        binding.editInfo.setText(info)
                    } else {
                        Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "설정 화면을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "get failed with ", exception)
                }
    }
    // 프로필 사진 가져오기
    private fun getSettingProPic(uid : String) {
        val proPicFile = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/prifile_img")!!
        if (!proPicFile.isDirectory) proPicFile.mkdir()

        // 이미지 다운로드해서 가져오기
        storageRef.child("proPic_$uid").downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(activity?.applicationContext!!)
                            .load(uri)
                            .error(R.drawable.ic_user_gray)                  // 오류 시 이미지
                            .apply(RequestOptions().centerCrop())
                            .into(imgProfile)
                    // imgProfile.setImageURI(uri)
                }
    }

    // <수정완료> 버튼 클릭 > 설정 정보 수정하기
   private fun setSetting(uid : String, name : String, info : String) {
        val firestore = Firebase.firestore
        val map= mutableMapOf<String,Any>()
        map["name"] = name
        map["info"] = info
        firestore.collection("userT").document(uid).update(map)
                .addOnCompleteListener {
                    if(it.isSuccessful) Toast.makeText(context, "수정 완료하였습니다.", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

    }
    // 프로필 사진 변경/수정하기
    private fun setSettingProPic(uid : String, proPicFile : File) {
        // 기존 프로필 사진 지우기
        setDeleteProPic(uid)

        val mountainsRef = storageRef.child("proPic_$uid")
        val stream = FileInputStream(proPicFile)
        val uploadTask = mountainsRef.putStream(stream)
        uploadTask.addOnFailureListener { Toast.makeText(context, "프로필 사진 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show() }
    }
    // 프로필 사진 삭제하기
    private fun setDeleteProPic(uid : String) {
        val deserRef = storageRef.child("proPic_$uid")
        deserRef.delete().addOnFailureListener { Toast.makeText(context, "프로필 사진 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show() }
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
    private fun savePhoto(bitmap: Bitmap) : String{
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

        return folderPath + fileName // 저장한 파일 이름
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
                    uri = Uri.fromFile(file) // 찍은 사진 uri 저장
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