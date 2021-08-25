package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.travelencer_android_2021.api.GMailSender
import com.example.travelencer_android_2021.api.RetrofitClient
import com.example.travelencer_android_2021.data.PasswordFindData
import com.example.travelencer_android_2021.data.PasswordFindResponse
import com.example.travelencer_android_2021.databinding.ActivityPasswordFindBinding
import kotlinx.android.synthetic.main.activity_password_find.*
import retrofit2.Call
import retrofit2.Response

// 비밀번호 찾기 액티비티
class PasswordFindActivity  : AppCompatActivity() {
    private var _binding: ActivityPasswordFindBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPasswordFindBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var code = "-1"

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
            val email = binding.editEmailId.text.toString()

            if (emailCheck) {
                // 이메일 보내기
                val mailSender = GMailSender()
                code = mailSender.code          // 이메일 인증키 저장
                mailSender.sendEmail(email)
                Toast.makeText(applicationContext, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show()

                binding.editCode.visibility = View.VISIBLE
                binding.btnCodeCheck.visibility = View.VISIBLE

            }
            else Toast.makeText(applicationContext, "이메일이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        // <확인> 버튼 입력(이메일 인증 코드 확인)
        binding.btnCodeCheck.setOnClickListener {
            // 인증 코드가 맞으면
            if (code == binding.editCode.text.toString()) {
                binding.editPassword1.visibility = View.VISIBLE
                binding.editPassword2.visibility = View.VISIBLE
                binding.btnChangePassword.visibility = View.VISIBLE

                binding.editEmailId.isEnabled = false
                binding.btnSendEmail.visibility = View.GONE
                binding.editCode.visibility = View.GONE
                binding.btnCodeCheck.visibility = View.GONE
            }
            else  Toast.makeText(applicationContext, "인증 코드를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
        }

        // 비밀번호 일치하는지 확인
        var passwordCheck = false
        binding.editPassword1.doOnTextChanged { _, _, _, _ ->
            val password1 = binding.editPassword1.text.toString()
            val password2 = binding.editPassword2.text.toString()

            // 비밀번호 6자리인지 확인
            if (!isPasswordValid(password1)) binding.editPassword1.error = "비민번호는 6지리 이상이어야 합니다."
            else binding.editPassword1.error = null

            // 비밀번호 정확히 입력했는지 확인
            if(password1 == password2) {
                passwordCheck = true
                binding.editPassword2.error = null
            }
            else {
                passwordCheck = false
                binding.editPassword2.error = "비밀번호가 다릅니다."
            }
        }
        binding.editPassword2.doOnTextChanged { _, _, _, _ ->
            val password1 = binding.editPassword1.text.toString()
            val password2 = binding.editPassword2.text.toString()

            // 비밀번호 정확히 입력했는지 확인
            if(password1 == password2) {
                passwordCheck = true
                binding.editPassword2.error = null
            }
            else {
                passwordCheck = false
                binding.editPassword2.error = "비밀번호가 다릅니다."
            }
        }
        // <비밀번호 변경> 버튼 클릭
        binding.btnChangePassword.setOnClickListener {
            val email = binding.editEmailId.text.toString()
            // 비밀번호 검사
            val password = binding.editPassword2.text.toString()
            if (password.isEmpty()) {
                Toast.makeText(applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isPasswordValid(password)) {
                Toast.makeText(applicationContext, "비민번호는 6지리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!passwordCheck) {
                Toast.makeText(applicationContext, "비밀번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startChangePassword(PasswordFindData(email, password))
        }

    }

    // 비밀번호 변경하기
    private fun startChangePassword(data : PasswordFindData) {
        val call = RetrofitClient.serviceApiUser.userPwchange(data)
        call.enqueue(object : retrofit2.Callback<PasswordFindResponse> {
            // 응답 성공 시
            override fun onResponse(call: Call<PasswordFindResponse>, response: Response<PasswordFindResponse>) {
                if (response.isSuccessful) {
                    val result : PasswordFindResponse = response.body()!!

                    if (result.code == 200) {
                        Toast.makeText(applicationContext, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<PasswordFindResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "로그인 에러 발생", Toast.LENGTH_SHORT).show()
                Log.d("mmm 로그인 fail", t.message.toString())
            }
        })
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