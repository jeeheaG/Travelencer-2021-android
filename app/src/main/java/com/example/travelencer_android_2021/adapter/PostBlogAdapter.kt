package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.PostDetailActivity
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPostBlog

class PostBlogAdapter(private val postList: ArrayList<ModelPostBlog>, private val mContext: Context) : RecyclerView.Adapter<PostBlogAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBlogAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_post_blog, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener{
                val curPosition: Int = adapterPosition
                if(curPosition != RecyclerView.NO_POSITION){ // TODO : index=-1 왜 얘만 이런 현상이 . . .
                    val post: ModelPostBlog = postList[curPosition]
                    Toast.makeText(parent.context, "제목 : ${post.title}, 날짜 : ${post.date}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PostBlogAdapter.CustomViewHolder, position: Int) {
        holder.bind(postList[position], mContext)

        holder.itemView.setOnClickListener{
            /*val intent = Intent(mContext, PostDetailActivity::class.java)
            mContext.startActivity(intent)*/
            Intent(mContext, PostDetailActivity::class.java).apply {
                putExtra("postId", postList[position].postId)
                putExtra("uid", postList[position].uid)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run {mContext.startActivity(this)}
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    //뷰홀더
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: ModelPostBlog, mContext: Context) {
            val title = itemView.findViewById<TextView>(R.id.tvPostBlogTitle)
            val date = itemView.findViewById<TextView>(R.id.tvPostBlogDate)
            val icon = itemView.findViewById<ImageView>(R.id.ivPostDetailPlaceIcon)
            val placeName = itemView.findViewById<TextView>(R.id.tvPostDetailPlaceName)
            val location = itemView.findViewById<TextView>(R.id.tvPostDetailPlaceLocation)
            val writing = itemView.findViewById<TextView>(R.id.tvPostBlogWriting)
            val rvPhoto = itemView.findViewById<RecyclerView>(R.id.rvPostBlogPhotoList)

            val strLen = 10

            title.text = post.title
            date.text = post.date
            icon.setImageResource(post.icon!!)
//            placeName.text = post.placeName
//            location.text = post.location
            if(post.placeName!=null){
                placeName.text = if(post.placeName!!.length>strLen){
                    post.placeName!!.substring(0,strLen).plus("...") //10자 이상이면 문자열 자르고 ...붙이기
                } else post.placeName
            }
            if(post.location!=null){
                location.text = if(post.location!!.length>strLen){
                    post.location!!.substring(0,strLen).plus("...") //10자 이상이면 문자열 자르고 ...붙이기
                } else post.location
            }
            writing.text = post.writing

            rvPhoto.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val postBlogPhotoAdapter = PostBlogPhotoAdapter(post.photoList!!, mContext)
            rvPhoto.adapter = postBlogPhotoAdapter
            rvPhoto.setHasFixedSize(true)
            postBlogPhotoAdapter.notifyDataSetChanged()
        }

    }

}