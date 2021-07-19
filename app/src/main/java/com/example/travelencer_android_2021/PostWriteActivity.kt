package com.example.travelencer_android_2021

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.travelencer_android_2021.course.CourseMaker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.FeedFoodAdapter
import com.example.travelencer_android_2021.adapter.PostWritePlaceAdapter
import com.example.travelencer_android_2021.databinding.ActivityPostWriteBinding
import com.example.travelencer_android_2021.model.ModelCourseSpot
import java.util.*

//뷰바인딩 사용
//TODO : 다른 액티비티로 이동해서 입력한 정보를 받아서 글 작성 화면으로 돌아올 때
// 작성하던 글을 유지하면서 방금 받은 정보를 추가해서 띄우려면.. 작성하던 내용을 sharedPreference에 저장했다가 불러와야하나?
// 아니면 onActivityResult?

class PostWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var placeName: String
        var placeLoc: String

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

        binding.rvPostWritePlaceList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPostWritePlaceList.setHasFixedSize(true)

        var placeList = arrayListOf<ModelCourseSpot>()
        //val feedFoodAdapter = FeedFoodAdapter()

        //입력 페이지들
        //여행지 추가 부분
        val placeAddResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                Log.d("로그pAddResultLauncher", "작동함 result.resultCode == Activity.RESULT_OK")
                val data = result.data
                Log.d("로그pAddResultLauncher","data : ${data}")
            }
        }
        val placeSearchResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                Log.d("로그pSearchResultLauncher", "작동함 result.resultCode == Activity.RESULT_OK")
                val data = result.data
                Log.d("로그pSearchResultLauncher","data : ${data}")
                if (data != null) {
                    placeName = data.getStringExtra("placeName").toString()
                    placeLoc = data.getStringExtra("placeLoc").toString()
                    placeList.add(ModelCourseSpot(placeName, placeLoc))
                    binding.rvPostWritePlaceList.adapter = PostWritePlaceAdapter(placeList, this)
                }
            }
        }



        val placeResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                Log.d("로그 postWrite의 Place입력받기", "작동함 result.resultCode == Activity.RESULT_OK")
                val howAddPlace = result.data?.getStringExtra("how")
                when(howAddPlace) {
                    "add" -> {
                        val intent = Intent(this, AddPlaceActivity::class.java)
                        placeAddResultLauncher.launch(intent)
                    }
                    "search" -> {
                        val intent = Intent(this, PostWritePlaceSearchActivity::class.java)
                        placeSearchResultLauncher.launch(intent)
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

        binding.btnPostWriteAddPlace.setOnClickListener {
            val intent = Intent(this, PostWritePlaceActivity::class.java)
            placeResultLauncher.launch(intent)
//            startActivityForResult(intent, requestCodePlace)
        }

        // <코스 추가> 버튼 클릭
        binding.btnPostWriteAddCourse.setOnClickListener {
            val intent = Intent(this, PostWriteCourseActivity::class.java)
            addCourseResultLauncher.launch(intent)
        }

        //TODO : 사진 입력페이지

        binding.btnPostWritePost.setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            startActivity(intent)
        }


        binding.ivBack.setOnClickListener{
            finish()
        }

    }
}