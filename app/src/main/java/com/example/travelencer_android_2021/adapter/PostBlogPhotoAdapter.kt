package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPostBlogPhoto
import kotlinx.android.synthetic.main.list_item_post_blog_photo.view.*

class PostBlogPhotoAdapter(private val photoList: ArrayList<ModelPostBlogPhoto>, private val mContext: Context) : RecyclerView.Adapter<PostBlogPhotoAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBlogPhotoAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_blog_photo, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener{
                val curPosition: Int = adapterPosition
                val photo: ModelPostBlogPhoto = photoList[curPosition]
                Toast.makeText(parent.context, "${curPosition} Photo ${photo.photo} is clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindViewHolder(holder: PostBlogPhotoAdapter.CustomViewHolder, position: Int) {
        holder.setItem(photoList[position])

//        holder.photo.setImageBitmap(photoList[position].photo)

//        holder.photo.setImageResource(photoList[position].photo)
//        Glide.with(mContext)
//                .load(photoList[position].photo)
//                .into(holder.photo)


    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    //뷰홀더
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo = itemView.findViewById<ImageView>(R.id.ivPostBlogPhoto)

        fun setItem(item: ModelPostBlogPhoto) {
            Glide.with(itemView)
                    .load(item.photo)
                    .error(R.drawable.ic_x_red)                  // 오류 시 이미지
//                    .apply(RequestOptions().centerCrop())
                    .into(itemView.ivPostBlogPhoto)
        }
    }
}