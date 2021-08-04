package com.example.travelencer_android_2021

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.AddPlaceSearchResultAdapter
import com.example.travelencer_android_2021.api.KakaoLocalApi
import com.example.travelencer_android_2021.api.KakaoLocalApiRetrofitClient
import com.example.travelencer_android_2021.databinding.ActivityAddPlaceSearchAddressBinding
import com.example.travelencer_android_2021.model.ModelAddressSearchList
import com.example.travelencer_android_2021.model.ModelKakaoLocalApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class AddPlaceSearchAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceSearchAddressBinding
    //private val codeAddress = "address"
    var currentPage = 1
    var isEnd: Boolean? = null
    private val size = 30 //최대값

    var addressList: ArrayList<ModelAddressSearchList> = arrayListOf()
    //lateinit var mAdapter: AddPlaceSearchResultAdapter
    private val mAdapter = AddPlaceSearchResultAdapter(addressList, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceSearchAddressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // 리사이클러 뷰 설정, 어댑터에 데이터 담아 연결
        binding.rvAddressSearchResultList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAddressSearchResultList.setHasFixedSize(true)

//        mAdapter = AddPlaceSearchResultAdapter(addressList, this)
        binding.rvAddressSearchResultList.adapter = mAdapter

        binding.btnAddressSearch.setOnClickListener {
            addressList.clear()
            currentPage = 1
            callKakaoLocalKeyword(binding.etAddressSearchKeyword.text.toString(), currentPage, size)

            //키보드 내림
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }

        binding.btnAddressMore.setOnClickListener {
            currentPage += 1
            callKakaoLocalKeyword(binding.etAddressSearchKeyword.text.toString(), currentPage, size)
        }

        // TODO : setResult, 리사이클러뷰 어댑터에서 finish()담긴 리스너 달기 -> 확인 창 한번 띄우면 좋겠다
    }


    // Kakao Local Api 데이터 받아오기
    private val kakaoApi = KakaoLocalApiRetrofitClient.kakaoApiService

    private fun callKakaoLocalKeyword(address: String, page: Int, size: Int) {
        val kakaoData = MutableLiveData<ModelKakaoLocalApi>()

        kakaoApi.getKakaoAddress(KakaoLocalApi.API_KEY, address = address, page = page, size = size)
                .enqueue(object : retrofit2.Callback<ModelKakaoLocalApi> {
                    override fun onResponse(
                            call: Call<ModelKakaoLocalApi>,
                            response: Response<ModelKakaoLocalApi>
                    ) {
                        kakaoData.value = response.body()

                        //<<<에러 시 에러코드 받아오는 부분>>>
                        if (kakaoData.value == null) {
                            Log.i("오류 kakao.value == null", response.errorBody().toString())
                            try {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.i("오류 try", jObjError.getJSONObject("error").getString("message"))
                                //Toast.makeText(getContext(), jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Log.i("오류 catch", e.message.toString())
                                //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                        if (!response.isSuccessful) {
                            Log.i("!response.isSuccessful", response.errorBody().toString())
                            try {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.i("오류 try", jObjError.getJSONObject("error").getString("message"))
                                //Toast.makeText(getContext(), jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Log.i("오류 catch", e.message.toString())
                                //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                        //<<<여기까지>>>

//                        Log.i("kakao", "0 ${kakaoData.value!!.documents[0].address_name}")
//                    Log.i("kakao", "1 ${kakaoData.value!!.documents[1].address_name}")
//                    Log.i("kakao", "2 ${kakaoData.value!!.documents[2].address_name}")
//                    Log.i("kakao", "0 ${kakaoData.value!!.documents[0].address_type}")
//                    Log.i("kakao", "${kakaoData.value!!.documents[0].x}")
//                    Log.i("kakao", "${kakaoData.value!!.documents[0].y}")
//                    Log.i("kakao", "${kakaoData.value!!.documents[0].address.region_1depth_name}")
//                    Log.i("kakao", "${kakaoData.value!!.documents[0].address.region_2depth_name}")
//                    Log.i("kakao", "${kakaoData.value!!.documents[0].address.region_3depth_name}")

                        Log.i("kakao", "total = ${kakaoData.value!!.meta.total_count}")
                        Log.i("kakao", "size = ${kakaoData.value!!.documents.size}")

                        // 검색결과 총 개수
                        val total = kakaoData.value?.let { it.meta.total_count }

                        if (total != 0) {
                            //좌표정보나 DB에 넘길 정보들 더 챙기기

                            //
                            isEnd = kakaoData.value?.let { it.meta.is_end }
                            if(page == 1 && isEnd == false){
                                if(binding.btnAddressMore.visibility == View.INVISIBLE){
                                    //<결과 더보기> 버튼 보이기
                                    binding.btnAddressMore.visibility = View.VISIBLE
                                }
                            }
                            if(page != 1 && isEnd == true){
                                binding.btnAddressMore.visibility = View.INVISIBLE
                                Toast.makeText(applicationContext, "마지막 페이지가 더보기 됩니다.", Toast.LENGTH_LONG).show()
                            }

                            for (element in kakaoData.value!!.documents) {
                                val name = element.address_name
                                val latitude = element.x.toFloat()
                                val longitude = element.y.toFloat()
                                addressList.add(ModelAddressSearchList(name, latitude, longitude))
                            }
                            mAdapter.notifyDataSetChanged()

                        }else{
                            Toast.makeText(applicationContext, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ModelKakaoLocalApi>, t: Throwable) {
                        Log.e("실패 로그", "실패")
                        t.printStackTrace()
                    }
                })
    }
}