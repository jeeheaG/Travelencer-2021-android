package com.example.travelencer_android_2021.adapter

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPlaceDetailRecentPost

class PlaceDetailRecentPostAdapter(val postList: ArrayList<ModelPlaceDetailRecentPost>) : RecyclerView.Adapter<PlaceDetailRecentPostAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_place_detail_recent_post, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener{
                val curPosition: Int = adapterPosition
                val post: ModelPlaceDetailRecentPost = postList[curPosition]
                //토스트 등..
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.title.text = postList[position].title
        holder.nickname.text = "by ".plus(postList[position].nickname)
        holder.profileImg.setImageResource(postList[position].profileImg)

        holder.setProfileImgRound()
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvPlaceDetailRecentPostTitle)
        val nickname = itemView.findViewById<TextView>(R.id.tvPlaceDetailRecentPostNickname)
        val profileImg = itemView.findViewById<ImageView>(R.id.ivPlaceDetailPostRecentProfileImg)

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun setProfileImgRound(){
            val profileImg = itemView.findViewById<ImageView>(R.id.ivPlaceDetailPostRecentProfileImg)

            profileImg.background = ShapeDrawable(OvalShape())
            profileImg.clipToOutline = true //안드로이드 버전 5(롤리팝)이상에서만 적용
        }
    }
}