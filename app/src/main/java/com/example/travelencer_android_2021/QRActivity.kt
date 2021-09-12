package com.example.travelencer_android_2021

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.travelencer_android_2021.api.RetrofitClient
import com.example.travelencer_android_2021.data.QRData
import com.example.travelencer_android_2021.data.QRResponse
import com.example.travelencer_android_2021.databinding.ActivityQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import retrofit2.Call
import retrofit2.Response

class QRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        val uid = applicationContext.getSharedPreferences("uid", Context.MODE_PRIVATE).getInt("uid", -1)
        if (uid != -1) { getEmail(QRData(uid)) }
    }

    // 이메일 요청 보내기
    private fun getEmail(data : QRData) {
        val call = RetrofitClient.serviceApiUser.userQR(data)
        call.enqueue(object : retrofit2.Callback<QRResponse> {
            // 응답 성공 시
            override fun onResponse(call: Call<QRResponse>, response: Response<QRResponse>) {
                if (response.isSuccessful) {
                    val result : QRResponse = response.body()!!
                    // QR 코드 생성
                    if (result.code == 200) { createQRCode(result.email) }
                }
            }
            // 응답 실패 시
            override fun onFailure(call: Call<QRResponse>, t: Throwable) {
                Toast.makeText(this@QRActivity, "QR 코드를 생성 실패", Toast.LENGTH_LONG).show()
                Log.d("mmm qr code fail", t.message.toString())
            }
        })
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