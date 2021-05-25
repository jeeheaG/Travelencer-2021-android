package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.travelencer_android_2021.databinding.ActivityNaviBinding

class NaviActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_navi)

        mBinding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        //네비게이션들을 담을 호스트를 가져오기
        val naviHostFragment = supportFragmentManager.findFragmentById(R.id.navi_host) as NavHostFragment

        val navController = naviHostFragment.navController

        NavigationUI.setupWithNavController(mBinding.navi, navController)
    }
}