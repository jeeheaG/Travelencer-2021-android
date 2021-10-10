package com.example.travelencer_android_2021.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travelencer_android_2021.*
import com.example.travelencer_android_2021.model.ModelCasePlaceCard

class PostWritePlaceSearchAdapter(private val placeList: ArrayList<ModelCasePlaceCard>, private val mContext: Context, private val launcher: ActivityResultLauncher<Intent>): RecyclerView.Adapter<PostWritePlaceSearchAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostWritePlaceSearchAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_place_main, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPosition: Int = adapterPosition
                val place: ModelCasePlaceCard = placeList[curPosition]
                //Toast.makeText(parent.context, "이름:${place.name}, 위치:${place.loc}", Toast.LENGTH_SHORT).show()

                //장소 선택 시 선택한 장소 데이터와 함께 PNC입력 Activity로 이동
                val intent = Intent(mContext, AddPNCActivity::class.java)
                intent.putExtra("placeName", place.name)
                intent.putExtra("placeLoc", place.loc)
                intent.putExtra("placeId", place.contentId)
                intent.putExtra("from", "search")
                launcher.launch(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: PostWritePlaceSearchAdapter.CustomViewHolder, position: Int) {
        Glide.with(mContext).load(placeList[position].img).into(holder.img)
        holder.name.text = placeList[position].name
        holder.loc.text = if(placeList[position].loc.length>20){
            placeList[position].loc.substring(0,20).plus("...") //20자 이상이면 문자열 자르고 ...붙이기
        } else placeList[position].loc

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
    }

}