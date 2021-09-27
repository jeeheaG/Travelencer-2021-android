package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.travelencer_android_2021.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

// 로그인 액티비티
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        // 뒤로가기 이미지 클릭
        binding.imgBack.setOnClickListener {
            finish()
        }

        // 이메일 유효성 확인
        var emailCheck = false
        binding.editId.doOnTextChanged { _, _, _, _ ->
            val email = binding.editId.text.toString()
            if (!isEmailValid(email)) {
                binding.editId.error = "이메일이 유효하지 않습니다."
                emailCheck = false
            }
            else {
                binding.editId.error = null
                emailCheck = true
            }
        }

        // 비밀번호 일치하는지 확인
        binding.editPassWord.doOnTextChanged { _, _, _, _ ->
            val password = binding.editPassWord.text.toString()

            // 비밀번호 6자리인지 확인
            if (!isPasswordValid(password)) binding.editPassWord.error = "비민번호는 6지리 이상이어야 합니다."
            else binding.editPassWord.error = null
        }

        // <로그인> 버튼 클릭
        binding.btnLogin.setOnClickListener {
            val email = binding.editId.text.toString()
            val password = binding.editPassWord.text.toString()

            // 이메일 검사
            if (!emailCheck) {
                Toast.makeText(applicationContext, "이메일이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 비밀번호 검사
            if (password.isEmpty()) {
                Toast.makeText(applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isPasswordValid(password)) {
                Toast.makeText(applicationContext, "비민번호는 6지리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 로그인 하기
            startLogin(email, password)
        }

        // <비밀번호 찾기> 텍스트뷰 클릭
        binding.tvPasswordFind.setOnClickListener {
            // PasswordFindActivity로 이동하기
            val intent = Intent(applicationContext, PasswordFindActivity::class.java)
            startActivity(intent)
        }

        // <회원가입> 버튼 클릭
        binding.btnRegister.setOnClickListener {
            // RegisterActivity로 이동하기
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    // 로그인 하기
    private fun startLogin(email : String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 로그인 여부 저장
                        val pref = applicationContext.getSharedPreferences("login", Context.MODE_PRIVATE)
                        val edit = pref.edit()
                        edit.putString("login", "O").apply()

                        // 로그인 여부 보내기
                        val outIntent = Intent(applicationContext, MainActivity::class.java)
                        outIntent.putExtra("login", "O")
                        setResult(Activity.RESULT_OK, outIntent)

                        Toast.makeText(baseContext, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else Toast.makeText(applicationContext, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
    }

    // 이메일 형식 체크
    private fun isEmailValid(email : String) : Boolean {
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    // 비밀번호 6자리 이상인지 확인
    private fun isPasswordValid(password : String) : Boolean {
        return password.length >= 6
    }

}