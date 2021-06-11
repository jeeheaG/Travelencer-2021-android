package com.example.travelencer_android_2021.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPlaceMain

class PlaceMainAdapter(val placeList: ArrayList<ModelPlaceMain>): RecyclerView.Adapter<PlaceMainAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceMainAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_place_main, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPosition: Int = adapterPosition
                val place: ModelPlaceMain = placeList.get(curPosition)
                Toast.makeText(parent.context, "이름:${place.name}, 위치:${place.loc}, 설명:${place.explain}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: PlaceMainAdapter.CustomViewHolder, position: Int) {
        holder.img.setImageResource(placeList.get(position).img)
        holder.name.text = placeList.get(position).name
        holder.loc.text = placeList.get(position).loc
        holder.explain.text = placeList.get(position).explain.substring(0,20).plus("...") //설명부분 문자열 자르고 ...붙이기

        holder.img.clipToOutline = true //안드로이드 버전 5 (롤리팝) 이상부터 적용
    }
    override fun getItemCount(): Int {
        return placeList.size
    }

    //뷰홀더
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.ivPlaceMain) //이미지
        val name = itemView.findViewById<TextView>(R.id.tvPlaceItemName) //장소 이름
        val loc = itemView.findViewById<TextView>(R.id.tvPlaceItemLoc) //지역
        val explain = itemView.findViewById<TextView>(R.id.tvPlaceItemExplain) //설명
    }

}