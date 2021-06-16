package com.example.travelencer_android_2021.course

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.example.travelencer_android_2021.R

class CoursePiece(mContext: Context, private val type: String, private val name: String) : LinearLayout(mContext) {

    init {
        when(type) {
            "start" -> {
                val const = inflate(mContext, R.layout.item_course_spot_start, this)
                const.findViewById<TextView>(R.id.tvCourseSpotName).text = name
            }
            "middle" -> {
                val const = inflate(mContext, R.layout.item_course_spot_middle, this)
                const.findViewById<TextView>(R.id.tvCourseSpotName).text = name
            }
            "end" -> {
                val const = inflate(mContext, R.layout.item_course_spot_end, this)
                const.findViewById<TextView>(R.id.tvCourseSpotName).text = name
            }
        }
    }

}