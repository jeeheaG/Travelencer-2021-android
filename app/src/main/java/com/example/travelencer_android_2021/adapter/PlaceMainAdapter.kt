package com.example.travelencer_android_2021.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPlaceMain
import kotlinx.android.synthetic.main.list_item_place_main.view.*

//TODO : 설명글 앞부분 일부만 잘라야 함
class PlaceMainAdapter(val placeList: ArrayList<ModelPlaceMain>): RecyclerView.Adapter<PlaceMainAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceMainAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_place_main, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPosition: Int = adapterPosition
                val place: ModelPlaceMain = placeList.get(curPosition)
                Toast.makeText(parent.context, "이름:${place.name}, 위치:${place.loc}, 설명:${place.explain}", Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onBindViewHolder(holder: PlaceMainAdapter.CustomViewHolder, position: Int) {
        holder.img.setImageResource(placeList.get(position).img)
        holder.name.text = placeList.get(position).name
        holder.loc.text = placeList.get(position).loc
        holder.explain.text = placeList.get(position).explain
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