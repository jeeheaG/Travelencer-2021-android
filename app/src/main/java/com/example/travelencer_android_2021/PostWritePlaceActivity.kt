package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityPostWritePlaceBinding

class PostWritePlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWritePlaceBinding
    //val finishPostWritePlace = "finish_post_write_place"
    //lateinit var _PostWritePlaceActivity: Activity
    private val howSearch = "search"
    private val howAdd = "add"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //_PostWritePlaceActivity = this
        binding = ActivityPostWritePlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = Intent(this, PostWriteActivity::class.java)

        binding.btnPlaceRegister.setOnClickListener {
            //val intent = Intent(this, AddPlaceActivity::class.java)
            intent.putExtra("how", howSearch)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.btnPlaceSearch.setOnClickListener {
            //val intent = Intent(this, PostWritePlaceSearchActivity::class.java)
            intent.putExtra("how", howAdd)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(resultCode == RESULT_OK){
//
//        }
//    }
}