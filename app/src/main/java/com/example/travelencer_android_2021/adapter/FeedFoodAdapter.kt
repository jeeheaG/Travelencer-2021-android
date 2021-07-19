package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelCourseSpot
import kotlinx.android.synthetic.main.list_item_feed_food.view.*

// 여행 피드 - 맛집 탭 어댑터
class FeedFoodAdapter : RecyclerView.Adapter<FeedFoodAdapter.ViewHolder>() {
    // ModelCourseSpot 배열
    var items = ArrayList<ModelCourseSpot>()

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedFoodAdapter.ViewHolder {
        // list_item_feed_food.xml 파일과 연결
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feed_food, parent, false)

        return ViewHolder(itemView).apply {
            itemView.setOnClickListener {
                Toast.makeText(parent.context, "${items[position].name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // position 번째 아이템 설정하기
    override fun onBindViewHolder(holder: FeedFoodAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = items.size

    // ModelCourseSpot 클래스에 데이터 넣어주기
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: ModelCourseSpot) {
            itemView.tvName.text = item.name
            itemView.tvLocation.text = item.location
        }
    }
}