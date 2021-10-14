package com.example.travelencer_android_2021

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.travelencer_android_2021.databinding.ActivityQrBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

private const val TAG = "mmm"

class QRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        // 사용자 정보 받아와서 설정하기
        val uid : String = (Firebase.auth.uid ?: getSharedPreferences("uid", Context.MODE_PRIVATE)!!.getString("uid", "-1")) as String
        if (uid != "-1") {
            getEmail(uid)
        }
    }

    // 이메일 얻기
    private fun getEmail(uid : String) {
        if (!NetworkManager(applicationContext).checkNetworkState()) {
            Toast.makeText(applicationContext, "네트워트 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val db = Firebase.firestore
        val docRef = db.collection("userT").document(uid)

        // 데이터 가져오기(프로필 사진 제외)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val map = document.data as HashMap<String, Any>
                        val email : String = map["email"] as String
                        createQRCode(email)
                    } else {
                        Toast.makeText(applicationContext, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, "QR코드를 만드는  데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "get failed with ", exception)
                }
    }

    // QR 코드 생성
    fun createQRCode(email : String) {
        val qrCode = QRCodeWriter()
        val bitMtx = qrCode.encode(
            email,
            BarcodeFormat.QR_CODE,
            350,
            350
        )
        val bitmap: Bitmap = Bitmap.createBitmap(bitMtx.width, bitMtx.height, Bitmap.Config.RGB_565)
        for(i in 0 until bitMtx.width){
            for(j in 0 until bitMtx.height){
                val color = if(bitMtx.get(i, j)) Color.BLACK else Color.WHITE
                bitmap.setPixel(i, j, color)
            }
        }
        binding.imgQR.setImageBitmap(bitmap)
    }
}