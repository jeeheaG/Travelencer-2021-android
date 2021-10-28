package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPostDetailPlace

class PostDetailPlaceAdapter() : RecyclerView.Adapter<PostDetailPlaceAdapter.CustomViewHolder>() {
    var placeList = ArrayList<ModelPostDetailPlace>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDetailPlaceAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_detail_place, parent, false)
        return CustomViewHolder(view).apply {
            //
        }
    }

    override fun onBindViewHolder(holder: PostDetailPlaceAdapter.CustomViewHolder, position: Int) {
        val strLen = 10
        holder.icon.setImageResource(placeList[position].icon)
        holder.name.text = if(placeList[position].name.length>strLen){
            placeList[position].name.substring(0,strLen).plus("...") //10자 이상이면 문자열 자르고 ...붙이기
        } else placeList[position].name
        holder.location.text = if(placeList[position].location.length>strLen){
            placeList[position].location.substring(0,strLen).plus("...") //10자 이상이면 문자열 자르고 ...붙이기
        } else placeList[position].location
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.findViewById<ImageView>(R.id.ivPostDetailPlaceIcon)
        val name = itemView.findViewById<TextView>(R.id.tvPostDetailPlaceName)
        val location = itemView.findViewById<TextView>(R.id.tvPostDetailPlaceLocation)
    }
}
