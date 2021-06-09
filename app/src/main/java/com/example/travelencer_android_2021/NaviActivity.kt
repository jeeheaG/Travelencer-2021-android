package com.example.travelencer_android_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_navi.*
import java.sql.Types.NULL

//TODO : home부분 연결, 필터링 한 후 필터 안띄우게 작업하고 코드 정리하기
//private const val TAG_HOME = "home_activity"
private const val TAG_FEED = "feed_fragment"
private const val TAG_PLACE_FILTER = "place_filter_fragment"
private const val TAG_PLACE_MAIN = "place_main_fragment"
private const val TAG_POST_BLOG = "post_blog_fragment"
private const val TAG_SETTING = "setting_fragment"

class NaviActivity : AppCompatActivity() {
    var filtered: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navi)

        //처음 보여줄 프래그먼트 설정
        setFragment(TAG_FEED, FeedFragment())

        //네비게이션 클릭에 따라 프래그먼트 설정하는 함수 호출
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
    fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        //지역을 선택했는지 확인하는 값을 sharedPreferences로부터 가져옴
        val pref = getSharedPreferences("pref",0)
        filtered = pref.getBoolean("filtered", false)
        Log.d("로그----","filtered - ${filtered}")


        //트랜잭션에 fragment들 add
        //placeMain메뉴 이외의 다른 메뉴를 눌렀을 때
        if(tag != TAG_PLACE_MAIN && manager.findFragmentByTag(tag) == null){
            ft.add(R.id.flContainer, fragment, tag)
        }
        //placeMain메뉴를 눌렀고, 지역 필터링이 안된 상태이고, placeFilter가 add되지 않았다면
        else if(tag == TAG_PLACE_MAIN && !filtered && manager.findFragmentByTag(TAG_PLACE_FILTER) == null){
            Log.d("로그 1-f--","placeFilter add")
            ft.add(R.id.flContainer, PlaceFilterFragment(), TAG_PLACE_FILTER)
        }
        //placeMain메뉴를 눌렀고, 지역 필터링이 된 상태이고, placeMain이 add되지 않았다면
        else if(tag == TAG_PLACE_MAIN && filtered && manager.findFragmentByTag(TAG_PLACE_MAIN) == null){
            Log.d("로그 1-m--","placeMain add")
            ft.add(R.id.flContainer, PlaceMainFragment(), TAG_PLACE_MAIN)
        }


        //val home = manager.findFragmentByTag(TAG_HOME)
        val feed = manager.findFragmentByTag(TAG_FEED)
        val placeFilter = manager.findFragmentByTag(TAG_PLACE_FILTER)
        val placeMain = manager.findFragmentByTag(TAG_PLACE_MAIN)
        val postBlog = manager.findFragmentByTag(TAG_POST_BLOG)
        val setting = manager.findFragmentByTag(TAG_SETTING)

        Log.d("로그 2---", "manager에서 tag로 찾은 것들 값 확인. NULL인 건 반영x \n" +
                "feed : ${feed}\n" +
                "placeFilter : ${placeFilter}\n" +
                "placeMain : ${placeMain}\n" +
                "postBlog : ${postBlog}\n" +
                "setting : ${setting}")


        //여기부터는 이전에 add되어있던 프래그먼트, 즉 위 로그에서 null이 나오지 않은 프래그먼트들에 대해서만 작동함
        //모든 프래그먼트 hide
        if(feed != null){
            Log.d("로그 3---", "feed hide")
            ft.hide(feed)
        }
        if(placeFilter != null){
            Log.d("로그 3---", "placeFilter hide")
            ft.hide(placeFilter)
        }
        if(placeMain != null){
            Log.d("로그 3---", "placeMain hide")
            ft.hide(placeMain)
        }
        if(postBlog != null){
            Log.d("로그 3---", "plostBlog hide")
            ft.hide(postBlog)
        }
        if(setting != null){
            Log.d("로그 3---", "setting hide")
            ft.hide(setting)
        }


        //선택한 프래그먼트 show

        if(tag == TAG_FEED){
            if(feed != null){
                Log.d("로그 4---", "feed show")
                ft.show(feed)
            }else{Log.d("로그 4---", "feed가 null")}
        }
        //place부분은 filter체크
        else if(tag == TAG_PLACE_MAIN){
            //filtered가 false면 placeFilter을 show
            if(!filtered){
                Log.d("로그 4-1--", "여기만 계속 실행됨 filtered == false")
                if(placeFilter != null){
                    ft.show(placeFilter)
                }
                else{ Log.d("로그 4-2-f-", "placeFilter가 null")}
            }
            //filtered가 true면, placeMain을 show
            else if(filtered){
                Log.d("로그 4-3--", "filtered == true")
                if(placeMain != null){
                    ft.show(placeMain)
                }
                else{Log.d("로그 4-3-m-", "placeMain가 null")}
            }
        }
        else if(tag == TAG_POST_BLOG){
            if(postBlog != null){
                ft.show(postBlog)
            }else{Log.d("로그 4---", "postBlog가 null")}
        }
        else if(tag == TAG_SETTING){
            if(setting != null){
                ft.show(setting)
            }else{Log.d("로그 4---", "setting가 null")}
        }

        ft.commitAllowingStateLoss()

    }

    override fun onDestroy() {
        super.onDestroy()
        //현재 액티비티 종료 시 필터 해제
        val pref = getSharedPreferences("pref", 0)
        val edit = pref.edit()
        edit.putBoolean("filtered", false)
        edit.apply()
    }

}