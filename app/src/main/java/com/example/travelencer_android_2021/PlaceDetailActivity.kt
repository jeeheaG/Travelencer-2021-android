package com.example.travelencer_android_2021

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
import kotlinx.android.synthetic.main.activity_place_detail.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

//뷰바인딩 사용

class PlaceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailBinding

    var photoList = arrayListOf<ModelCasePhotoOnly>()
    var recentPostList = arrayListOf<ModelPlaceDetailRecentPost>()
    private lateinit var photoAdapter: PlaceDetailPhotoAdapter
    private lateinit var recentPostAdapter: PlaceDetailRecentPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val contentId = intent.getStringExtra("contentId") ?: "-1" //null이면 "-1"값(말이 안되는 값임)으로 대체


/*        val photoList = arrayListOf(
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae),
                ModelCasePhotoOnly(R.drawable.dummy_haewoojae),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung),
                ModelCasePhotoOnly(R.drawable.dummy_hwasung)
        )*/
        //더미데이터
        recentPostList = arrayListOf(
                ModelPlaceDetailRecentPost("날 좋은 날 화성 나들이", "jeehea", R.drawable.dummy_haewoojae),
                ModelPlaceDetailRecentPost("수원화성 놀러감", "yoojin", R.drawable.dummy_haewoojae),
                ModelPlaceDetailRecentPost("수원으로 소풍", "yoonkung", R.drawable.dummy_hwasung),
                ModelPlaceDetailRecentPost("수원 놀기 좋은 코스 추천", "sewon", R.drawable.dummy_hwasung),
                ModelPlaceDetailRecentPost("놀러가자 수원", "minyeong", R.drawable.dummy_haewoojae)
        )

        binding.rvPlaceDetailPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlaceDetailRecentPostList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvPlaceDetailPhotoList.setHasFixedSize(true)
        binding.rvPlaceDetailRecentPostList.setHasFixedSize(true)

        photoAdapter = PlaceDetailPhotoAdapter(photoList)
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
        binding.tvPlaceDetailPhotoNumber.text = "1 / ".plus(photoList.size)
        binding.rvPlaceDetailPhotoList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val curPhotoNum = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val photoCounter = "${curPhotoNum+1} / ${photoList.size}"
                binding.tvPlaceDetailPhotoNumber.text = photoCounter
            }
        })


        /*// 지도
        val latitude = 37.28730797086605
        val longitude = 127.01192716921177
        val placeName = "수원 화성"

        val mapView = MapView(this)
        val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)

        mapView.setMapCenterPoint(mapPoint, false)
        mapView.setZoomLevel(1, true)

        val marker = MapPOIItem()
        marker.itemName = placeName
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

        mapView.addPOIItem(marker)
        binding.clPlaceDetailMapView.addView(mapView)*/


        // <PNC더보기 버튼> 클릭
        binding.ivPlaceDetailPNCMore.setOnClickListener{
            val intent = Intent(this, PNCActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }

        //interceptor설정과 데이터 요청 함수
        TourApiRetrofitClient.interceptor.level = HttpLoggingInterceptor.Level.BODY
        callTourDetailCommon(contentId)

    }


    // 데이터 받아오기
    private val tourApi = TourApiRetrofitClient.tourApiService

    private fun callTourDetailCommon(contentId: String) {
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

/*                            // 지도
                            val latitude = item.mapx
                            val longitude = item.mapy
                            val placeName = item.title
                            val zoomLevel = 1

                            val mapView = MapView(parent) //PlaceDetailActivity()?baseContext랑 똑같은 듯. applicationContext는 아닌 것 같고 parent??
                            val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)

                            mapView.setMapCenterPoint(mapPoint, false)
                            mapView.setZoomLevel(zoomLevel, true)

                            val marker = MapPOIItem()
                            marker.itemName = placeName
                            marker.mapPoint = mapPoint
                            marker.markerType = MapPOIItem.MarkerType.BluePin
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

                            mapView.addPOIItem(marker)
                            binding.clPlaceDetailMapView.addView(mapView)*/

                        }
                        //mAdapter.notifyDataSetChanged()
                    }

                    //retrofit 은 통신장애로 인한 오류만 Failure로 넘어간다 -> 아닌듯??
                    override fun onFailure(call: Call<ModelTourApiDetailCommon>, t: Throwable) {
                        /*// 검색 결과 없어서 오류날 경우 IllegalStateException Exception Throwable ..
                        // TODO : 토스트말고 다이얼로그 띄워서 다시 필터로 보내는 게 나을 듯
                        Toast.makeText(activity, "검색 결과가 없거나 1개 뿐입니다. 다른 검색어로 검색해 주세요", Toast.LENGTH_LONG).show()*/
                        Log.e("실패 로그", "실패..")
                        t.printStackTrace()
                    }
                })
    }//요청 함수 끝
}