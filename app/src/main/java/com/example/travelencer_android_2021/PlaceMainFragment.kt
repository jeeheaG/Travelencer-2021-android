package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelencer_android_2021.adapter.PlaceMainAdapter
import com.example.travelencer_android_2021.api.TourApiRetrofitClient
import com.example.travelencer_android_2021.databinding.FragmentPlaceMainBinding
import com.example.travelencer_android_2021.model.ModelPlaceMainCard
import com.example.travelencer_android_2021.model.modelTourApiKeyword.ModelTourApiKeyword
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_place_main.view.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


//뷰바인딩 사용

class PlaceMainFragment : Fragment() {
    private var _binding: FragmentPlaceMainBinding? = null
    private val binding get() = _binding!!

    lateinit var firebase: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef : StorageReference

//    var placeList = arrayListOf<ModelPlaceMainCard>() //TourApi에서 가져온 장소정보// FirebaseDB에서 가져온 장소 정보
    var placeDataList = arrayListOf<ModelPlaceMainCard>()
    private lateinit var mAdapter: PlaceMainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val view = inflater.inflate(R.layout.fragment_place_main, container, false) //setContentView행위를 프래그먼트버전으로 한 것
        _binding = FragmentPlaceMainBinding.inflate(inflater, container, false)
        val view = binding.root

        firebase = FirebaseFirestore.getInstance()
        storage = Firebase.storage
        storageRef = storage.reference

        // 전달받은 필터 선택된 값으로 텍스트뷰 변경
        var keyword = ""
        var area1 = ""
        var area2 = ""
        var area1Code = ""   // 지역 코드
        var area2Code = ""   // 시군구 코드

        val bundle = arguments
        if (bundle != null) {
            keyword = bundle.getString("keyword").toString()    //검색어
            area1 = bundle.getString("area1").toString()    // 지역명
            area2 = bundle.getString("area2").toString()    // 시군구명
            area1Code = bundle.getInt("area1Code").toString()    // 지역 코드
            area2Code = bundle.getInt("area2Code").toString()    // 시군구 코드
            view.tvPlaceSmall.text = area2
            view.tvPlaceLarge.text = area1
        }

        binding.btnPlaceMainAddPlace.setOnClickListener {
            val intent = Intent(activity, AddPlaceActivity::class.java)
            intent.putExtra("from", "placeMain")
            startActivity(intent)
        }

        binding.rvPlaceMain.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false) //프래그먼트이므로 this대신 activity
        binding.rvPlaceMain.setHasFixedSize(true)

//        mAdapter = PlaceMainAdapter(placeList, requireActivity()) //TODO : activity!!대신 requireActivity 쓰라는데 저게 뭐지
        mAdapter = PlaceMainAdapter(placeDataList, requireActivity()) //TODO : activity!!대신 requireActivity 쓰라는데 저게 뭐지
        binding.rvPlaceMain.adapter = mAdapter

        //interceptor설정과 데이터 요청 함수
        TourApiRetrofitClient.tourInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        callTourKeyword(keyword, area1Code, area2Code)
        getPlaceData(keyword, area1Code, area2Code)

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Firebase 데이터 가져오기
    private fun getPlaceData(keyword: String, area1Code: String, area2Code: String) {
        Log.d("로그---placeMainFrag", "getPlaceData start $keyword, $area1Code, $area2Code")
        val placeRef = firebase.collection("placeT")
        placeRef
            .whereEqualTo("area1Code", area1Code.toInt())
            .whereEqualTo("area2Code", area2Code.toInt())
            .get()
            .addOnSuccessListener { documents ->
                Log.d("로그---placeMainFrag", "getPaceData Success")
                Log.d("로그---placeMainFrag", "empty : ${documents.isEmpty}")
                for (document in documents) {
                    val map = document.data as HashMap<String, Any>
                    // keyword 들어가는지 확인
                    val produce : String = map["plcProduce"] as String
                    val name : String = map["plcName"] as String
                    Log.d("로그---placeMainFrag", "areaCode일치 이름: $name")
                    // keyword 들어가면 Adapter에 추가
                    if (produce.contains(keyword) || name.contains(keyword)) {
                        Log.d("로그---placeMainFrag", "키워드 있음 | 이름: $name | 조건 produce : ${produce.contains(keyword)} name : ${name.contains(keyword)}")
                        val placeId : String = map["plcId"] as String
                        val placeName : String = map["plcName"] as String
                        val placeAddress : String = map["plcAddress"] as String

                        //사진 가져옴
                        var placePhoto = ""
                        var placePhotoUrl = ""
                        firebase.collection("placePhotoT").whereEqualTo("placeId", placeId).limit(1).get()
                            .addOnSuccessListener { documents2->
                                for (document2 in documents2) {
                                    Log.d("로그--placeMainPhoto","가져왔다 success")
                                    val map2 = document2.data as HashMap<String, Any>
                                    if(map2["placePhoto"]!=null){
                                        placePhoto = map2["placePhoto"] as String
                                    }
                                    else{
                                        placePhoto = "noImage.png"
                                    }
                                    Log.d("로그--placeMainPhoto","placeName : ${placeName} placePhoto : ${placePhoto}")

                                    // 사진 uri 가져오기
                                    Log.d("로그--placeMainPhoto스토리지", "요청 : place/$placePhoto")
                                    storageRef.child("place/$placePhoto").downloadUrl
                                        .addOnSuccessListener { uri ->
                                            placePhotoUrl = uri.toString()
                                            Log.d("로그--placeMainPhoto스토리지", " placePhotoUrl : $placePhotoUrl")
                                            placeDataList.add(ModelPlaceMainCard(placePhotoUrl, placeName, placeAddress, placeId, false))
                                            mAdapter.notifyDataSetChanged()
                                        }.addOnFailureListener { exception ->
                                            Log.d("로그--placeMainPhoto스토리지", " error: ", exception)
                                        }
                                }
                            }
                            .addOnFailureListener {
                            }
                        //여기까지 사진 가져오기
                    }
                    else continue
                }
                //관광 데이터
                callTourKeyword(keyword, area1Code, area2Code)
            }
            .addOnFailureListener {

            }

    }

    // TourApi 데이터 받아오기
    private val tourApi = TourApiRetrofitClient.tourApiService

    private fun callTourKeyword(keyword: String, areaCode: String, sigunguCode: String) {
        val tourData = MutableLiveData<ModelTourApiKeyword>()
        //Log.d("로그 tour", "keyword : ${keyword}")

        // api 요청보냄
        tourApi.getTourKeywordData(keyword = keyword, area = areaCode, sigungu = sigunguCode)
                .enqueue(object : retrofit2.Callback<ModelTourApiKeyword> {

                    override fun onResponse(
                            call: Call<ModelTourApiKeyword>,
                            response: Response<ModelTourApiKeyword>
                    ) {
                        tourData.value = response.body()
                        //Log.d("로그 tour", "${tourData.value}")

                        //<<<에러 시 에러코드 받아오는 부분>>>
                        if (tourData.value == null) {
                            Log.d("오류 tourData.value==null", response.errorBody().toString())
                            try {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.d("오류 try", jObjError.getJSONObject("error").getString("message"))
                                //Toast.makeText(getContext(), jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Log.d("오류 catch", e.message.toString())
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

                        val body = tourData.value?.let { it.response.body }
                        if(body!=null){
                            val numsOfRows = body.numOfRows.toInt()
                            val totalCount = body.totalCount.toInt()
                            val count: Int = if(numsOfRows > totalCount) { totalCount } else numsOfRows

                            //Log.d("로그 tour", "numsOfRows : ${numsOfRows}")
                            val dummyImageUrl = "https://firebasestorage.googleapis.com/v0/b/travelencer-ds.appspot.com/o/place%2FnoImage.png?alt=media&token=39a3e6db-4d17-4551-881e-b2d2eb27e552"
                                for (i in 0 until count) {
                                val item = body.items.item[i]
                                val addr = item.addr1 ?: "-" //null이면 문자열 "-"로 대체해라
                                var imageUrl: String = item.firstimage ?: dummyImageUrl //firstimage null일 수도 있어서 널체크 지우면 안됨
                                placeDataList.add(ModelPlaceMainCard(imageUrl, item.title, addr, item.contentid, true))
                            }

                        }
                        mAdapter.notifyDataSetChanged()
                    }

                    //retrofit 은 통신장애로 인한 오류만 Failure로 넘어간다 -> 아닌듯??
                    override fun onFailure(call: Call<ModelTourApiKeyword>, t: Throwable) {
                        // 검색 결과 없어서 오류날 경우 IllegalStateException Exception Throwable ..
                        // TODO : 토스트말고 다이얼로그 띄워서 다시 필터로 보내는 게 나을 듯
                        Toast.makeText(activity, "검색 결과가 없거나 1개 뿐입니다. 다른 검색어로 검색해 주세요", Toast.LENGTH_LONG).show()
                        Log.e("실패 로그", "실패..")
                        t.printStackTrace()
                    }
                })
    }//요청 함수 끝

}