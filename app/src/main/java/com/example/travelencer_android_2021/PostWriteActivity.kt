package com.example.travelencer_android_2021

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPostWriteBinding
import java.util.*

//뷰바인딩 사용

class PostWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWriteBinding

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

        binding.btnPostWriteAddPlace.setOnClickListener {
            val intent = Intent(this, PostWritePlaceActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }

    }
}