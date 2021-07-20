package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceBinding

class AddPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //TODO : placeLoc에 대한 코드도 placeName처럼 추가하기
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val resultIntent = Intent(this, PostWriteActivity::class.java)
                val placeName = result.data?.getStringExtra(codePlaceName)
                val placeLoc = result.data?.getStringExtra(codePlaceLoc)
                resultIntent.putExtra(codePlaceName, placeName)
                resultIntent.putExtra(codePlaceLoc, placeLoc)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        binding.btnPlaceRegisterNext.setOnClickListener {
            val intent = Intent(this, AddPNCActivity::class.java)
            intent.putExtra(codePlaceName, binding.etPlaceRegisterName.text.toString())
            intent.putExtra(codePlaceLoc, "주소도 무슨시")
            intent.putExtra("from", "add")
            resultLauncher.launch(intent)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}