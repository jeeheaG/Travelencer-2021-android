package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.course.CourseMaker
import kotlinx.android.synthetic.main.list_item_feed_course.view.*

class FeedCourseAdapter(val context : Context) : RecyclerView.Adapter<FeedCourseAdapter.ViewHolder>() {
    // ArrayList<String> 배열
    var items = ArrayList<ArrayList<String>>()

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedCourseAdapter.ViewHolder {
        // list_item_feed_course.xml 파일과 연결
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feed_course, parent, false)

        return ViewHolder(itemView).apply {
            itemView.setOnClickListener {
                Toast.makeText(parent.context, "클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // position 번째 아이템 설정하기
    override fun onBindViewHolder(holder: FeedCourseAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
        Log.d("mmm check", "포지션 ${position}")
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = items.size

    // 코스 생성
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: ArrayList<String>) {
            var layout = itemView.list_item_feed_course_layout

            CourseMaker().makeCourse(item, layout, context)
            Log.d("mmm check", "배열 ${item[0]}")

            Log.d("mmm size", "${getItemCount()}")
        }
    }
}