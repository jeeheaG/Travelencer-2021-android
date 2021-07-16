package com.example.travelencer_android_2021

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPostWriteCourseBinding
import java.sql.Time
import java.time.Clock
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
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH) //JANUARY == 0 임
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var hour = calendar.get(Calendar.HOUR)
        var minute = calendar.get(Calendar.MINUTE)


        binding.btnPostWriteCourseDate.setOnClickListener {
            val dateListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                binding.tvPostWriteCourseDate.text = "${y} ${m+1} ${d}"
            }
            val datePicker = DatePickerDialog(this, dateListener, year, month, day)
            datePicker.show()
        }

        binding.btnPostWriteCourseTime.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { view, h, m ->
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
            val timePicker = TimePickerDialog(this, timeListener, hour, minute, true)
            timePicker.show()
        }



        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}