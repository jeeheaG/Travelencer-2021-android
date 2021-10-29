package com.example.travelencer_android_2021

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tos.*

// 서비스 이용 약관 액티비티
class TOSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tos)

        // 뒤로가기 이미지 클릭
        imgBack.setOnClickListener {
            finish()
        }

        // 웹뷰 시작
        val mWebView = findViewById<View>(R.id.webview) as WebView
        mWebView.webViewClient = WebViewClient()

        val mWebSettings = mWebView.getSettings() //각종 환경 설정 가능여부
        mWebSettings.javaScriptEnabled = true // 자바스크립트 허용여부
        mWebSettings.setSupportMultipleWindows(false) // 윈도우 여러개 사용여부
        mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 컨텐츠사이즈 맞추기
        mWebSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK // 캐시 허용 여부
        mWebSettings.useWideViewPort = true // wide viewport 사용 여부
        mWebSettings.setSupportZoom(true) // Zoom사용여부
        mWebSettings.javaScriptCanOpenWindowsAutomatically = false // 자바스크립트가 window.open()사용할수있는지 여부
        mWebSettings.loadWithOverviewMode = true // 메타태그 허용 여부
        mWebSettings.builtInZoomControls = true // 화면 확대 축소 허용 여부
        mWebSettings.domStorageEnabled = true // 로컬저장소 허용 여부
        mWebView.loadUrl("file:///android_asset/ppa.html") // 웹뷰 사이트 주소 및 시작

        }
}