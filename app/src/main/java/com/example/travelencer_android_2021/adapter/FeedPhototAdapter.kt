package com.example.travelencer_android_2021.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelFeedPhoto
import kotlinx.android.synthetic.main.list_item_feed_photo.view.*

private const val TAG = "mmm"

// 여행 피드 - 사진 탭 어댑터
class FeedPhototAdapter : RecyclerView.Adapter<FeedPhototAdapter.ViewHolder>() {
    // ModelFeedPhoto 배열
    var items = ArrayList<ModelFeedPhoto>()

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedPhototAdapter.ViewHolder {
        // list_item_feed_photo.xml 파일과 연결
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feed_photo, parent, false)
        val imgFeedPhoto = itemView.findViewById<ImageView>(R.id.imgFeedPhoto)

        // 사진 리사이징
        val width = parent.resources.displayMetrics.widthPixels / 3             // 사용자 화면의 가로 길이 / 3

        // 이미지뷰의 가로, 세로 크기 지정(정사각형)
        imgFeedPhoto.layoutParams = LinearLayoutCompat.LayoutParams(width, width)

        return ViewHolder(itemView).apply {
            itemView.setOnClickListener {
                Toast.makeText(parent.context, "${items[position].postId} 클릭", Toast.LENGTH_SHORT).show()
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

    // ModelFeedPhoto 클래스의 데이터(사진) 로드하기
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: ModelFeedPhoto) {
            Glide.with(itemView)
                    .load(item.uri)
                    .error(R.drawable.ic_x_red)                  // 오류 시 이미지
                    .apply(RequestOptions().centerCrop())
                    .into(itemView.imgFeedPhoto)
        }
    }
}