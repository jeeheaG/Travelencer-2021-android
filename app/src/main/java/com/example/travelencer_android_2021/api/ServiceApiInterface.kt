package com.example.travelencer_android_2021.api

import com.example.travelencer_android_2021.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceApiInterface {
    //장소 등록
    //사진은 MultipartBody.Part 로 보내야 하는데, 사진과 다른 텍스트 등의 데이터를 같이 보내고 싶으면 RequestBody로 만들어서 보내야 한다고 함.
    //여러 문자열을 HashMap에 담아 보내는데, HashMap의 키값String은 서버의 변수값과 동일하게 써줘야 함!!
    //**RequestBody에는 int, double등의 숫자형 데이터가 안 들어가서 문자로 변환 후 넣어줘야 한다고 함.
    @Multipart
    @POST("/place/register")
/*    fun placeRegister(@Part image: MultipartBody.Part?,
                      @PartMap data: HashMap<String, RequestBody>)
    : Call<PlaceRegisterResponse>*/
    fun placeRegister(@Part image: MultipartBody.Part?)
    : Call<PlaceRegisterResponse>

/*    //장소 등록
    @POST("/place/register")
    fun placeRegister(@Body data: PlaceRegisterData): Call<PlaceRegisterResponse>*/


}