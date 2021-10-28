package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travelencer_android_2021.PlaceDetailActivity
import com.example.travelencer_android_2021.R
import com.example.travelencer_android_2021.model.ModelPlaceMainCard

class PlaceMainAdapter(private val placeList: ArrayList<ModelPlaceMainCard>, private val mContext: Context): RecyclerView.Adapter<PlaceMainAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceMainAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_place_main, parent, false)
        return CustomViewHolder(view).apply {
//            Log.d("로그Tour PlaceMainAdapter", "onCreateViewHolder실행")
//            for (i in placeList){
//                Log.d("로그Tour PlaceMainAdapter", "${i.name}, ${i.loc}")
//            }
            itemView.setOnClickListener {
                val curPosition: Int = adapterPosition
                val place: ModelPlaceMainCard = placeList[curPosition]
                val intent = Intent(mContext, PlaceDetailActivity::class.java)
                intent.putExtra("contentId", place.contentId)
                intent.putExtra("isTour", place.isTour)
                mContext.startActivity(intent)
                //Toast.makeText(parent.context, "이름:${place.name}, 위치:${place.loc}, 설명:${place.explain}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: PlaceMainAdapter.CustomViewHolder, position: Int) {
        val strLen1 = 10
        val strLen2 = 20
        //Log.d("로그Tour PlaceMainAdapter", "onBindViewHolder실행")
        Glide.with(mContext).load(placeList[position].img).into(holder.img)
        holder.name.text = if(placeList[position].name.length>strLen1){
            placeList[position].name.substring(0,strLen1).plus("...") //10자 이상이면 문자열 자르고 ...붙이기
        } else placeList[position].name
        holder.loc.text = if(placeList[position].loc.length>strLen2){
            placeList[position].loc.substring(0,strLen2).plus("...") //20자 이상이면 문자열 자르고 ...붙이기
        } else placeList[position].loc

        holder.img.clipToOutline = true //안드로이드 버전 5 (롤리팝) 이상부터 적용

//        holder.itemView.setOnClickListener{
//        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    //뷰홀더
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.ivPlaceMain) //이미지
        val name = itemView.findViewById<TextView>(R.id.tvPlaceItemName) //장소 이름
        val loc = itemView.findViewById<TextView>(R.id.tvPlaceItemLoc) //지역
    }

}