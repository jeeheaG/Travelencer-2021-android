package com.example.travelencer_android_2021.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
//크롬 테스트 했던 url
//http://152.70.95.197:3001/place/register?plcName=a&plcProduce=a&plcAddress=a&plcCategory=0&plcPicture=a&plcGood=a&plcBad=a&locX=127.07012&locY=37.29222
//http://152.70.95.197:3000/user/login?email=%22j@h.com%22&password=%22000000%22

//okhttp와 httpLoggingInterceptor
object RetrofitClient {
    var interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.e("", message)
        }
    })

    private var client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS) //5->10->15 다
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()


    // User 서버 연결
    private val retrofitUser = Retrofit.Builder()
            .baseUrl("http://152.70.95.197:3000")
//            .baseUrl("http://152.70.95.197:3001")
            .client(client) //okhttp
            .addConverterFactory(GsonConverterFactory.create())

    val serviceApiUser: ServiceApiInterface by lazy {
        retrofitUser.build().create(ServiceApiInterface::class.java)
    }

    // Setting 서버 연결
    private val retrofitSetting = Retrofit.Builder()
        .baseUrl("http://152.70.95.197:3003")
        .client(client) //okhttp
        .addConverterFactory(GsonConverterFactory.create())

    val serviceApiSetting: ServiceApiInterface by lazy {
        retrofitSetting.build().create(ServiceApiInterface::class.java)
    }

}