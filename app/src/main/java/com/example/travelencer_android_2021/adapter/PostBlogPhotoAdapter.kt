package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPostBlogPhoto

class PostBlogPhotoAdapter(val photoList: ArrayList<ModelPostBlogPhoto>) : RecyclerView.Adapter<PostBlogPhotoAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBlogPhotoAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_blog_photo, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener{
                val curPosition: Int = adapterPosition
                val photo: ModelPostBlogPhoto = photoList.get(curPosition)
                Toast.makeText(parent.context, "${curPosition} Photo ${photo.photo} is clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindViewHolder(holder: PostBlogPhotoAdapter.CustomViewHolder, position: Int) {
        holder.photo.setImageResource(photoList.get(position).photo)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    //뷰홀더
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo = itemView.findViewById<ImageView>(R.id.ivPostBlogPhoto)
    }
}