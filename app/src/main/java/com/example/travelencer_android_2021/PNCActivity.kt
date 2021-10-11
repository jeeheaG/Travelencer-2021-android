package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PNCAdapter
import com.example.travelencer_android_2021.databinding.ActivityPNCBinding
import com.example.travelencer_android_2021.model.ModelPNC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//뷰바인딩 사용

class PNCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPNCBinding

    private val pncList = arrayListOf<ModelPNC>()
    private lateinit var mAdapter: PNCAdapter

    lateinit var firebase: FirebaseFirestore
//    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPNCBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val placeId = intent.getStringExtra("placeId") ?: "none"

        firebase = FirebaseFirestore.getInstance()


 /*       val pncList = arrayListOf(
                ModelPNC(R.drawable.ic_thumb_up, "수원화성의 대표적인 관광지"),
                ModelPNC(R.drawable.ic_thumb_down, "모기가 너무 많음"),
                ModelPNC(R.drawable.ic_thumb_up, "맛집 천지"),
                ModelPNC(R.drawable.ic_thumb_up, "맛집 천지"),
                ModelPNC(R.drawable.ic_thumb_down, "모기가 너무 많음"),
                ModelPNC(R.drawable.ic_thumb_up, "수원화성의 대표적인 관광지")
        )*/

        binding.rvPNCList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPNCList.setHasFixedSize(true)

        getPNC(placeId)

        mAdapter = PNCAdapter(pncList)
        binding.rvPNCList.adapter = mAdapter

    }


    private fun getPNC(placeId: String) {
        val document = firebase.collection("pncT").whereEqualTo("placeId", placeId).get()
        document.addOnSuccessListener { documents ->
            for (doc in documents){
                val map = doc.data as HashMap<String, Any>
                val pros : String? = map["pros"] as String?
                val cons : String? = map["cons"] as String?

                if(pros != null){
                    pncList.add(ModelPNC(0,pros))
                }
                if(cons != null){
                    pncList.add(ModelPNC(1,cons))
                }
            }
            mAdapter.notifyDataSetChanged()
        }
                .addOnFailureListener {
                    Log.d("로그 PNCActivity","실패 . . .")
                }
    }
}