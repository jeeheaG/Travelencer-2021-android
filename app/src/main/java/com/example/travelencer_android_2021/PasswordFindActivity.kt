package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.travelencer_android_2021.databinding.ActivityPasswordFindBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_password_find.*

// 비밀번호 찾기 액티비티
class PasswordFindActivity  : AppCompatActivity() {
    private var _binding: ActivityPasswordFindBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPasswordFindBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 뒤로가기 이미지 클릭
        binding.imgBack.setOnClickListener {
            finish()
        }

        // 이메일 유효성 확인
        var emailCheck = false
        binding.editEmailId.doOnTextChanged { _, _, _, _ ->
            val email = binding.editEmailId.text.toString()
            if (!isEmailValid(email)) {
                binding.editEmailId.error = "이메일이 유효하지 않습니다."
                emailCheck = false
            }
            else {
                binding.editEmailId.error = null
                emailCheck = true
            }
        }

        // <이메일 인증> 버튼 클릭
        binding.btnSendEmail.setOnClickListener {
            if (!NetworkManager(applicationContext).checkNetworkState()) {
                Toast.makeText(applicationContext, "네트워트 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = binding.editEmailId.text.toString()

            if (emailCheck) {
                // 이메일 보내기
                Firebase.auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }

            }
            else Toast.makeText(applicationContext, "이메일이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
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