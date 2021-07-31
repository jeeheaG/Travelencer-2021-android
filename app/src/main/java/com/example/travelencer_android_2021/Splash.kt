package com.example.travelencer_android_2021

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

// 스플래시
class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 권한 설정하기
        setPermission()
    }

    // tedpermission 설정
    private fun setPermission() {
        // 권한 묻는 팝업 만들기
        val permission = object : PermissionListener {
            // 설정해놓은 권한을 허용됐을 때
            override fun onPermissionGranted() {
                // 1초간 스플래시 보여주기
                val backgroundExecutable : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
                val mainExecutor : Executor = ContextCompat.getMainExecutor(this@Splash)
                backgroundExecutable.schedule({
                    mainExecutor.execute {
                        // MainActivity 넘어가기
                        val intent = Intent(this@Splash, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, 1, TimeUnit.SECONDS)

                Toast.makeText(this@Splash, "권한 허용", Toast.LENGTH_SHORT).show()
            }

            // 설정해놓은 권한을 거부됐을 때
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                // 권한 없어서 요청
                AlertDialog.Builder(this@Splash)
                        .setMessage("권한 거절로 인해 일부 기능이 제한됩니다.")
                        .setPositiveButton("권한 설정하러 가기") { dialog, which ->
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:com.example.travelencer_android_2021"))
                                startActivity(intent)
                            } catch (e : ActivityNotFoundException) {
                                e.printStackTrace()
                                val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                                startActivity(intent)
                            }
                        }
                        .show()

                Toast.makeText(this@Splash, "권한 거부", Toast.LENGTH_SHORT).show()
            }

        }

        // 권한 설정
        TedPermission.with(this@Splash)
                .setPermissionListener(permission)
                .setRationaleMessage("Travelencer를 이용하기 위해 권한을 허용해주세요.")
                .setDeniedMessage("권한을 거부하셨습니다. [앱 설정]->[권한] 항목에서 허용해주세요.")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) // 필수 권한만
                .check()
    }
}