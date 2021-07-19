package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityAddPNCBinding

class AddPNCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPNCBinding
    private val finishPostWritePlace = "finish_post_write_place"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPNCBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val placeName = intent.getStringExtra("placeName")
        val placeLoc = intent.getStringExtra("placeLoc")

        //val _PostWritePlaceActivity: PostWritePlaceActivity = PostWritePlaceActivity._PostWritePlaceActivity

        binding.btnPlaceRegisterDone.setOnClickListener {
            val resultIntent = Intent(this, PostWritePlaceSearchActivity::class.java)
            //resultIntent.putExtra(finishPostWritePlace, true)
            resultIntent.putExtra("placeName", placeName)
            resultIntent.putExtra("placeLoc", placeLoc)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

//            val intent = Intent()
//            startActivity(intent)
//            finish()
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}