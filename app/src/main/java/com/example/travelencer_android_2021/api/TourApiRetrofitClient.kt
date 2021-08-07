package com.example.travelencer_android_2021.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object TourApiRetrofitClient {
    //okhttp와 httpLoggingInterceptor
    var interceptor = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
        override fun log(message: String) { Log.e("", message)
        }
    })

    var client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()


    //retrofit
    private val tourRetrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(TourApi.BASE_URL)
            .client(client) //okhttp
            .addConverterFactory(GsonConverterFactory.create())
    }

    val tourApiService: TourApiInterface by lazy {
        tourRetrofit
            .build()
            .create(TourApiInterface::class.java)
    }

/*    val tourApiDetailCommonService: TourApiDetailCommonInterface by lazy {
        tourRetrofit
                .build()
                .create(TourApiInterface::class.java)
    }*/

}