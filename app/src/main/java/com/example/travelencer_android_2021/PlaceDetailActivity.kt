package com.example.travelencer_android_2021

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.adapter.PlaceDetailPhotoAdapter
import com.example.travelencer_android_2021.adapter.PlaceDetailRecentPostAdapter
import com.example.travelencer_android_2021.api.TourApiRetrofitClient
import com.example.travelencer_android_2021.databinding.ActivityPlaceDetailBinding
import com.example.travelencer_android_2021.model.ModelCasePhotoOnly
import com.example.travelencer_android_2021.model.ModelPlaceDetailRecentPost
import com.example.travelencer_android_2021.model.modelTourApiDetailCommon.ModelTourApiDetailCommon
import com.example.travelencer_android_2021.model.modelTourApiDetailImage.ModelTourApiDetailImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_place_detail.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

//뷰바인딩 사용
const val NO_ID = "none"

class PlaceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailBinding
    lateinit var firebase: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    var photoList = arrayListOf<ModelCasePhotoOnly>()
    var recentPostList = arrayListOf<ModelPlaceDetailRecentPost>()
    private lateinit var photoAdapter: PlaceDetailPhotoAdapter
    private lateinit var recentPostAdapter: PlaceDetailRecentPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val contentId = intent.getStringExtra("contentId") ?: NO_ID
        val placeId = intent.getStringExtra("placeId") ?: NO_ID

/*        val photoList = arrayListOf(
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung)
        )*/
/*        //더미데이터
        recentPostList = arrayListOf(
                ModelPlaceDetailRecentPost("날 좋은 날 화성 나들이", "jeehea", R.drawable.dummy_haewoojae),
                ModelPlaceDetailRecentPost("수원화성 놀러감", "yoojin", R.drawable.dummy_haewoojae),
                ModelPlaceDetailRecentPost("수원으로 소풍", "yoonkung", R.drawable.dummy_hwasung),
                ModelPlaceDetailRecentPost("수원 놀기 좋은 코스 추천", "sewon", R.drawable.dummy_hwasung),
                ModelPlaceDetailRecentPost("놀러가자 수원", "minyeong", R.drawable.dummy_haewoojae)
        )*/

        binding.rvPlaceDetailPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlaceDetailRecentPostList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvPlaceDetailPhotoList.setHasFixedSize(true)
        binding.rvPlaceDetailRecentPostList.setHasFixedSize(true)

        photoAdapter = PlaceDetailPhotoAdapter(photoList, this)
        recentPostAdapter = PlaceDetailRecentPostAdapter(recentPostList)
        binding.rvPlaceDetailPhotoList.adapter = photoAdapter
        binding.rvPlaceDetailRecentPostList.adapter = recentPostAdapter
        
        //TODO : bookmarked 값 서버에서 데이터 받아와서 쓰기
        var bookmarked = false
        binding.ivPlaceDetailBookmark.setOnClickListener{
            if(!bookmarked){
                ivPlaceDetailBookmark.setImageResource(R.drawable.ic_bookmark_filled)
                bookmarked = true
                Toast.makeText(this, "즐겨찾기 되었습니다", Toast.LENGTH_SHORT).show()
            }
            else if(bookmarked){
                ivPlaceDetailBookmark.setImageResource(R.drawable.ic_bookmark_line)
                bookmarked = false
                Toast.makeText(this, "즐겨찾기 해제 되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 사진 한장씩 넘어감
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvPlaceDetailPhotoList)

        // 사진 번호 표시
        photoCountUpdate()
        binding.rvPlaceDetailPhotoList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val curPhotoNum = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val photoCounter = "${curPhotoNum+1} / ${photoList.size}"
                binding.tvPlaceDetailPhotoNumber.text = photoCounter
            }
        })

        //지도는 callTourDetailCommon 안에

        // <PNC더보기 버튼> 클릭
        binding.ivPlaceDetailPNCMore.setOnClickListener{
            val intent = Intent(this, PNCActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }

        //interceptor설정과 데이터 요청 함수
        TourApiRetrofitClient.tourInterceptor.level = HttpLoggingInterceptor.Level.BODY
        callTourDetailCommon(contentId, this)
        callTourDetailImage(contentId, this)

    }


    // 공통정보 데이터 받아오기
    private val tourApi = TourApiRetrofitClient.tourApiService

    private fun callTourDetailCommon(contentId: String, mContext: Context) {
        val tourData = MutableLiveData<ModelTourApiDetailCommon>()
        //Log.d("로그 tour", "keyword : ${keyword}")

        // api 요청보냄
        tourApi.getTourDetailCommonData(contentId = contentId)
                .enqueue(object : retrofit2.Callback<ModelTourApiDetailCommon> {

                    override fun onResponse(
                            call: Call<ModelTourApiDetailCommon>,
                            response: Response<ModelTourApiDetailCommon>
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
                            val item = body.items.item
                            binding.tvPlaceDetailPlaceName.text = item.title
                            binding.tvPlaceDetailPlaceLocation.text = item.addr1 ?: "-"
                            binding.tvPlaceDetailExplain.text = item.overview

                            // 지도
                            // TODO : 지도 부분 드래그 시 리사이클러뷰 스크롤 작동 개선하기
                            val latitude = item.mapy //lat이 y, long이 x였네
                            val longitude = item.mapx
                            val placeName = item.title
                            val zoomLevel = 1
                            Log.d("로그 TourDetail", "$latitude, $longitude, $placeName")

                            val mapView = MapView(mContext) //PlaceDetailActivity()?baseContext랑 똑같은 듯. applicationContext는 아닌 것 같고 parent??
                            val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)

                            mapView.setMapCenterPoint(mapPoint, false)
                            mapView.setZoomLevel(zoomLevel, true)

                            val marker = MapPOIItem()
                            marker.itemName = placeName
                            marker.mapPoint = mapPoint
                            marker.markerType = MapPOIItem.MarkerType.BluePin
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

                            mapView.addPOIItem(marker)
                            binding.clPlaceDetailMapView.addView(mapView)

                            //대표사진
                            val firstImage = item.firstimage
                            if(firstImage != null){
                                photoList.add(ModelCasePhotoOnly(firstImage))
                            }


                        }
                        photoAdapter.notifyDataSetChanged()
                        photoCountUpdate()
                    }

                    override fun onFailure(call: Call<ModelTourApiDetailCommon>, t: Throwable) {
                        Log.e("실패 로그", "실패..")
                        t.printStackTrace()
                    }
                })
    }//요청 함수 끝


    // 이미지 데이터 받아오기
    private val tourApiImage = TourApiRetrofitClient.tourApiService

    private fun callTourDetailImage(contentId: String, mContext: Context) {
        val tourData = MutableLiveData<ModelTourApiDetailImage>()
        //Log.d("로그 tour", "keyword : ${keyword}")

        // api 요청보냄
        tourApiImage.getTourDetailImageData(contentId = contentId)
                .enqueue(object : retrofit2.Callback<ModelTourApiDetailImage> {

                    override fun onResponse(
                            call: Call<ModelTourApiDetailImage>,
                            response: Response<ModelTourApiDetailImage>
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
                            //val item = body.items.item

                            val numsOfRows = body.numOfRows
                            val totalCount = body.totalCount
                            val count: Int = if(numsOfRows > totalCount) { totalCount } else numsOfRows

                            //Log.d("로그 tour", "numsOfRows : ${numsOfRows}")
                            //val dummyImageUrl = "data:image/jpeg;base64,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"
                            for (i in 0 until count) {
                                val item = body.items.item[i]
                                photoList.add(ModelCasePhotoOnly(item.originimgurl))
                            }

                        }
                        photoAdapter.notifyDataSetChanged()
                        photoCountUpdate()
                    }

                    override fun onFailure(call: Call<ModelTourApiDetailImage>, t: Throwable) {
                        Log.e("실패 로그", "이미지 정보 가져오기 실패..")
                        t.printStackTrace()
                    }
                })
    }//요청 함수 끝

    fun photoCountUpdate(){
        binding.tvPlaceDetailPhotoNumber.text = "1 / ".plus(photoList.size)
    }
}