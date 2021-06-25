package com.example.travelencer_android_2021

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.travelencer_android_2021.databinding.FragmentFeedFilterBinding
import com.example.travelencer_android_2021.model.ModelAreaCode
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL

private const val TAG_FEED_FILTER = "feed_filter_fragment"
private const val TAG_FEED = "feed_fragment"

private const val SP_FEED_FILTERED: String = "feedFiltered"

// 여행 피드 - 필터
class FeedFilterFragment : Fragment() {
    private var _binding: FragmentFeedFilterBinding? = null
    private val binding get() = _binding!!

    lateinit var spinner : Array<Spinner>   // 스피너 배열

    val areaCodeArray = arrayListOf(ArrayList<ModelAreaCode>(), ArrayList<ModelAreaCode>())   // 지역 코드 배열, 시군구 코드 배열
    val spinnerArray = arrayListOf(ArrayList<String>(), ArrayList<String>())        // 지역 코드 스피너 배열, 시군구 코드 스피너 배열

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val view = inflater.inflate(R.layout.fragment_feed_filter, container, false)
        _binding = FragmentFeedFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        spinner = arrayOf(view.findViewById(R.id.spinPlaceLarge), view.findViewById(R.id.spinPlaceSmall))

        // 스피너 설정
        fetchXML("http://api.visitkorea.or.kr/upload/manual/sample/areaCode_sample1.xml", 0)

        //지역 아이템 목록 더미 데이터
        val placeLargeItemList: Array<String> = resources.getStringArray(R.array.place_large_item_list)
        val placeSmallItemList: Array<String> = resources.getStringArray(R.array.place_small_item_list)

        //두 스피너 어댑터. 프래그먼트이므로 context가져올 때 activity가 null인지 아닌지 확인
        val placeLargeAdapter = activity?.let{ ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, placeLargeItemList) }
        val placeSmallAdapter = activity?.let{ ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, placeSmallItemList) }

        binding.spinPlaceLarge.adapter = placeLargeAdapter
        binding.spinPlaceLarge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    Toast.makeText(activity, placeLargeItemList[position], Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinPlaceSmall.adapter = placeSmallAdapter
        binding.spinPlaceSmall.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    Toast.makeText(activity, placeSmallItemList[position], Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //이 화면으로 오면 필터 설정이 해제됨
        val pref = activity?.getSharedPreferences("pref", 0)
        val edit = pref?.edit()
        edit?.putBoolean(SP_FEED_FILTERED, false)
        edit?.apply()
        Log.d("로그 -feedFiltered-1--", "feedFiltered : ${pref?.getBoolean(SP_FEED_FILTERED, true)}")


        // 검색 버튼 눌렀을 때
        // sharedPreferences 의 SF_FEED_FILTERED값 true로 변경
        // preantFragmentManager에 접근해서 현재 feedFilter 프래그먼트 remove, feed 프래그먼트 add

        binding.btnShowFeed.setOnClickListener {
            edit?.putBoolean(SP_FEED_FILTERED, true)
            edit?.apply()
            Log.d("로그 -filtered-2--", "feedFiltered : ${pref?.getBoolean(SP_FEED_FILTERED, false)}")


            val parentManager: FragmentManager = parentFragmentManager
            val pft: FragmentTransaction = parentManager.beginTransaction()

            pft.add(R.id.flContainer, FeedFragment(), TAG_FEED)

            val feedFilter = parentManager.findFragmentByTag(TAG_FEED_FILTER)

            feedFilter?.let {pft.remove(it)}

            pft.commitAllowingStateLoss()
        }

        return view
    }

    // xml 파싱하기 (option : 0 = 스피너0, 지역코드 / option : 1 = 스피너1, 시군구 코드)
    fun fetchXML(url : String, option : Int) {
        lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수

        // xml 데이터 가져와서 파싱하기
        // 외부에서 데이터 가져올 때 화면 계속 동작하도록 AsyncTask 이용
        class getDangerGrade : AsyncTask<Void, Void, Void>() {
            // url 이용해서 xml 읽어오기
            override fun doInBackground(vararg p0: Void?): Void? {
                // 데이터 스트림 형태로 가져오기
                val stream = URL(url).openStream()
                val bufReader = BufferedReader(InputStreamReader(stream, "UTF-8"))

                // 한줄씩 읽어서 스트링 형태로 바꾼 후 page에 저장
                page = ""
                var line = bufReader.readLine()
                while (line != null) {
                    page += line
                    line = bufReader.readLine()
                }

                return null
            }

            // 읽어온 xml 파싱하기
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                // 스피너1초기화 (시군구 코드는 계속 바뀌므로)
                spinnerArray[1].clear()
                areaCodeArray[1].clear()

                var tagAreaCode = false   // 지역 코드 태그
                var tagAreaName = false   // 지역 이름 태그

                var areaCode = ""    // 지역 코드
                var areaName = ""    // 지역 이름

                var factory = XmlPullParserFactory.newInstance()    // 파서 생성
                factory.setNamespaceAware(true)                     // 파서 설정
                var xpp = factory.newPullParser()                   // XML 파서

                // 파싱하기
                xpp.setInput(StringReader(page))

                // 파싱 진행
                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {}
                    else if (eventType == XmlPullParser.START_TAG) {
                        var tagName = xpp.name
                        if (tagName.equals("code")) tagAreaCode = true
                        else if (tagName.equals("name")) tagAreaName = true
                    }
                    if (eventType == XmlPullParser.TEXT) {
                        if (tagAreaCode) {         // 지역 코드
                            areaCode = xpp.text
                            tagAreaCode = false
                        }
                        else if (tagAreaName) {    // 지역 이름
                            areaName = xpp.text
                            tagAreaName = false

                            // 지역 이름까지 다 읽으면 하나의 데이터 다 읽은 것임
                            var item = ModelAreaCode(areaCode.toInt(), areaName)
                            areaCodeArray[option].add(item)
                            spinnerArray[option].add(item.areaName)
                        }
                    }
                    if (eventType == XmlPullParser.END_TAG) {}

                    eventType = xpp.next()
                }
                // 스피너에 데이터 연결
                val spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, spinnerArray[option])
                spinner[option].adapter = spinnerAdapter

                val areaSpinnerAdapter = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(option == 0) {
                            val serviceUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode"
                            val serviceKey = "edSnzhmFwkaoSFwGnzfI%2FVoqtQcqDM67Uzv%2BQmbp7OkjHCY6j%2B9Pq%2BriPr7jQXagfQA0GRllEZL%2BhWBQSljPIw%3D%3D"

                            // 이 url 주소 가지고 xml에서 데이터 파싱하기
                            val requstUrl = serviceUrl +
                                    "?serviceKey=" + serviceKey +
                                    "&numOfRows=30&pageNo=1&MobileOS=AND&MobileApp=AppTest" +
                                    "&areaCode=" + areaCodeArray[option].get(position).areaCode
                            fetchXML(requstUrl, 1)
                        }
                        Log.d("mmm 스피너 선택",  areaCodeArray[option].get(position).areaName + ", " +areaCodeArray[option].get(position).areaCode)
                    }
                }
                spinner[option].onItemSelectedListener = areaSpinnerAdapter
            } // end of override fun onPostExecute(result: Void?)
        } // end of class getDangerGrade : AsyncTask<Void, Void, Void>()

        getDangerGrade().execute()
    } // end of fetchXML

}