package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelFeedPhoto
import kotlinx.android.synthetic.main.list_item_feed_photo.view.*

class FeedPhototAdapter : RecyclerView.Adapter<FeedPhototAdapter.ViewHolder>() {
    // ModelFeedPhoto 배열
    var items = ArrayList<ModelFeedPhoto>()

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedPhototAdapter.ViewHolder {
        // list_item_feed_photo.xml 파일과 연결
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feed_photo, parent, false)

//        var width = parent.resources.displayMetrics.widthPixels / 3
//        var imageView = ImageView(parent.context)
//        imageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)

        return ViewHolder(itemView).apply {
            itemView.setOnClickListener {
                Toast.makeText(parent.context, "${items[position].str}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // position 번째 아이템 설정하기
    override fun onBindViewHolder(holder: FeedPhototAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = items.size

    // ModelFeedPhoto 클래스에 데이터 넣어주기
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: ModelFeedPhoto) {
            itemView.imgFeedPhoto.setImageResource(item.img)
        }
    }
}