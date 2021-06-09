package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPostBlog

class PostBlogAdapter(val postList: ArrayList<ModelPostBlog>) : RecyclerView.Adapter<PostBlogAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBlogAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_blog, parent, false)
        return CustomViewHolder(view).apply {
            val curPosition: Int = adapterPosition
            if(curPosition != RecyclerView.NO_POSITION){ // TODO : 왜 얘만 이런 현상이 . . .
                val post: ModelPostBlog = postList.get(curPosition)
                Toast.makeText(parent.context, "제목 : ${post.title}, 날짜 : ${post.date}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindViewHolder(holder: PostBlogAdapter.CustomViewHolder, position: Int) {
        holder.title.text = postList.get(position).title
        holder.date.text = postList.get(position).date
        holder.icon.setImageResource(postList.get(position).icon)
        holder.placeName.text = postList.get(position).placeName
        holder.location.text = postList.get(position).location
        holder.writing.text = postList.get(position).writing.substring(0,42).plus("...")
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvPostBlogTitle)
        val date = itemView.findViewById<TextView>(R.id.tvPostBlogDate)
        val icon = itemView.findViewById<ImageView>(R.id.ivPostBlogLocationIcon)
        val placeName = itemView.findViewById<TextView>(R.id.tvPostBlogPlaceName)
        val location = itemView.findViewById<TextView>(R.id.tvPostBlogLocation)
        val writing = itemView.findViewById<TextView>(R.id.tvPostBlogWriting)

    }

}