package com.example.travelencer_android_2021.course

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.travelencer_android_2021.R

class CoursePiece(mContext: Context, type: String, name: String) : LinearLayout(mContext) {

    init {
        when(type) {
            "start" -> {
                inflate(mContext, R.layout.item_course_start, this)
            }
            "middle" -> {
                inflate(mContext, R.layout.item_course_middle, this)
            }
            "end" -> {
                inflate(mContext, R.layout.item_course_end, this)
            }
            "right up" -> {
                inflate(mContext, R.layout.item_course_curve_right_up, this)
            }
            "right down" -> {
                inflate(mContext, R.layout.item_course_curve_right_down, this)
            }
            "left up" -> {
                inflate(mContext, R.layout.item_course_curve_left_up, this)
            }
            "left down" -> {
                inflate(mContext, R.layout.item_course_curve_left_down, this)
            }
            "right down end" -> {
                inflate(mContext, R.layout.item_course_curve_right_down_end, this)
            }
            "left down end" -> {
                inflate(mContext, R.layout.item_course_curve_left_down_end, this)
            }
        }
        setName(name)
    }

    private fun setName(name: String){
        if(name.length > 5){
            var nameLine: String = ""
            val len = name.length
            for( i in 0 .. (len/5 -1) ){
                nameLine = nameLine.plus(name.substring(i*5,(i+1)*5))
                if((i == len/5 -1) && (len%5 == 0)){
                    break
                }
                nameLine = nameLine.plus("\n")
            }
            nameLine = nameLine.plus(name.substring((len/5)*5))
            findViewById<TextView>(R.id.tvCourseSpotName).text = nameLine
        }
        else{
            findViewById<TextView>(R.id.tvCourseSpotName).text = name
        }
    }

}