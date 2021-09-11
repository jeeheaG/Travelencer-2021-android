package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.travelencer_android_2021.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

// 홈 액티비티
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var uid = -1

    // 뒤로가기 연속 클릭 대기 시간
    private var mBackWait : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // uid 받기
        uid = intent.getIntExtra("uid", -1)

        val loginResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) uid = data.getIntExtra("uid", -1)
            }
        }

        // <로그인 & 회원가입> 버튼 클릭
        binding.btnLoingAndRegister.setOnClickListener {
            // LoginActivity로 이동해서 uid 받아오기
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            loginResultLauncher.launch(intent)
        }

        // 설정 이미지 클릭
        binding.imgSetting.setOnClickListener {
            setNavi(R.id.settingFragment)
        }

        // 사람모양 이미지 선택 > QR 액티비티로 이동
        binding.imgQR.setOnClickListener {
            val intent = Intent(this@MainActivity, QRActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }

        // <여행지 검색> 클릭
        binding.btnSearchPlace.setOnClickListener {
            setNavi(R.id.placeMainFragment)
        }

        // <여행 피드> 클릭
        binding.btnFeed.setOnClickListener {
            setNavi(R.id.feedFragment)
        }

        // <나의 여행 일지> 클릭
        binding.btnMyPost.setOnClickListener {
            setNavi(R.id.postBlogFragment)
        }

    }

    private fun setNavi(fregmentId : Int) {
        val intent = Intent(this@MainActivity, NaviActivity::class.java)
        intent.putExtra("selectFragId", fregmentId)
        intent.putExtra("uid", uid)
        startActivity(intent)
        finish()
    }

    // 2초 내애 뒤로가기 버튼을 2번 누르면 종료
    override fun onBackPressed() {
        if(System.currentTimeMillis() - mBackWait >= 2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this@MainActivity,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        }
        else finish()
    }

}