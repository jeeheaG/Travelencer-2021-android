package com.example.travelencer_android_2021

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.travelencer_android_2021.databinding.ActivityQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        val uid = applicationContext.getSharedPreferences("uid", Context.MODE_PRIVATE).getInt("uid", -1)
        // TODO : uid 이용해서 이메일 받아오기
        if (uid != -1) { getEmail() }
    }

    // 이메일 요청 보내기
    private fun getEmail() {
        if (!NetworkManager(applicationContext).checkNetworkState()) {
            Toast.makeText(applicationContext, "네트워트 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO : 받아온 이메일로 QR 코드 생성하기 (createQRCode(email)이용)
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