package com.example.travelencer_android_2021.model

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.travelencer_android_2021.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL

data class ModelAreaCode(val areaCode : Int, val areaName : String)

class FetxhXML(spinner : Array<Spinner>, context : Context) {
    val areaCodeArray = arrayListOf(ArrayList<ModelAreaCode>(), ArrayList<ModelAreaCode>())   // 지역 코드 배열, 시군구 코드 배열
    val spinnerArray_String = arrayListOf(ArrayList<String>(), ArrayList<String>())        // 지역 코드 스피너 배열, 시군구 코드 스피너 배열

    private val spinnerArr = spinner
    val context = context

    lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수

    private val serviceUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode"
    private val serviceKey = "edSnzhmFwkaoSFwGnzfI%2FVoqtQcqDM67Uzv%2BQmbp7OkjHCY6j%2B9Pq%2BriPr7jQXagfQA0GRllEZL%2BhWBQSljPIw%3D%3D"
    val requstUrl = serviceUrl + "?serviceKey=" + serviceKey + "&numOfRows=30&pageNo=1&MobileOS=AND&MobileApp=AppTest"

    // xml 파싱하기 (option : 0 = 스피너0, 지역코드 / option : 1 = 스피너1, 시군구 코드)
    fun fetchXML(url : String, option : Int) {

        CoroutineScope(Dispatchers.IO).launch { // 비동기적으로 실행
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

            // 스피너1초기화 (시군구 코드는 계속 바뀌므로)
            spinnerArray_String[1].clear()
            areaCodeArray[1].clear()

            var tagAreaCode = false   // 지역 코드 태그
            var tagAreaName = false   // 지역 이름 태그

            var areaCode = ""    // 지역 코드
            var areaName = ""    // 지역 이름

            val factory = XmlPullParserFactory.newInstance()    // 파서 생성
            factory.isNamespaceAware = true                     // 파서 설정
            val xpp = factory.newPullParser()                   // XML 파서

            // 파싱하기
            xpp.setInput(StringReader(page))

            // 파싱 진행
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    val tagName = xpp.name
                    if (tagName.equals("code")) tagAreaCode = true
                    else if (tagName.equals("name")) tagAreaName = true
                }
                else if (eventType == XmlPullParser.TEXT) {
                    if (tagAreaCode) {         // 지역 코드
                        areaCode = xpp.text
                        tagAreaCode = false
                    }
                    else if (tagAreaName) {    // 지역 이름
                        areaName = xpp.text
                        tagAreaName = false

                        // 지역 이름까지 다 읽으면 하나의 데이터 다 읽은 것임
                        val item = ModelAreaCode(areaCode.toInt(), areaName)
                        areaCodeArray[option].add(item)
                        spinnerArray_String[option].add(item.areaName)
                    }
                }
                eventType = xpp.next()
            }
            // 스피너에 데이터 연결
            withContext(Dispatchers.Main) {
                val spinnerAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, spinnerArray_String[option])
                spinnerArr[option].adapter = spinnerAdapter

                val areaSpinnerAdapter = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(option == 0) {
                            // 이 url 주소 가지고 xml에서 데이터 파싱하기
                            fetchXML(requstUrl + "&areaCode=" + areaCodeArray[option][position].areaCode, 1)
                        }
                        Log.d("mmm 스피너 선택",  areaCodeArray[option].get(position).areaName + ", " +areaCodeArray[option].get(position).areaCode)
                    }
                }
                spinnerArr[option].onItemSelectedListener = areaSpinnerAdapter
            }

        }
    } // end of fetchXML
}