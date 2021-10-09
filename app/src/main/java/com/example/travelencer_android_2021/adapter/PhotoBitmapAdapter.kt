package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R

class PhotoBitmapAdapter(private val photoList: ArrayList<Bitmap>, private val mContext: Context) : RecyclerView.Adapter<PhotoBitmapAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoBitmapAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_detail_photo, parent, false)
        return CustomViewHolder(view).apply{
            //
        }
    }

    override fun onBindViewHolder(holder: PhotoBitmapAdapter.CustomViewHolder, position: Int) {
//        val uri = photoList[position]
        //Glide.with(mContext).load(uri).into(holder.photo)

        val bitmap = photoList[position]
        holder.photo.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val photo: ImageView = itemView.findViewById(R.id.ivPostDetailPhoto)
    }
}