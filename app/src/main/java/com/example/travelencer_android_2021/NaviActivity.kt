package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_navi.*

//TODO : home부분 연결
//private const val TAG_HOME = "home_activity"
private const val TAG_FEED = "feed_fragment"
private const val TAG_PLACE_MAIN = "place_main_fragment"
private const val TAG_POST_BLOG = "post_blog_fragment"
private const val TAG_SETTING = "setting_fragment"

class NaviActivity : AppCompatActivity() {
//j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2j2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navi)

        //처음 보여줄 프래그먼트 설정
        setFragment(TAG_FEED, FeedFragment())

        //네비게이션 클릭에 따라 프래그먼트 설정
        navi.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {

                //R.id.homeFragment -> setFragment(TAG_HOME, )
                R.id.feedFragment -> setFragment(TAG_FEED, FeedFragment())
                R.id.placeMainFragment -> setFragment(TAG_PLACE_MAIN, PlaceMainFragment())
                R.id.postBlogFragment -> setFragment(TAG_POST_BLOG, PostBlogFragment())
                R.id.settingFragment -> setFragment(TAG_SETTING, SettingFragment())
            }
            true
        }

    }


    //네비게이션 클릭에 따라 보여줄 프래그먼트 변화시키면서 프래그먼트 상태는 유지하는 함수
    private fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val fragTrans: FragmentTransaction = manager.beginTransaction()

        //null이면 add
        if(manager.findFragmentByTag(tag) == null){
            fragTrans.add(R.id.flContainer, fragment, tag)
        }

        //val home = manager.findFragmentByTag(TAG_HOME)
        val feed = manager.findFragmentByTag(TAG_FEED)
        val placeMain = manager.findFragmentByTag(TAG_PLACE_MAIN)
        val postBlog = manager.findFragmentByTag(TAG_POST_BLOG)
        val setting = manager.findFragmentByTag(TAG_SETTING)


        //모든 프래그먼트 hide
        if(feed != null){
            fragTrans.hide(feed)
        }
        if(placeMain != null){
            fragTrans.hide(placeMain)
        }
        if(postBlog != null){
            fragTrans.hide(postBlog)
        }
        if(setting != null){
            fragTrans.hide(setting)
        }

        //선택한 프래그먼트 show
        if(tag == TAG_FEED){
            if(feed != null){
                fragTrans.show(feed)
            }
        }
        else if(tag == TAG_PLACE_MAIN){
            if(placeMain != null){
                fragTrans.show(placeMain)
            }
        }
        else if(tag == TAG_POST_BLOG){
            if(postBlog != null){
                fragTrans.show(postBlog)
            }
        }
        else if(tag == TAG_SETTING){
            if(setting != null){
                fragTrans.show(setting)
            }
        }

        fragTrans.commitAllowingStateLoss()

    }

}