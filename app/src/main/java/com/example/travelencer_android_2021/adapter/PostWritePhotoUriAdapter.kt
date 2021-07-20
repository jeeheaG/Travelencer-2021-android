package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travelencer_android_2021.R

class PostWritePhotoUriAdapter(private val photoList: ArrayList<Uri>, private val mContext: Context) : RecyclerView.Adapter<PostWritePhotoUriAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostWritePhotoUriAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_detail_photo, parent, false)
        return CustomViewHolder(view).apply{
            //
        }
    }

    override fun onBindViewHolder(holder: PostWritePhotoUriAdapter.CustomViewHolder, position: Int) {
        val uri = photoList[position]
        Glide.with(mContext).load(uri).into(holder.photo)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val photo: ImageView = itemView.findViewById(R.id.ivPostDetailPhoto)
    }
}