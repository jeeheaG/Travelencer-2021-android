package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPostDetailPlace

class PostDetailPlaceAdapter(private val placeList: ArrayList<ModelPostDetailPlace>) : RecyclerView.Adapter<PostDetailPlaceAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDetailPlaceAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_detail_place, parent, false)
        return CustomViewHolder(view).apply {
            //
        }
    }

    override fun onBindViewHolder(holder: PostDetailPlaceAdapter.CustomViewHolder, position: Int) {
        holder.icon.setImageResource(placeList[position].icon!!)
        holder.name.text = placeList[position].name
        holder.location.text = placeList[position].location
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