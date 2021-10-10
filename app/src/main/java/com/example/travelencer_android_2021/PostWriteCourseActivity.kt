package com.example.travelencer_android_2021

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PostWriteCourseAdapter
import com.example.travelencer_android_2021.databinding.ActivityPostWriteCourseBinding
import kotlinx.android.synthetic.main.activity_feed_course_detail.*
import java.util.*

class PostWriteCourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWriteCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteCourseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //현재 날짜, 시간
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) //JANUARY == 0 임
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        // 날짜 설정
        binding.btnPostWriteCourseDate.setOnClickListener {
            val dateListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                binding.tvPostWriteCourseDate.text = "${y} ${m+1} ${d}"
            }
            val datePicker = DatePickerDialog(this, R.style.DialogTheme, dateListener, year, month, day)
            datePicker.show()
        }
        // 텍스트뷰 누를 때도 날짜 설정
        binding.tvPostWriteCourseDate.setOnClickListener {
            binding.btnPostWriteCourseDate.callOnClick()
        }

        // 시간 설정
        binding.btnPostWriteCourseTime.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { _, h, m ->
                var noon = "error"
                var noonH = 0
                if(h<12){
                    noon = "오전"
                    noonH = h
                }
                else if(h>=12){
                    noon = "오후"
                    noonH = h-12
                }
                binding.tvPostWriteCourseTime.text = "${noon} ${noonH}시 ${m}분"
            }
            val timePicker = TimePickerDialog(this, R.style.DialogTheme, timeListener, hour, minute, true)
            timePicker.show()
        }
        // 텍스트뷰 누를 때도 시간 설정
        binding.tvPostWriteCourseTime.setOnClickListener {
            binding.btnPostWriteCourseTime.callOnClick()
        }

        // 리사이클러뷰 매니저 설정
        val layoutManager = LinearLayoutManager(this@PostWriteCourseActivity)
        binding.rcCourseSpot.layoutManager = layoutManager
        // 리아시클러뷰에 어댑터 달기
        val courseAdapter = PostWriteCourseAdapter()
        binding.rcCourseSpot.adapter = courseAdapter

        // <코스 스팟 추가> 버튼 클릭
        binding.btnPostWriteAddCourseSpot.setOnClickListener {
            if (binding.tvPostWriteCourseDate.text.contains("00") || binding.etPostWriteCourseName.text.toString() == "") {
                Toast.makeText(applicationContext, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            courseAdapter.courseName.add(binding.etPostWriteCourseName.text.toString())
            val date = binding.tvPostWriteCourseDate.text.toString() + binding.tvPostWriteCourseTime.text.toString()
            courseAdapter.courseDate.add(date)

            // 데이터 변경 알림
            courseAdapter.notifyDataSetChanged()

            // 장소 지우기
            binding.etPostWriteCourseName.setText("")
        }

        // <입력 완료> 버튼 클릭
        binding.btnPostWriteCourseDone.setOnClickListener {
            val outIntent = Intent(applicationContext, PostWriteActivity::class.java)
            outIntent.putStringArrayListExtra("courseName", courseAdapter.courseName)
            outIntent.putStringArrayListExtra("courseDate", courseAdapter.courseDate)
            setResult(Activity.RESULT_OK, outIntent)
            finish()
        }


        // 뒤로 가기 이미지
        binding.ivBack.setOnClickListener{
            // 코스 추가 취소 알림창 띄우기
            val alert = AlertDialog.Builder(this@PostWriteCourseActivity)
            alert.setTitle("취소 확인")
            alert.setMessage("코스 추가를 취소하시겠습니까?")
            // <네> 버튼 누르면
            alert.setPositiveButton("네") { _, _ ->
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            // <아니오> 버튼 누르면 아무 일도 없음
            alert.setNegativeButton("아니오", null)
            alert.show()
        }

    }

    override fun onBackPressed() {
        binding.ivBack.callOnClick()
    }
}