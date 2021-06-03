package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_navi.*

/*//TODO : home부분 연결
//private const val TAG_HOME = "home_activity"
private const val TAG_FEED = "feed_fragment"
private const val TAG_PLACE_MAIN = "place_main_fragment"
private const val TAG_POST_BLOG = "post_blog_fragment"
private const val TAG_SETTING = "setting_fragment"*/

class NaviActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navi)

        //처음 보여줄 프래그먼트 설정
        with(supportFragmentManager.beginTransaction()) {
            var feedFragment = FeedFragment()
            replace(R.id.flContainer, feedFragment)
            //add(R.id.flContainer, feedFragment)
            //addToBackStack(null)
            commit()
        }

        //네비게이션 클릭에 따라 프래그먼트 설정
        navi.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.feedFragment -> {
                    with(supportFragmentManager.beginTransaction()){
                        var feedFragment = FeedFragment()
                        replace(R.id.flContainer, feedFragment)
                        //add(R.id.flContainer, feedFragment)
                        //addToBackStack(null)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.placeMainFragment -> {
                    with(supportFragmentManager.beginTransaction()){
                        var placeMainFragment = PlaceMainFragment()
                        replace(R.id.flContainer, placeMainFragment)
                        //add(R.id.flContainer, placeMainFragment)
                        //addToBackStack(null)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.postBlogFragment -> {
                    with(supportFragmentManager.beginTransaction()){
                        var postBlogFragment = PostBlogFragment()
                        replace(R.id.flContainer, postBlogFragment)
                        //add(R.id.flContainer, postBlogFragment)
                        //addToBackStack(null)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.settingFragment -> {
                    with(supportFragmentManager.beginTransaction()){
                        var settingFragment = SettingFragment()
                        replace(R.id.flContainer, settingFragment)
                        //add(R.id.flContainer, settingFragment)
                        //addToBackStack(null)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

    }


}