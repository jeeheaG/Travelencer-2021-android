package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.travelencer_android_2021.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

private const val NO_LOGIN = "X"

// 홈 액티비티
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    var login = "X"

    // 뒤로가기 연속 클릭 대기 시간
    private var mBackWait : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 로그인 여부 불러오기
        login = applicationContext.getSharedPreferences("login", Context.MODE_PRIVATE).getString("login", NO_LOGIN).toString()

        val loginResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) login = data.getStringExtra("login").toString()
                Log.d("mmm", login)
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
            if (login == NO_LOGIN) {
                Toast.makeText(applicationContext, "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setNavi(R.id.settingFragment)
        }

        // 사람모양 이미지 선택 > QR 액티비티로 이동
        binding.imgQR.setOnClickListener {
            if (login == NO_LOGIN) {
                Toast.makeText(applicationContext, "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this@MainActivity, QRActivity::class.java)
            intent.putExtra("login", login)
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
            if (login == NO_LOGIN) {
                Toast.makeText(applicationContext, "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setNavi(R.id.postBlogFragment)
        }

        //이 화면으로 오면 필터 설정이 해제됨
        val pref = this?.getSharedPreferences("pref", 0)
        val edit = pref?.edit()
        edit?.putBoolean("placeFiltered", false)
        edit?.apply()

    }

    private fun setNavi(fregmentId : Int) {
        val intent = Intent(this@MainActivity, NaviActivity::class.java)
        intent.putExtra("selectFragId", fregmentId)
        intent.putExtra("login", login)
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