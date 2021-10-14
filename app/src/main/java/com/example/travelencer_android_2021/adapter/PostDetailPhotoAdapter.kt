package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelCasePhotoOnly

class PostDetailPhotoAdapter(private val mContext: Context) : RecyclerView.Adapter<PostDetailPhotoAdapter.CustomViewHolder>() {
//    var photoListUri = ArrayList<Uri>()
    var photoListBitmap = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDetailPhotoAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_detail_photo, parent, false)
        return CustomViewHolder(view).apply{
            //
        }
    }

    override fun onBindViewHolder(holder: PostDetailPhotoAdapter.CustomViewHolder, position: Int) {
//        Glide.with(mContext).load(photoListUri[position]).into(holder.photo)
        holder.photo.setImageBitmap(photoListBitmap[position])
    }

    override fun getItemCount(): Int {
        return photoListBitmap.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val photo = itemView.findViewById<ImageView>(R.id.ivPostDetailPhoto)
    }
}
