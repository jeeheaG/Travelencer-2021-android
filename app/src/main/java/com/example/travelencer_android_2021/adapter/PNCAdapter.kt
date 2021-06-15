package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPNC

class PNCAdapter(private val pncList: ArrayList<ModelPNC>) : RecyclerView.Adapter<PNCAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PNCAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pnc, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener{
                //..
            }
        }
    }

    override fun onBindViewHolder(holder: PNCAdapter.CustomViewHolder, position: Int) {
        holder.icon.setImageResource(pncList[position].icon)
        holder.comment.text = pncList[position].comment
    }

    override fun getItemCount(): Int {
        return pncList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.findViewById<ImageView>(R.id.ivPNCIcon)
        val comment = itemView.findViewById<TextView>(R.id.tvPNCComment)
    }
}