package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.FeedCourseDetailActivity
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.course.CourseMaker
import kotlinx.android.synthetic.main.list_item_feed_course.view.*

// 여행 피드 - 코스 탭 어댑터
class FeedCourseAdapter(val context : Context) : RecyclerView.Adapter<FeedCourseAdapter.ViewHolder>() {
    // ArrayList<String> = 코스 이름 배열
    var placeNames = ArrayList<ArrayList<String>>() // 코스 장소 이름 배열
//    var courseDates = ArrayList<ArrayList<String>>() // 코스 시간 배열
    var postIds = ArrayList<String>()
    var uids = ArrayList<String>()

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedCourseAdapter.ViewHolder {
        // list_item_feed_course.xml 파일과 연결
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feed_course, parent, false)

        return ViewHolder(itemView).apply {
            itemView.setOnClickListener {
                // postId 전달
                val intent = Intent(context, FeedCourseDetailActivity::class.java)
                intent.putExtra("postId", postIds[position])
                intent.putExtra("uid", uids[position])
                context.startActivity(intent)
            }
        }
    }

    // position 번째 아이템 설정하기
    override fun onBindViewHolder(holder: FeedCourseAdapter.ViewHolder, position: Int) {
        val placeName = placeNames[position]
        holder.setItem(placeName)
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = placeNames.size

    // 코스 생성
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: ArrayList<String>) {
            val layout = itemView.list_item_feed_course_layout

            layout.removeAllViews()
            CourseMaker().makeCourse(item, layout, context)
        }
    }
}