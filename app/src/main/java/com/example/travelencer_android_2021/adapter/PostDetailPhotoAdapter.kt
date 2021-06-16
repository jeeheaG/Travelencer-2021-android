package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelCasePhotoOnly

class PostDetailPhotoAdapter(private val photoList: ArrayList<ModelCasePhotoOnly>) : RecyclerView.Adapter<PostDetailPhotoAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDetailPhotoAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_detail_photo, parent, false)
        return CustomViewHolder(view).apply{
            //
        }
    }

    override fun onBindViewHolder(holder: PostDetailPhotoAdapter.CustomViewHolder, position: Int) {
        holder.photo.setImageResource(photoList[position].photo)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val photo = itemView.findViewById<ImageView>(R.id.ivPostDetailPhoto)
    }
}