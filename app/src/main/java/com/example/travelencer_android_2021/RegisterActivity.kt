package com.example.travelencer_android_2021

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.travelencer_android_2021.api.GMailSender
import com.example.travelencer_android_2021.data.RegisterData
import com.example.travelencer_android_2021.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btnRegister
import kotlinx.android.synthetic.main.activity_register.imgBack

// 회원가입 액티비티
class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    var code = "-1"     // 이메일 인증 코드

    var fbAuth : FirebaseAuth? = null
    var fbFiresotre : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fbAuth = FirebaseAuth.getInstance()
        fbFiresotre = FirebaseFirestore.getInstance()

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

        // <이메일 확인> 버튼 클릭
        btnEmailCheck.setOnClickListener {
            // 이메일 검사
            val email = editEmailId.text.toString()
            // 이메일 인증 코드 보내기
            if (isEmailValid(email)) {
                val mailSender = GMailSender()
                code = mailSender.code          // 이메일 인증키 저장
                mailSender.sendEmail(email)
                Toast.makeText(applicationContext, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show()

                binding.layoutStep1.visibility = View.GONE
                binding.layoutStep2.visibility = View.VISIBLE
            }
            else Toast.makeText(applicationContext, "이메일이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        // <확인> 버튼 클릭(이메일 인증 코드 확인)
        binding.btnEmailCodeCheck.setOnClickListener {
            // 인증 코드가 맞으면
            if (code == binding.editEmailCode.text.toString()) {
                binding.layoutStep2.visibility = View.GONE
                binding.layoutStep3.visibility = View.VISIBLE
            }
            else  Toast.makeText(applicationContext, "인증 코드를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
        }

        // 비밀번호 일치하는지 확인
        var passwordCheck = false
        editPassword1.doOnTextChanged { _, _, _, _ ->
            val password1 = editPassword1.text.toString()
            val password2 = editPassword2.text.toString()

            // 비밀번호 6자리인지 확인
            if (!isPasswordValid(password1)) editPassword1.error = "비민번호는 6지리 이상이어야 합니다."
            else editPassword1.error = null

            // 비밀번호 정확히 입력했는지 확인
            if(password1 == password2) {
                passwordCheck = true
                editPassword2.error = null
            }
            else {
                passwordCheck = false
                editPassword2.error = "비밀번호가 다릅니다."
            }
        }
        editPassword2.doOnTextChanged { _, _, _, _ ->
            val password1 = editPassword1.text.toString()
            val password2 = editPassword2.text.toString()

            // 비밀번호 정확히 입력했는지 확인
            if(password1 == password2) {
                passwordCheck = true
                editPassword2.error = null
            }
            else {
                passwordCheck = false
                editPassword2.error = "비밀번호가 다릅니다."
            }
        }

        // 이메일 유효성 확인
        var emailCheck = false
        editEmailId.doOnTextChanged { _, _, _, _ ->
            val email = editEmailId.text.toString()
            if (!isEmailValid(email)) {
                editEmailId.error = "이메일이 유효하지 않습니다."
                emailCheck = false
            }
            else {
                editEmailId.error = null
                emailCheck = true
            }
        }

        // <가입하기> 버튼 클릭
        btnRegister.setOnClickListener {
            // 닉네임 검사
            val name = editNickname.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(applicationContext, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 이메일 검사
            val email = editEmailId.text.toString()
            if (!emailCheck) {
                Toast.makeText(applicationContext, "이메일이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 비밀번호 검사
            val password = editPassword2.text.toString()
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
            // 약관 동의 검사
            if (btnRegister.isEnabled != checkbox.isChecked) {
                Toast.makeText(applicationContext, "서비스 이용 약관 및 개인정보 보호정책에 동의해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 회원가입 하기
            startRegister(email, password, name)
        }

    }

    // 회원가입 하기
    private fun startRegister(email : String, password : String, name : String) {
        // 사용자 계정 만들기
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(baseContext, "회원가입에 실패했습니다...", Toast.LENGTH_SHORT).show()
                return@addOnCompleteListener
            } else {
                // 사용자 정보 저장하기
                val uid = auth.currentUser!!.uid //ref.id
                val ref = fbFiresotre?.collection("userT")?.document(uid)!!
                val data = RegisterData(uid, email, null, name, null)
                ref.set(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "회원가입에 실패했습니다...", Toast.LENGTH_SHORT).show()
                        }
            }
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
