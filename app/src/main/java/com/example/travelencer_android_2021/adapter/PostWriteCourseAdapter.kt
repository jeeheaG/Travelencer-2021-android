package com.example.travelencer_android_2021.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import kotlinx.android.synthetic.main.list_item_post_write_course.view.*

class PostWriteCourseAdapter : RecyclerView.Adapter<PostWriteCourseAdapter.ViewHolder>() {
    val courseName = ArrayList<String>()    // 코스 이름
    val courseDate = ArrayList<String>()    // 코스 시간

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostWriteCourseAdapter.ViewHolder {
        // list_item_post_write_course.xml 파일과 연결
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_write_course, parent, false)

        return ViewHolder(itemView).apply {
            // x 이미지 클릭 시 스팟 삭제
            itemView.btnDelete.setOnClickListener {
                // 삭제 확인 알림창 띄우기
                var alert = AlertDialog.Builder(parent.context)
                alert.setTitle("삭제 확인")
                alert.setMessage("삭제하시겠습니까? : \n\n" + courseName[position] + "\n" + courseDate[position])
                // <네> 버튼 누르면
                alert.setPositiveButton("네") { dialog, which ->
                    // 삭제하기
                    courseName.remove(courseName[position])
                    courseDate.remove(courseDate[position])
                    // 데이터 변경 알림
                    notifyDataSetChanged()
                }
                // <아니오> 버튼 누르면 아무 일도 없음
                alert.setNegativeButton("아니오", null)
                alert.show()
            }
        }
    }

    // position 번째 아이템 설정하기
    override fun onBindViewHolder(holder: PostWriteCourseAdapter.ViewHolder, position: Int) {
        holder.setItem(position)
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = courseName.size

    // 데이터 넣어주기
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(position: Int) {
            itemView.tvFoodName.text = courseName[position]

            itemView.tvDate.text = courseDate[position]

            // 맨 첫 번째, 맨 마지막 아이템은 선 지우기
            if (position == 0) itemView.imgFirst.visibility = View.INVISIBLE
            else if (position == itemCount-1) itemView.imgLast.visibility = View.INVISIBLE
        }
    }
}