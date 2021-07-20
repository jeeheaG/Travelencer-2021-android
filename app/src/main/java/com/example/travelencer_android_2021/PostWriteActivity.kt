package com.example.travelencer_android_2021

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.travelencer_android_2021.course.CourseMaker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostWritePhotoUriAdapter
import com.example.travelencer_android_2021.adapter.PostWritePlaceAdapter
import com.example.travelencer_android_2021.databinding.ActivityPostWriteBinding
import com.example.travelencer_android_2021.model.ModelCourseSpot
import java.util.*

//뷰바인딩 사용

class PostWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWriteBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //현재 날짜
        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH) //JANUARY == 0 임
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        //작성일
        binding.tvPostWritePostDate.text = "작성일 ${year} ${month+1} ${day}"

        //여행 기간 입력
        binding.btnPostWriteStartDate.setOnClickListener {
            val startDateListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                binding.tvPostWriteStartDate.text = "${y} ${m+1} ${d}"
            }
            val startDatePicker = DatePickerDialog(this, startDateListener, year, month, day)
            startDatePicker.show()
        }
        binding.btnPostWriteEndDate.setOnClickListener {
            val endDateListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                binding.tvPostWriteEndDate.text = "${y} ${m+1} ${d}"
            }
            val endDatePicker = DatePickerDialog(this, endDateListener, year, month, day)
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
                    placeList.add(ModelCourseSpot(placeName, placeLoc))
                    binding.rvPostWritePlaceList.adapter = PostWritePlaceAdapter(placeList, this)
                }
            }
        }

        val placeHowResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val howAddPlace = result.data?.getStringExtra("how")
                when(howAddPlace) {
                    "add" -> {
                        val intent = Intent(this, AddPlaceActivity::class.java)
                        placeResultLauncher.launch(intent)
                    }
                    "search" -> {
                        val intent = Intent(this, PostWritePlaceSearchActivity::class.java)
                        placeResultLauncher.launch(intent)
                    }
                }
            }
        }

        // 코스 추가 버튼 클릭 시 동작
        val addCourseResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val course = result.data!!.getStringArrayListExtra("course")   // 코스 정보 받기
                binding.llPostWriteCourse.removeAllViews()                            // 이전 코스 지우기
                CourseMaker().makeCourse(course!!, binding.llPostWriteCourse, applicationContext)   // 코스 만들기
            }
        }

        // 사진 추가 launcher, 사진 목록 리스트
        var photoList = arrayListOf<Uri>()

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
                binding.rvPostWritePhotoList.adapter = PostWritePhotoUriAdapter(photoList, this)
            }
        }

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
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addPhotoResultLauncher.launch(intent)
        }

        // <등록 하기> 버튼 클릭
        binding.btnPostWritePost.setOnClickListener {
            // TODO : DB : DB로 게시글 데이터 보내기
            val intent = Intent(this, PostDetailActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.ivBack.setOnClickListener{
            finish()
        }

    }
}