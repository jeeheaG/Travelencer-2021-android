package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.travelencer_android_2021.data.PlaceRegisterData
import com.example.travelencer_android_2021.databinding.ActivityAddPNCBinding
import com.example.travelencer_android_2021.model.ModelPNCT
import com.google.firebase.firestore.FirebaseFirestore

// Main에서 왔는지 Write에서 왔는지 resultCode로 구분
const val RESULT_CODE_MAIN = Activity.RESULT_FIRST_USER + 0 //1
const val RESULT_CODE_WRITE = Activity.RESULT_FIRST_USER + 1 //2

class AddPNCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPNCBinding
    private val codePlaceName = "placeName"
    private val codePlaceLoc = "placeLoc"
    private val codePlaceId = "placeId"
    lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPNCBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firestore = FirebaseFirestore.getInstance()

        // placeMain의 여행지 등록 : placeMain / postWrite의 여행지 검색 : write / 여행지 등록 : add -> 셋 중 어디서 온 건 지 알아야 함
        val from = intent.getStringExtra("from")

        // 등록 완료
        binding.btnPlaceRegisterDone.setOnClickListener {
            val pros = binding.etPlaceRegisterPros.text.toString()
            val cons = binding.etPlaceRegisterCons.text.toString()

            when(from){
                "placeMain" -> {
                    //방금 등록한 장소의 placeID가져오기
                    val placeId = intent.getStringExtra("placeId") ?: "id없음"
                    //업로드
                    postPNC(placeId, pros, cons)

                    setResult(RESULT_CODE_MAIN)
                    finish()
                }
                "search" -> {
                    // 선택한 장소의 contentID 가져오기
                    val contentId = intent.getStringExtra("contentId") ?: "-1" //null이면 "-1"값(말이 안되는 값임)으로 대체
                    // 업로드
                    postPNC(contentId, pros, cons)

                    // postWrite에서 온 "search" 나 "add"일 경우 장소명과 장소주소를 가지고 돌아감
                    val writeIntent = Intent(this, PostWritePlaceSearchActivity::class.java)
                    writeIntent.putExtra(codePlaceId, contentId)
                    writeIntent.putExtra(codePlaceName, intent.getStringExtra(codePlaceName))
                    writeIntent.putExtra(codePlaceLoc, intent.getStringExtra(codePlaceLoc))
                    setResult(RESULT_CODE_WRITE, writeIntent)

                    finish()
                }
                "add" -> {
                    //방금 등록한 장소의 placeID가져오기
                    val placeId = intent.getStringExtra("placeId") ?: "id없음"
                    //업로드
                    postPNC(placeId, pros, cons)

                    val writeIntent = Intent(this, PostWriteActivity::class.java)
                    writeIntent.putExtra(codePlaceId, placeId)
                    writeIntent.putExtra(codePlaceName, intent.getStringExtra(codePlaceName))
                    writeIntent.putExtra(codePlaceLoc, intent.getStringExtra(codePlaceLoc))
                    setResult(RESULT_CODE_WRITE, writeIntent)

                    finish()
                }
            }
        }

        binding.ivBack.setOnClickListener{
            finish()
        }
    }

    private fun postPNC(placeId: String, pros: String, cons: String) {
        val modelPNCT = ModelPNCT(placeId, pros, cons)
        firestore.collection("pncT").document().set(modelPNCT)
                .addOnSuccessListener { Log.d("로그--addPlace","성공~~") }
                .addOnFailureListener { Log.e("로그--addPlace","실패 . . . .") }
    }

}