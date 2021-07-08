package com.example.travelencer_android_2021

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

// 스플래시
class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1초간 스플래시 보여주기
        val backgroundExecutable : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val mainExecutor : Executor = ContextCompat.getMainExecutor(this@Splash)
        backgroundExecutable.schedule({
            mainExecutor.execute {
                // NaviActivity 넘어가기
                var intent = Intent(this@Splash, NaviActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1, TimeUnit.SECONDS)
    }
}