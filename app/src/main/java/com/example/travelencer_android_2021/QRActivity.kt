package com.example.travelencer_android_2021

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelencer_android_2021.databinding.ActivityQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createQRCode("이메일")
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