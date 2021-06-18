package com.example.travelencer_android_2021.course

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout

class CourseMaker() {
    private val cTypeFirst = "first"
    private val cTypeOdd = "odd"
    private val cTypeEven = "even"

    private val pTypeStart = "start"
    private val pTypeMiddle = "middle"
    private val pTypeEnd = "end"
    private val pTypeRightUp = "right up"
    private val pTypeRightDown = "right down"
    private val pTypeLeftUp = "left up"
    private val pTypeLeftDown = "left down"
    private val pTypeRightDownEnd = "right down end"
    private val pTypeLeftDownEnd = "left down end"

    fun makeCourse(spotNameList: ArrayList<String>, linear: LinearLayout, mContext: Context){
        val spotNameListLen = spotNameList.size
        var type = cTypeOdd

        // 전체 스팟이 5개 이상
        if(spotNameListLen > 4){
            // 첫 줄 ~ 중간 줄 4개씩 잘라 보내기 (마지막줄만 제외)
            for(i in 0..(spotNameListLen/4 -1)){ // 13: 0 1 2  12: 0 1 2..
                val spotNameListSplit = ArrayList(spotNameList.subList(i*4, (i+1)*4))
                if(i==0){ // 첫 줄
                    linear.addView(makeCourseLayout(cTypeFirst, spotNameListSplit, true, mContext))
                }
                else if(i == (spotNameListLen/4 -1) && spotNameListLen%4 == 0){ // 마지막줄이 4개로 딱 떨어질 때
                    break
                }
                else{ //중간줄
                    linear.addView(makeCourseLayout(type, spotNameListSplit, false, mContext))
                }

                when(type){
                    cTypeOdd -> type = cTypeEven
                    cTypeEven -> type = cTypeOdd
                }
            }
            val lastLen =
                    if(spotNameListLen%4 == 0){ // 마지막줄이 4개로 딱 떨어질 때
                        (spotNameListLen/4 -1)*4
                    }else{ // 마지막줄이 1~3개일 때
                        (spotNameListLen/4)*4
                    }
            // 마지막줄
            val spotNameListSplit = ArrayList(spotNameList.subList(lastLen, spotNameListLen))
            linear.addView(makeCourseLayout(type, spotNameListSplit, true, mContext))
        }

        // 전체 스팟이 4개이하
        else if(spotNameListLen <= 4){
            linear.addView(makeCourseLayout(cTypeFirst, spotNameList, false, mContext))
        }

    }

    // 코스의 한 줄을 경우에 따라 다르게 생성해서 반환하는 함수 (스팟이 0~1개인 코스는 없다고 가정)
    fun makeCourseLayout(type: String, spotNameList: ArrayList<String>, diffEdge: Boolean, mContext: Context): LinearLayout {
        val numInLine = spotNameList.size
        val courseLayout = LinearLayout(mContext)
        courseLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL)
        courseLayout.setVerticalGravity(Gravity.BOTTOM)
        var coursePiece1: CoursePiece? = null
        var coursePiece2: CoursePiece? = null
        var coursePiece3: CoursePiece? = null
        var coursePiece4: CoursePiece? = null

        // 전체 스팟 4개이하로 커브가 없는 코스
        if(type == cTypeFirst){
            coursePiece1 = CoursePiece(mContext, pTypeStart, spotNameList[0])

            if(numInLine >= 2){
                if(numInLine >= 3){
                    coursePiece2 = CoursePiece(mContext, pTypeMiddle, spotNameList[1])

                    if(numInLine == 4){ //numInLine == 4
                        coursePiece3 = CoursePiece(mContext, pTypeMiddle, spotNameList[2])
                        coursePiece4 =
                                if(!diffEdge){
                                    CoursePiece(mContext, pTypeEnd, spotNameList[3])
                                }else{
                                    CoursePiece(mContext, pTypeRightUp, spotNameList[3])
                                }

                    }else{ //numInLine == 3
                        coursePiece3 = CoursePiece(mContext, pTypeEnd, spotNameList[2])
                    }
                }else{ // numInLine == 2
                    coursePiece2 = CoursePiece(mContext, pTypeEnd, spotNameList[1])
                }
            }
        }

        // 왼쪽에서 오른쪽으로 가는(홀수번째) 코스 한 줄
        else if(type == cTypeOdd){
            // spotName 4개 채우기
            if(numInLine < 4){
                for(i in 0..3-numInLine){
                    spotNameList.add("")
                }
            }

            //coursePiece들 초기화
            coursePiece1 = CoursePiece(mContext, pTypeLeftDown, spotNameList[0])
            coursePiece2 = CoursePiece(mContext, pTypeMiddle, spotNameList[1])
            coursePiece3 = CoursePiece(mContext, pTypeMiddle, spotNameList[2])
            coursePiece4 =
                    if(!diffEdge){
                        CoursePiece(mContext, pTypeRightUp, spotNameList[3])
                    }else{
                        CoursePiece(mContext, pTypeEnd, spotNameList[3])
                    }

            //diffEdge일 경우 numInLine에 따라 coursePiece들 - end형태로 변경, visibility 변경
            if(diffEdge){
                if(numInLine <= 3){
                    coursePiece4.visibility = View.INVISIBLE
                    if(numInLine <= 2){
                        coursePiece3.visibility = View.INVISIBLE
                        if(numInLine == 1){ //numInLine == 1
                            coursePiece2.visibility = View.INVISIBLE
                            coursePiece1 = CoursePiece(mContext, pTypeLeftDownEnd, spotNameList[0])
                        }else{ // numInLine == 2
                            coursePiece2 = CoursePiece(mContext, pTypeEnd, spotNameList[1])
                        }
                    }else{ // numInLine == 3
                        coursePiece3 = CoursePiece(mContext, pTypeEnd, spotNameList[2])
                    }
                }
            }

        }

        // 오른쪽에서 왼쪽으로 가는(짝수번째) 코스 한 줄
        else if(type == cTypeEven){
            if(numInLine < 4){
                for(i in 0..3-numInLine){
                    spotNameList.add("")
                }
            }

            coursePiece1 = CoursePiece(mContext, pTypeRightDown, spotNameList[0])
            coursePiece2 = CoursePiece(mContext, pTypeMiddle, spotNameList[1])
            coursePiece3 = CoursePiece(mContext, pTypeMiddle, spotNameList[2])
            coursePiece4 =
                    if(!diffEdge){
                        CoursePiece(mContext, pTypeLeftUp, spotNameList[3])
                    }else{
                        CoursePiece(mContext, pTypeStart, spotNameList[3])
                    }

            if(diffEdge){
                if(numInLine <= 3){
                    coursePiece4.visibility = View.INVISIBLE
                    if(numInLine <= 2){
                        coursePiece3.visibility = View.INVISIBLE
                        if(numInLine == 1){ //numInLine == 1
                            coursePiece2.visibility = View.INVISIBLE
                            coursePiece1 = CoursePiece(mContext, pTypeRightDownEnd, spotNameList[0])
                        }else{ // numInLine == 2
                            coursePiece2 = CoursePiece(mContext, pTypeStart, spotNameList[1])
                        }
                    }else{ // numInLine == 3
                        coursePiece3 = CoursePiece(mContext, pTypeStart, spotNameList[2])
                    }
                }
            }
        }

        if(type == cTypeEven){
            courseLayout.addView(coursePiece4)
            courseLayout.addView(coursePiece3)
            courseLayout.addView(coursePiece2)
            courseLayout.addView(coursePiece1)
        }else{
            coursePiece1?.let { courseLayout.addView(it) }
            coursePiece2?.let { courseLayout.addView(it) }
            coursePiece3?.let { courseLayout.addView(it) }
            coursePiece4?.let { courseLayout.addView(it) }
        }

        return courseLayout
    }
}