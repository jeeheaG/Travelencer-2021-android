package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelencer_android_2021.api.RetrofitClient
import com.example.travelencer_android_2021.data.JoinResponse
import com.example.travelencer_android_2021.data.LoginData
import com.example.travelencer_android_2021.data.LoginResponse
import com.example.travelencer_android_2021.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

// 로그인 액티비티
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

        // <로그인> 버튼 클릭
        btnLogin.setOnClickListener {
            val email = binding.editId.text.toString()
            val password = binding.editPassWord.text.toString()

            startLogin(LoginData(email, password))
        }

        // <비밀번호 찾기> 텍스트뷰 클릭
        tvPasswordFind.setOnClickListener {
            // PasswordFindActivity로 이동하기
            val intent = Intent(this@LoginActivity, PasswordFindActivity::class.java)
            startActivity(intent)
        }

        // <회원가입> 버튼 클릭
        btnRegister.setOnClickListener {
            // RegisterActivity로 이동하기
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    // 로그인 하기
    private fun startLogin(data : LoginData) {
        val call = RetrofitClient.serviceApi.userLogin(data)
        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            // 응답 성공 시
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val result : LoginResponse = response.body()!!
                    Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                    Log.d("mmm 로그인 성공", "로그인 성공, 유저 아이디 : " + result.userId)
                    finish()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "로그인 에라 발생", Toast.LENGTH_SHORT).show()
                Log.d("mmm 로그인 fail", t.message.toString())
            }
        })
    }

}