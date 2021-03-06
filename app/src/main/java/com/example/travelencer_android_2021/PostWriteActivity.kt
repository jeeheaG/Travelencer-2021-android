package com.example.travelencer_android_2021

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import com.example.travelencer_android_2021.course.CourseMaker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.adapter.PhotoBitmapAdapter
import com.example.travelencer_android_2021.adapter.PostWritePlaceAdapter
import com.example.travelencer_android_2021.databinding.ActivityPostWriteBinding
import com.example.travelencer_android_2021.model.ModelCourseSpot
import com.example.travelencer_android_2021.model.ModelPostPhotoT
import com.example.travelencer_android_2021.model.ModelPostT
import com.example.travelencer_android_2021.model.ModelCourseT
import com.example.travelencer_android_2021.model.ModelPostPlaceT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//뷰바인딩 사용

class PostWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWriteBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"
    private lateinit var auth: FirebaseAuth
    var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var ModelPostT = ModelPostT()
    var ModelCourseT = ModelCourseT()
    var ModelPostPlaceT = ModelPostPlaceT()
    private var storage : FirebaseStorage? = null
    var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var imgcnt = 0 // 이미지 추가할때마다 카운트 증가, 파일 여러개인거 대비
    var photoList = arrayListOf<Uri>()
    var photoBitmapList = arrayListOf<Bitmap>()
    //lateinit var courseName : ArrayList<String>
    var courseName = arrayListOf<String>()
    //lateinit var courseDate : ArrayList<String>
    var courseDate = arrayListOf<String>()
    var placeIdList = arrayListOf<String>()
    var placeNameList = arrayListOf<String>()
    var placeLocList = arrayListOf<String>()
    //TODO: 이름, 위치, 카테고리 리스트로 받아서 각자 저장하고 컬렉션에 저장하기
    //일단 카테고리는 디폴트 관광지로 표시하기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()


        val view = binding.root
        setContentView(view)

        //현재 날짜
        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH) //JANUARY == 0 임
        var day = calendar.get(Calendar.DAY_OF_MONTH)


        //tvPostWriteNickname
        firestore?.collection("userT")?.document(auth.currentUser!!.uid).get()
                .addOnSuccessListener { doc->
                    binding.tvPostWriteNickname.text = doc?.data?.get("name").toString()
                }
        // 이미지 다운로드해서 가져오기
        var storageRef = storage?.reference?.child("user")
                ?.child("proPic_${auth.currentUser!!.uid}")
        storageRef?.downloadUrl
                ?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                            .load(uri)
                            .error(R.drawable.ic_user_gray)                  // 오류 시 이미지
                            .apply(RequestOptions().centerCrop())
                            .into(binding.ivPostWriteProfileImg)
                }

        //작성일
        binding.tvPostWritePostDate.text = "작성일 ${year} ${month+1} ${day}"

        //여행 기간 입력
        binding.btnPostWriteStartDate.setOnClickListener {
            val startDateListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                binding.tvPostWriteStartDate.text = "${y} ${m+1} ${d}"
            }
            val startDatePicker = DatePickerDialog(this, R.style.DialogTheme, startDateListener, year, month, day)
            startDatePicker.show()
        }
        binding.btnPostWriteEndDate.setOnClickListener {
            val endDateListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                binding.tvPostWriteEndDate.text = "${y} ${m+1} ${d}"
            }
            val endDatePicker = DatePickerDialog(this, R.style.DialogTheme, endDateListener, year, month, day)
            endDatePicker.show()
        }

        //여행지 목록 RecyclerView 설정
        binding.rvPostWritePlaceList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPostWritePlaceList.setHasFixedSize(true)

        //사진 목록 RecyclerView 설정
        binding.rvPostWritePhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPostWritePhotoList.setHasFixedSize(true)

        //여행지 추가 부분 launcher 2개, 추가한 여행지 목록 리스트
        var placeList = arrayListOf<ModelCourseSpot>()

        val placeResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                if (data != null) {
                    val placeName = data.getStringExtra(codePlaceName).toString()
                    val placeLoc = data.getStringExtra(codePlaceLoc).toString()
                    val placeId = data.getStringExtra("placeId").toString()
                    placeIdList.add(placeId) //선택 장소 리스트에 추가
                    placeNameList.add(placeName)
                    placeLocList.add(placeLoc)
                    placeList.add(ModelCourseSpot(placeName, placeLoc, placeId))
                    binding.rvPostWritePlaceList.adapter = PostWritePlaceAdapter(placeList, this)
                }
            }
        }

        val placeHowResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val howAddPlace = result.data?.getStringExtra("how")
                when(howAddPlace) {
                    "search" -> {
                        val searchIntent = Intent(this, PostWritePlaceSearchActivity::class.java)
                        searchIntent.putExtra("from", "search")
                        placeResultLauncher.launch(searchIntent)
                    }
                    "add" -> {
                        val addIntent = Intent(this, AddPlaceActivity::class.java)
                        addIntent.putExtra("from", "add")
                        placeResultLauncher.launch(addIntent)
                    }
                }
            }
        }

        // 코스 추가 버튼 클릭 시 동작
        val addCourseResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                courseName = result.data!!.getStringArrayListExtra("courseName") as ArrayList<String>   // 코스이름 정보 받기
                courseDate = result.data!!.getStringArrayListExtra("courseDate") as ArrayList<String>   // 코스 날짜 정보 받기
                binding.llPostWriteCourse.removeAllViews()                            // 이전 코스 지우기
                CourseMaker().makeCourse(courseName!!, binding.llPostWriteCourse, applicationContext)   // 코스 만들기

            }
        }

        //장소 DB 저장하는 함수
        fun postPlaceUpload(){
            Log.d("로그--postPlaceUpload 실행","ㅇㅇ")
            for (i in (0 until placeIdList.size)){
                ModelPostPlaceT.postId = auth?.currentUser?.uid + "_" + timeStamp
                ModelPostPlaceT.placeId = placeIdList[i]
                ModelPostPlaceT.placeName = placeNameList[i]
                ModelPostPlaceT.placeLoc = placeLocList[i]
                //db 업로드
                firestore?.collection("postPlaceT").document().set(ModelPostPlaceT)
            }
        }

        //코스 DB 저장하는 함수
        fun courseUpload(courseName:ArrayList<String>, courseDate:ArrayList<String>){
            //파베 호출문 안에 포문 넣기
            firestore?.collection("userT")?.document(auth.currentUser!!.uid)
                    .get().addOnSuccessListener { doc->
                        for(i in (0 until courseName.size)){
                            ModelCourseT.courseDate = courseDate[i]
                            ModelCourseT.coursePlaceName = courseName[i]
                            ModelCourseT.postId = auth?.currentUser?.uid + "_" + timeStamp
                            ModelCourseT.uid = auth?.currentUser?.uid
                            ModelCourseT.sequence = i
                            ModelCourseT.nickname = doc?.data?.get("name").toString()
                            firestore.collection("postCourseT").document()?.set(ModelCourseT)
                        }
                    }

        }

        // 사진 추가 launcher, 사진 목록 리스트
        //var photoList = arrayListOf<Uri>()

        val addPhotoResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            val imageData = result.data
            // 이미지를 한 개라도 선택했을 경우
            if(result.resultCode == Activity.RESULT_OK && imageData != null){
                val clipData = imageData.clipData

                // 이미지를 한 개만 선택했을 경우
                if(clipData == null){
                    val uri = imageData.data
                    uri?.let{ photoList.add(uri) }
                    val bitmap = uriToBitmapRotate(uri)
                    uri?.let{
                        // 사진 회전 정보 가져오기
                        val orientation = getOrientationOfImage(uri).toFloat()
                        // 이미지 회전하기
                        val newBitmap = getRotatedBitmap(bitmap, orientation)
                        photoBitmapList.add(newBitmap!!)
                    }
                }
                //이미지를 여러개 선택했을 경우
                else{
                    for(i in 0 until clipData.itemCount){
                        val uri = clipData.getItemAt(i).uri
                        photoList.add(uri)
                        val bitmap = uriToBitmapRotate(uri)
                        uri?.let{
                            // 사진 회전 정보 가져오기
                            val orientation = getOrientationOfImage(uri).toFloat()
                            // 이미지 회전하기
                            val newBitmap = getRotatedBitmap(bitmap, orientation)
                            photoBitmapList.add(newBitmap!!)
                        }
                    }
                }
                binding.rvPostWritePhotoList.adapter = PhotoBitmapAdapter(photoBitmapList, this)
            }
        }


        //사진 스토리지 등록 코드
        fun photoUpload(content : String, postId :String){
            for ((imgcnt, uri) in photoList.withIndex()){
                val imgFileName = "postImage_" + postId + "_"+imgcnt+"_.jpg"
                val ref = FirebaseStorage.getInstance().getReference("/post/$imgFileName")
                Log.d("사진111",imgFileName)
                ref.putFile(uri).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        val modelPostPhotoT = ModelPostPhotoT(postId,imgFileName,content)
                        Log.d("사진222",imgFileName)
                        firestore.collection("postPhotoT").document()?.set(modelPostPhotoT)
                    }
                }
            }
        }

        //사진 파이어스토어 컬렉션에 넣기


        // <여행지 추가> 버튼 클릭
        binding.btnPostWriteAddPlace.setOnClickListener {
            val intent = Intent(this, PostWritePlaceActivity::class.java)
            placeHowResultLauncher.launch(intent)
        }

        // <코스 추가> 버튼 클릭
        binding.btnPostWriteAddCourse.setOnClickListener {
            val intent = Intent(this, PostWriteCourseActivity::class.java)
            addCourseResultLauncher.launch(intent)
        }

        // <사진 추가> 버튼 클릭
        binding.btnPostWriteAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT) // 구글 갤러리 앱으로 선택
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"     // 모든 이미지
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addPhotoResultLauncher.launch(intent)
        }

        // <등록 하기> 버튼 클릭
        binding.btnPostWritePost.setOnClickListener {
            Log.d("로그 게시글 등록버튼", "누름")
            //게시글 테이블 업로드
            ModelPostT.uid = auth?.currentUser?.uid
            var postId = auth?.currentUser?.uid + "_"+timeStamp
            ModelPostT.postId = postId
            ModelPostT.updateDate = timeStamp
            ModelPostT.title = binding.tvPostWriteTitle.text.toString()
            ModelPostT.startDate = binding.tvPostWriteStartDate.text.toString()
            ModelPostT.EndDate = binding.tvPostWriteEndDate.text.toString()
            ModelPostT.content = binding.tvPostWriteWriting.text.toString()
            firestore?.collection("postT")?.document(postId)?.set(ModelPostT)
            photoUpload(binding.tvPostWriteWriting.text.toString(),postId)
            if(courseName.isNotEmpty() && courseDate.isNotEmpty()){
                courseUpload(courseName, courseDate)
            }
             //TODO : 코스 입력 안해서 여기서 오류나면 postPlaceUpload로 실행 안됨
            postPlaceUpload()
            Log.d("로그 게시글 등록 작업 실행 완", "ㅇㅇ")
            val intent = Intent(this, PostDetailActivity::class.java)
            startActivity(intent)
            finish()
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

    // 이미지 회전 정보 가져오기
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getOrientationOfImage(uri: Uri): Int {
        // uri -> inputStream
        val inputStream = contentResolver.openInputStream(uri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

        // 회전된 각도 알아내기
        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    // 이미지 회전하기
    @Throws(Exception::class)
    private fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }




}